package com.bestgood.commons.sample.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bestgood.commons.activity.TakePictureActivity;
import com.bestgood.commons.sample.R;
import com.bestgood.commons.util.log.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestCameraTakePictureActivity extends Activity {

    @Bind(R.id.picture)
    ImageView mPicture;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_take_picture_test);
        ButterKnife.bind(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, TakePictureActivity.class);
        intent.putExtra(TakePictureActivity.EXTRA_OUTPUT_FILE_PATH, "");//存放拍摄照片的完整路径,包括文件名
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDestroy() {
        if (!(mPicture.getDrawable() instanceof BitmapDrawable)) return;
        // 释放bitmap占用的空间
        BitmapDrawable b = (BitmapDrawable) mPicture.getDrawable();
        b.getBitmap().recycle();
        mPicture.setImageDrawable(null);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(TakePictureActivity.EXTRA_OUTPUT_FILE_PATH);

            // 图片路径
            Logger.i(filePath);

            mPicture.setImageBitmap(decodeSampledBitmapFromFile(filePath, mPicture.getWidth(), mPicture.getHeight()));
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}