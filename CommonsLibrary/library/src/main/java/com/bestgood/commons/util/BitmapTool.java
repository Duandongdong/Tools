package com.bestgood.commons.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;


import com.bestgood.commons.util.log.Logger;
import com.bestgood.commons.ui.widget.toast.ToastHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片操作帮助类
 *
 * @author hebiwen
 */
public class BitmapTool {

    public static final int UNCONSTRAINED = -1;
    private static long maxMem = -1;

    // 960x640/w*h, 拍摄车辆照片压缩后的尺寸
    public static final int TAKE_CAR_PICTURE_WIDTH = 960;
    public static final int TAKE_CAR_PICTURE_HEIGHT = 640;

    public static Bitmap read(InputStream ips, Config config) {
        if (maxMem == -1) {
            maxMem = Runtime.getRuntime().maxMemory();
        }

        long freeMem = Runtime.getRuntime().freeMemory();
        Options opt = new Options();
        if (maxMem / 10 > freeMem) {
            System.gc();
            opt.inJustDecodeBounds = false;
            opt.inSampleSize = 2;
        }
        opt.inPreferredConfig = config;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        return BitmapFactory.decodeStream(ips, null, opt);
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // Bitmap bitmap2 = Bitmap.createBitmap(bitmap);
        Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        final int color = 0xFFEFEFEF;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        paint.setColor(color);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, width / 2, height / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return bitmap2;
    }

    /**
     * 获得设置信息
     */
    public static Options getOptions(String path) {
        Options options = new Options();
        options.inJustDecodeBounds = true;// 只描边，不读取数据
        BitmapFactory.decodeFile(path, options);
        return options;
    }

    /**
     * 获得图像
     *
     * @param path
     * @param options
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapByPath(String path, Options options, int screenWidth, int screenHeight) throws FileNotFoundException {
        System.gc();
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream in = null;
        in = new FileInputStream(file);
        if (options != null) {
            Rect r = getScreenRegion(screenWidth, screenHeight);
            int w = r.width();
            int h = r.height();
            int maxSize = w > h ? w : h;
            int inSimpleSize = computeSampleSize(options, maxSize, w * h);
            inSimpleSize = inSimpleSize * 2 / 3;
            options.inSampleSize = inSimpleSize; // 设置缩放比例
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inDither = false;
            options.inPreferredConfig = Config.RGB_565;
            options.inPreferQualityOverSpeed = true;
            options.inTempStorage = new byte[1024 * 1024 * 2];
        }
        Bitmap b = BitmapFactory.decodeStream(in, null, options);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 获取需要进行缩放的比例，即options.inSampleSize
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static Rect getScreenRegion(int width, int height) {
        return new Rect(0, 0, width, height);
    }

    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
                .ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
                .min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == UNCONSTRAINED)
                && (minSideLength == UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    // 压缩图片尺寸
    public static Bitmap getResizeBitmap(String pathName, int targetWidth, int targetHeight) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;

        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);

        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    // 存储进SD卡
    public static void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        // 检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete(); // 删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    // 读取图片的角度
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            Logger.d("degree:%d", degree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 将图片压缩为系统分辨率大小，并旋转
     *
     * @param path
     */
    public static void processCameraBitmap(String path) {
        // WindowManager wm = (WindowManager)
        // context.getSystemService(Context.WINDOW_SERVICE);
        // Display display = wm.getDefaultDisplay();
        // Display display = activity.getWindowManager().getDefaultDisplay();
        // Point size = new Point();
        // display.getSize(size);
        // int width = size.x;
        // int height = size.y;

        // 缩小图片尺寸为固定值
        Bitmap bitmap = BitmapTool.getResizeBitmap(path,
                TAKE_CAR_PICTURE_WIDTH,
                TAKE_CAR_PICTURE_HEIGHT);
        // 是否需要旋转图片
        int degree = BitmapTool.readPictureDegree(path);
        if (degree != 0) {// 旋转照片角度
            bitmap = BitmapTool.rotateBitmap(bitmap, degree);
        }
        // 保存压缩后的图片
        try {
            BitmapTool.saveFile(bitmap, path);
        } catch (Exception e) {
            Logger.e(e, "processCameraBitmap exception, path is: %s", path);
        } finally {
            bitmap.recycle();
        }
    }

    /**
     * 是否是横屏拍摄的照片
     *
     * @param context
     * @param path
     * @return
     */
    public static boolean isHorizontalPicture(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            return false;
        }
        Logger.d("width:%d; height:%d", bitmap.getWidth(), bitmap.getHeight());
        if (bitmap.getWidth() <= bitmap.getHeight()) {//注意这里图片的旋转角度必须已处理为0
            ToastHelper.show(context, "必须横屏拍摄照片！");
            try {
                FileUtils.deleteFile(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
     *
     * @param f             图片文件
     * @param required_size 最大的宽度尺寸
     * @return
     */
    public static Bitmap decodeFile(File f, int required_size) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inPreferredConfig = Config.ARGB_8888;
            o.inPurgeable = true;
            o.inInputShareable = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = required_size;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}
