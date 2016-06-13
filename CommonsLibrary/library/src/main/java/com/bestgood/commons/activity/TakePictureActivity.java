package com.bestgood.commons.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.bestgood.commons.R;
import com.bestgood.commons.ui.widget.LoadingDialog;
import com.bestgood.commons.ui.widget.toast.ToastHelper;
import com.bestgood.commons.util.log.Logger;

/**
 * http://blog.csdn.net/u012964281/article/details/41143169
 * http://www.jianshu.com/p/23caee6cad0f
 * http://www.lai18.com/content/1165371.html
 */
public class TakePictureActivity extends Activity {

    public static final String EXTRA_OUTPUT_FILE_PATH = "com.bestgood.commons.EXTRA_OUTPUT_FILE_PATH";

    private final float PICTURE_WIDTH = 1280;
    private final float PICTURE_HEIGHT = 720;
    private final float PICTURE_RATIO = PICTURE_WIDTH / PICTURE_HEIGHT;


    enum Orientation {
        OrientationUnknown(-1),
        OrientationPortrait(90),
        PortraitUpsideDown(270),
        OrientationLandscapeLeft(180),
        OrientationLandscapeRight(0);

        int rotation;

        Orientation(int rotation) {
            this.rotation = rotation;
        }
    }


    SurfaceView mSurfaceView;
    ImageView mFlashLightView;

    Camera mCamera;
    OrientationEventListener orientationListener;

    Orientation orientation; // 当前设备方向

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_take_picture);

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mFlashLightView = (ImageView) findViewById(R.id.iv_flash_light);

        enableOrientationChangeListener(); // 启动设备方向监听器

        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(surfaceHolder); // 回调接口
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开启相机
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
            // i=0 表示后置相机
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放相机
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        disableOrientationChangeListener();
        super.onDestroy();
    }


    public void onClick(View v) {
        Logger.object(orientation);
        if (v.getId() == R.id.iv_close) {
            finish();
        } else if (v.getId() == R.id.iv_flash_light) {
            changeFlashLight();
        } else if (v.getId() == R.id.iv_take_picture) {
            if (mCamera == null) {
                ToastHelper.show(getBaseContext(), "您的手机没有拍摄照片功能!");
                return;
            }
            if (orientation == null || (orientation != Orientation.OrientationLandscapeLeft && orientation != Orientation.OrientationLandscapeRight)) {
                ToastHelper.show(getBaseContext(), "请横屏拍摄照片!");
                return;
            }
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    saveToSDCard(data);
                }
            });
        }
    }


    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data
     * @throws IOException
     */
    private void saveToSDCard(final byte[] data) {
        final LoadingDialog dialog = LoadingDialog.createDialog(this);
        dialog.setMessage("保存照片...");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                final File filePath = new File(getPictureFilePath());
                File parent = filePath.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }

                FileOutputStream out = null; // 文件输出流
                try {
                    out = new FileOutputStream(filePath);

                    byte[] newData;
                    // 竖屏时，旋转图片再保存
                    Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    Matrix matrix = new Matrix();
                    matrix.setRotate(orientation.rotation);
                    Logger.d(String.valueOf(orientation.rotation));

                    Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    newData = baos.toByteArray();
                    out.write(newData);

                    //这个的作用是让系统去扫描刚拍下的这个图片文件，以利于在MediaSore中能及时更新，
                    // 可能会存在部分手机不用使用的情况（众所周知，现在国内的Rom厂商已把原生Rom改的面目全非）
                    //mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mPictureSavePath)));
                    MediaScannerConnection.scanFile(TakePictureActivity.this, new String[]{filePath.getPath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    // Log.e(TAG, "扫描完成");
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                        Intent i = new Intent();
                        i.putExtra(EXTRA_OUTPUT_FILE_PATH, filePath.getPath());
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });

            }
        }).start();


    }

    /**
     * 得到图片保存的文件路径
     *
     * @return
     */
    public String getPictureFilePath() {
        String filePath = getIntent().getStringExtra(EXTRA_OUTPUT_FILE_PATH);
        if (TextUtils.isEmpty(filePath)) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA); // 格式化时间
            String filename = format.format(date) + ".jpg";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filePath = Environment.getExternalStorageDirectory() + File.separator + "BestgoodCamera" + File.separator + filename;
            } else {
                filePath = getCacheDir().getAbsolutePath() + File.separator + "BestgoodCamera" + File.separator + filename;
            }
        }
        return filePath;
    }

    private SurfaceHolder.Callback surfaceHolder = new SurfaceHolder.Callback() {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
            Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象

            Size previewSize = getPreviewSize(parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(previewSize.width, previewSize.height);// 设置预览图片尺寸

            Size picture = getPictureSize(parameters.getSupportedPictureSizes());// 设置捕捉图片尺寸
            parameters.setPictureSize(picture.width, picture.height);

            //parameters.setPreviewFpsRange(4, 10);//fps
            parameters.setJpegQuality(100); // 设置照片质量

            if (parameters.getSupportedFlashModes().contains(Parameters.FLASH_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//自动对焦
            }

            mCamera.setParameters(parameters);

            try {
                mCamera.startPreview();
            } catch (Exception e) {
                if (mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // SurfaceView创建时，建立Camera和SurfaceView的联系
            if (mCamera != null) {
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // SurfaceView销毁时，取消Camera预览
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        }
    };


    public Size getPreviewSize(List<Size> list) {
        Logger.object(list);
        Collections.sort(list, new CameraSizeComparator());
        Logger.object(list);
        return list.get(0);
    }

    public Size getPictureSize(List<Camera.Size> list) {
        Logger.object(list);
        Collections.sort(list, new CameraSizeComparator());
        Logger.object(list);
        return list.get(0);
    }


    private void enableOrientationChangeListener() {
        orientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (rotation > 45 && rotation < 135) {
                    orientation = Orientation.OrientationLandscapeLeft;
                } else if (rotation > 225 && rotation < 315) {
                    orientation = Orientation.OrientationLandscapeRight;
                } else if ((rotation >= 0 && rotation <= 45) || rotation >= 315) {
                    orientation = Orientation.OrientationPortrait;
                } else if (rotation >= 135 && rotation <= 225) {
                    orientation = Orientation.PortraitUpsideDown;
                } else {
                    orientation = Orientation.OrientationUnknown;
                }
            }
        };
        orientationListener.enable();
    }

    private void disableOrientationChangeListener() {
        if (orientationListener != null) {
            orientationListener.disable();
        }
    }


    private void changeFlashLight() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }

        String flashMode = parameters.getFlashMode();
        if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
            if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
            mFlashLightView.setImageResource(R.drawable.btn_camera_flash_off);
        } else if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            if (flashModes.contains(Parameters.FLASH_MODE_ON)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                mCamera.setParameters(parameters);
            }
            mFlashLightView.setImageResource(R.drawable.btn_camera_flash_on);
        } else {
            if (flashModes.contains(Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mCamera.setParameters(parameters);
            }
            mFlashLightView.setImageResource(R.drawable.btn_camera_flash_auto);
        }
    }


    class CameraSizeComparator implements Comparator<Size> {
        //按升序排列
        public int compare(Size lhs, Size rhs) {

            float lhsMinusRatio = Math.abs((float) lhs.width / (float) lhs.height - PICTURE_RATIO);
            float rhsMinusRatio = Math.abs((float) rhs.width / (float) rhs.height - PICTURE_RATIO);

            if (lhsMinusRatio != rhsMinusRatio) {
                return lhsMinusRatio > rhsMinusRatio ? 1 : -1;
            }

            float lhsMinusSize = Math.abs(lhs.width - PICTURE_WIDTH);
            float rhsMinusSize = Math.abs(rhs.width - PICTURE_WIDTH);

            float minus = lhsMinusSize - rhsMinusSize;

            return minus == 0 ? 0 : (minus > 0 ? 1 : -1);
        }
    }
}