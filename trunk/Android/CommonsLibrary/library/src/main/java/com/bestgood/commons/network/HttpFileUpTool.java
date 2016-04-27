//package com.bestgood.commons.network;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.util.Map;
//
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//
//
//import com.bestgood.commons.util.BitmapTool;
//import com.bestgood.commons.ui.widget.toast.ToastHelper;
//import com.bestgood.commons.util.log.Logger;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.Log;
//
///**
// * 上传图片
// *
// * @author Youjk
// */
//public class HttpFileUpTool {
//    public static final int REQUEST_SIZE = 480;
//    private static final int CONNECTION_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
//    private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
//    public static int statusCode;// 状态码
//    public static final String TAG = "HttpFileUpTool-->";
//
//    /**
//     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
//     *
//     * @param actionUrl
//     * @param params
//     * @param maps
//     * @return
//     * @throws IOException
//     */
//    public String post(String actionUrl, Map<String, String> params, Map<String, Map<String, File>> maps) {
//        StringBuilder sb2;
//        String back = null;
//        String BOUNDARY = java.util.UUID.randomUUID().toString();
//        String PREFIX = "--", LINEND = "\r\n";
//        String MULTIPART_FROM_DATA = "multipart/form-data";
//        String CHARSET = "UTF-8";
//
//        URL uri;
//        try {
//            uri = new URL(actionUrl);
//            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
//            conn.setReadTimeout(10 * 1000); // 缓存的最长时间
//            conn.setDoInput(true);// 允许输入
//            conn.setDoOutput(true);// 允许输出
//            conn.setUseCaches(false); // 不允许使用缓存
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Charsert", "UTF-8");
//            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
//
//            // 首先组拼文本类型的参数
//            StringBuilder sb = new StringBuilder();
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINEND);
//                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
//                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
//                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//                sb.append(LINEND);
//                sb.append(entry.getValue());
//                sb.append(LINEND);
//            }
//            DataOutputStream outStream = new DataOutputStream(
//                    conn.getOutputStream());
//            outStream.write(sb.toString().getBytes());
//            InputStream in = null;
//            // 发送文件数据
//            if (maps != null) {
//                for (Map.Entry<String, Map<String, File>> map : maps.entrySet()) {
//                    String path = map.getKey();
//                    Map<String, File> files = map.getValue();
//                    if (files != null) {
//                        for (Map.Entry<String, File> file : files.entrySet()) {
//                            Log.i(TAG + "文件名", file.getKey());
//                            StringBuilder sb1 = new StringBuilder();
//                            sb1.append(PREFIX);
//                            sb1.append(BOUNDARY);
//                            sb1.append(LINEND);
//                            sb1.append("Content-Disposition: form-data; name=\""
//                                    + path
//                                    + "\"; filename=\""
//                                    + file.getKey()
//                                    + "\"" + LINEND);
//                            sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
//                            sb1.append(LINEND);
//                            outStream.write(sb1.toString().getBytes());
//                            // Bitmap image =
//                            // BitmapFactory.decodeFile(file.getKey());
//                            Bitmap image = BitmapTool.decodeFile(new File(file.getKey()), REQUEST_SIZE);
//                            InputStream isBm = compressImage(image);
//                            // InputStream isBm = new
//                            // FileInputStream(file.getValue());
//                            byte[] buffer = new byte[1024];
//                            int len = 0;
//                            while ((len = isBm.read(buffer)) != -1) {
//                                outStream.write(buffer, 0, len);
//                            }
//                            isBm.close();
//                            outStream.flush();
//                            outStream.write(LINEND.getBytes());
//                            if (!image.isRecycled()) {
//                                image.recycle();
//                                image = null;
//                                System.gc();
//                            }
//                        }
//                    }
//                }
//            }
//            // 请求结束标志
//            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
//            outStream.write(end_data);
//            outStream.flush();
//            // 得到响应码
//            int res = conn.getResponseCode();
//            String responseMessage = conn.getResponseMessage();
//            Logger.t(TAG).i("response_code", res + "");
//            Logger.t(TAG).i("responseMessage", responseMessage + "");
//            if (res == 200) {
//                in = conn.getInputStream();
//                int ch;
//                sb2 = new StringBuilder();
//                while ((ch = in.read()) != -1) {
//                    sb2.append((char) ch);
//                }
//                System.out.println(sb2.toString());
//                back = "上传成功";
//            } else {
//                back = "上传失败";
//            }
//            outStream.close();
//            conn.disconnect();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return back;
//    }
//
//    public static ByteArrayInputStream compressImage(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        Logger.t(TAG).i("文件字节长度[byte]", baos.toByteArray().length + "");
//        if (baos.toByteArray().length > 3000) {
//            while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                baos.reset();// 重置baos即清空baos
//                options -= 20;// 每次都减少10
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//                Log.e("baosss", baos.toByteArray().length + "比例" + options);
//                Log.e("options", options + "");
//                if (options <= 0) {
//                    break;
//                }
//            }
//        } else {
//            while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                baos.reset();// 重置baos即清空baos
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//                options -= 10;// 每次都减少10
//            }
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
//        // null);//把ByteArrayInputStream数据生成图片
//        return isBm;
//    }
//
//    // public static Bitmap comp(Bitmap image) {
//    //
//    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    // image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//    // if( baos.toByteArray().length / 1024>1024)
//    // {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//    // baos.reset();//重置baos即清空baos
//    // image.compress(Bitmap.CompressFormat.JPEG, 50,
//    // baos);//这里压缩50%，把压缩后的数据存放到baos中
//    // }
//    // ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//    // BitmapFactory.Options newOpts = new BitmapFactory.Options();
//    // //开始读入图片，此时把options.inJustDecodeBounds 设回true了
//    // newOpts.inJustDecodeBounds = true;
//    // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//    // newOpts.inJustDecodeBounds = false;
//    // int w = newOpts.outWidth;
//    // int h = newOpts.outHeight;
//    // //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//    // float hh = 800f;//这里设置高度为800f
//    // float ww = 480f;//这里设置宽度为480f
//    // //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//    // int be = 1;//be=1表示不缩放
//    // if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//    // be = (int) (newOpts.outWidth / ww);
//    // } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//    // be = (int) (newOpts.outHeight / hh);
//    // }
//    // if (be <= 0)
//    // be = 1;
//    // newOpts.inSampleSize = be;//设置缩放比例
//    // //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//    // isBm = new ByteArrayInputStream(baos.toByteArray());
//    // bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//    // return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
//    // }
//    public static InputStream Bitmap2InputStream(Bitmap bm) {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        InputStream is = new ByteArrayInputStream(baos.toByteArray());
//        return is;
//    }
//
//    /**
//     * post请求
//     *
//     * @param context 上下文
//     * @param url     请求url地址
//     * @param params  参数map集合----可为null
//     * @param files   文件map集合-----可为null
//     * @param token   令牌----可为null
//     * @return 服务器响应
//     */
//    public static String doPost(Context context, String url, Map<String, String> params, Map<String, File> files, String token) {
//        String result = "";
//        HttpPost post = new HttpPost(url);
//        // post.setHeader("Auth-Token", token);
//        // post.setHeader("Accept", "application/json");
//        // post.setHeader("Content-Type", "application/json;charset=UTF-8");
//        MultipartEntity entity = new MultipartEntity();
//        // 添加参数
//        if (params != null && !params.isEmpty()) {
//            for (String key : params.keySet()) {
//                try {
//                    entity.addPart(key, new StringBody(params.get(key), Charset.forName(HTTP.UTF_8)));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        // try {
//        // entity.addPart("id",
//        // new StringBody("7330", Charset.forName(HTTP.UTF_8)));
//        // } catch (UnsupportedEncodingException e1) {
//        // e1.printStackTrace();
//        // }
//        // 添加文件
//        int asd = 0;
//        if (files != null && !files.isEmpty()) {
//            for (String key : files.keySet()) {
//                if (files.get(key).exists()) {
//                    asd++;
//                    Log.i("YANGBANG1234", "正在上传第" + asd + "张图片");
//                    // FileBody fileBody = new FileBody(files.get(key),
//                    // "application/octet-stream");
//                    entity.addPart(key, new FileBody(files.get(key)));
//                    // array.put(new FileBody(files.get(key)));
//                    // "application/octet-stream"));
//                    // FormBodyPart bodyPart = new FormBodyPart(files.get(key)
//                    // .getName(), fileBody);
//                    // entity.addPart(bodyPart);
//                }
//            }
//        }
//        Log.i("YANGBANG1111", "setEntity");
//        post.setEntity(entity);
//        try {
//            HttpResponse httpResponse = getHttpClient().execute(post);
//            statusCode = httpResponse.getStatusLine().getStatusCode();
//            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//            Log.i("statusCode", statusCode + "");
//            Log.i("result", result);
//            if (statusCode == 200 || statusCode == 201) {
//                return result;
//            } else {
//                Log.i("No201 Or 200", "No201 Or 200");
//                Log.i("response", "response-->" + statusCode);
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            ToastHelper.show(context, "连接超时");
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 取得HttpCilent实例
//     *
//     * @return HttpClient实例
//     */
//    private static HttpClient getHttpClient() {
//        HttpParams httpParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
//        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
//        HttpClient httpClient = new DefaultHttpClient(httpParams);
//        return httpClient;
//    }
//
//}