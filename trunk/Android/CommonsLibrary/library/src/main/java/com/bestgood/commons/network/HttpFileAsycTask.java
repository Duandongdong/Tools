package com.bestgood.commons.network;

import java.io.File;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 异步任务进行下载
 *
 * @author Youjk
 */
public class HttpFileAsycTask extends AsyncTask<String, Integer, String> {
    private Map<String, Map<String, File>> files;
    private Map<String, String> params;
    private ProgressDialog pDialog;
    private Activity activity;

    public HttpFileAsycTask(Map<String, Map<String, File>> files, Activity activity,
                            Map<String, String> params) {
        this.files = files;
        this.params = params;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("数据上传中。。。");
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... url) {
        return new HttpFileUpTool().post(url[0], params, files);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pDialog.dismiss();
        activity.finish();
        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
    }
}