package com.bestgood.commons.sample.ui.widget.TouchImage;


import android.app.Activity;
import android.os.Bundle;

import com.bestgood.commons.sample.R;
import com.bestgood.commons.ui.widget.TouchImageView;

public class MirroringExampleActivity extends Activity {

    TouchImageView topImage;
    TouchImageView bottomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_touch_image_mirroring);
        topImage = (TouchImageView) findViewById(R.id.topImage);
        bottomImage = (TouchImageView) findViewById(R.id.bottomImage);

        //
        // Each image has an OnTouchImageViewListener which uses its own TouchImageView
        // to set the other TIV with the same zoom variables.
        //
        topImage.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                bottomImage.setZoom(topImage);
            }
        });

        bottomImage.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {

            @Override
            public void onMove() {
                topImage.setZoom(bottomImage);
            }
        });
    }
}
