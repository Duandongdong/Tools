package com.baigu.ui.zxing.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

import com.baigu.ui.zxing.view.ViewfinderView;
import com.google.zxing.Result;

/**
 * Created by JIADONG on 2016/1/14.
 */
public interface CaptureActivityInterface {

    ViewfinderView getViewfinderView();

    void handleDecode(Result obj, Bitmap barcode);

    void setResult(int resultOk, Intent obj);

    void finish();

    void startActivity(Intent intent);

    void drawViewfinder();

    Handler getHandler();
}
