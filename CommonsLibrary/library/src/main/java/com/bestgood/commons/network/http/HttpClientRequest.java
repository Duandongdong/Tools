
package com.bestgood.commons.network.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bestgood.commons.util.GsonFactory;
import com.bestgood.commons.util.config.AppConfig;
import com.bestgood.commons.util.log.Logger;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * @author ddc
 * @date: Apr 20, 2014 9:28:39 PM
 */
public abstract class HttpClientRequest<RESULT extends HttpClientResponse> extends SpiceRequest<RESULT> {

    private String TAG_TIME = "REQUEST_TIME";

    /* AOP JSON 格式 */
    public static final String FORMAT_JSON = "json";
    /* AOP XML 格式 */
    public static final String FORMAT_XML = "xml";
    /* application/x-www-form-urlencoded */
    public static final String FORMAT_URLENCODED = "urlencoded";
    /* HTTP GET method. */
    public static final String HTTP_METHOD_GET = "GET";
    /* HTTP POST method. */
    public static final String HTTP_METHOD_POST = "POST";

    // ============================================================================
    /* 连接超时时间 */
    private final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    /* 读取数据超时时间 */
    private final int DEFAULT_READ_TIMEOUT = 30 * 1000;
    /* 构造xml请求参数时，通用的xml头 */
    private final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    // ============================================================================
    private Context mContext;

    /* HTTP request URL. */
    private HttpClientUrl mHttpClientUrl;
    /* Timeout in milliseconds to establish a connection or {@code 0} for an infinite timeout. */
    private int mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
    /* Timeout in milliseconds to read data from an established connection or {@code 0} for an infinite timeout. */
    private int mReadTimeout = DEFAULT_READ_TIMEOUT;
    /* HTTP request method. 默认 为 {@link #HTTP_METHOD_POST} */
    private String mRequestMethod = HTTP_METHOD_POST;
    /* Post 请求参数格式，只支持 {@link #FORMAT_JSON} 和 {@link #FORMAT_XML}。默认为 {@link #FORMAT_JSON} */
    private String mRequestContentFormat = FORMAT_JSON;
    /* Post 简析返回内容格式，只支持 {@link #FORMAT_JSON} 和 {@link #FORMAT_XML}。默认为 {@link #FORMAT_JSON} */
    private String mResponseContentFormat = FORMAT_JSON;
    /* 返回的数据是否简析 为 对象，为 true时返回简析的对象，为false时返回将返回的数据 保存为字符串 resStr */
    private boolean mParser = true;

    /* 返回xml内容格式名称空间 */
    private Map<String, String> mXmlNamespaceDictionaryMap = new HashMap<String, String>();

    // ============================================================================
    private HttpRequestFactory mHttpRequestFactory;

    /**
     * requestMethod 默认为 {@link #HTTP_METHOD_POST}
     *
     * @param baiguGenericUrl baiguGenericUrl
     */
    public HttpClientRequest(Class<RESULT> clazz, HttpClientUrl baiguGenericUrl) {
        this(clazz, baiguGenericUrl, HTTP_METHOD_POST);
    }

    /**
     * @param clazz
     * @param httpClientUrl
     * @param requestMethod HttpMethod {@link #HTTP_METHOD_POST} or  {@link #HTTP_METHOD_GET}
     */
    public HttpClientRequest(Class<RESULT> clazz, HttpClientUrl httpClientUrl, String requestMethod) {
        super(clazz);
        setRetryPolicy(null);// 请求失败后，不尝试 重新 请求
        this.mHttpClientUrl = httpClientUrl;
        this.mRequestMethod = requestMethod;
    }

    // ============================================================================
    // ============================================================================

    /**
     * 获取所有的Key-Value形式的请求参数集合。其中： Key: 请求参数名 Value: 请求参数值。GET 请求方式需覆盖此方法，获取请求参数
     *
     * @return 请求参数集合
     */
    private Map<? extends String, ?> initFieldValuePairs() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers)) {
                continue;
            }
            field.setAccessible(true);
            try {
                // Object obj = field.get(this);
                // if (obj != null) {
                // String objVal = obj.toString();
                // if (!TextUtils.isEmpty(objVal)) {
                map.put(field.getName(), field.get(this));
                // }
                // }
            } catch (Exception e) {
                Logger.t(getClass().getSimpleName()).e(e, "");
            }
        }
        return map;
    }

    /**
     * Sets the all given field value for the given field name.
     * Any existing value for the field will be overwritten.
     */
    private void buildGetEntity() {
        Map<? extends String, ?> map = initFieldValuePairs();
        if (map == null || map.isEmpty()) {
            return;
        }
        mHttpClientUrl.putAll(map);
    }

    /**
     * 获取String类型的 entity 请求参数。POST 请求方式 如不是 json 格式 需覆盖此方法，获取请求参数
     *
     * @return
     */
    protected String buildPostEntity() {
        buildCommFiledsValue(mContext);
        if (FORMAT_JSON.equals(mRequestContentFormat)) {
            Gson gson = GsonFactory.buildOnlyIncludePublicFieldsGson();
            return gson.toJson(this);
        }
        return "";
    }


    /**
     * 初始化所有接口都有的三个通用字段
     * 注意：因为要使用到Context,所以必须在mContext参数赋值后调用,目前mContext是在Request中添加到Service时赋值,
     * 在excute()方法执行中都可使用Context,现在buildPostEntity()方法中调用,子类如果覆盖buildPostEntity()方法,需要注意在构造json串前条用次方法赋值
     *
     * @param context
     */
    protected abstract void buildCommFiledsValue(Context context);

    // ============================================================================
    protected Map<String, String> getXmlNamespaceDictionaryMap() {
        return mXmlNamespaceDictionaryMap;
    }

    /**
     * 增加一个XmlNamespaceDictionary
     *
     * @param alias
     * @param uri
     */
    public void addXmlNamespaceDictionary(String alias, String uri) {
        mXmlNamespaceDictionaryMap.put(alias, uri);
    }

    // ============================================================================
    @Override
    public RESULT loadDataFromNetwork() throws Exception {
        return excute(getHttpRequestFactory());
    }

    protected RESULT excute(HttpRequestFactory requestFactory) throws IOException, InstantiationException, IllegalAccessException {
        //long startTime = System.currentTimeMillis();

        HttpRequest httpRequest;
        HttpResponse httpResponse;


        //用户手机号码,加密解密用,需放进头字段中
        String phoneNumber = getPhoneNumber(mContext);

        if (HTTP_METHOD_POST.equals(mRequestMethod)) {
            String entityContent = buildPostEntity();
            entityContent = encrypt(entityContent, phoneNumber);
            httpRequest = requestFactory.buildPostRequest(mHttpClientUrl, buildPostContent(entityContent));
        } else if (HTTP_METHOD_GET.equals(mRequestMethod)) {
            buildGetEntity();
            httpRequest = requestFactory.buildGetRequest(mHttpClientUrl);
        } else {
            throw new IllegalStateException("The Http Request Method mast be GET or POST.");
        }

        // -----------------------------------------------------------------------------

        putHeaders(httpRequest.getHeaders());
        httpRequest.getHeaders().set("mimatype", phoneNumber);
        httpRequest.setConnectTimeout(mConnectTimeout);
        httpRequest.setReadTimeout(mReadTimeout);

        //long executeTime = System.currentTimeMillis();
        httpResponse = httpRequest.execute();
        //Logger.t(TAG_TIME).i("execute time:%s", System.currentTimeMillis() - executeTime);

        // -------------------------------------------------------------------

        //long parseAsStringTime = System.currentTimeMillis();
        String responseStr = httpResponse.parseAsString();
        //Logger.t(TAG_TIME).i("parse as String time:%s", System.currentTimeMillis() - parseAsStringTime);

        responseStr = decrypt(responseStr, phoneNumber);

        //long jsonParserTime = System.currentTimeMillis();
        RESULT response = getResultType().newInstance();
        if (mParser) {
            Gson gson = GsonFactory.buildOnlyIncludeExposeAnnotationGson();
            response = gson.fromJson(responseStr, getResultType());
            response.setStatusCode(httpResponse.getStatusCode());
            response.setStatusMessage(httpResponse.getStatusMessage());
        } else {
            response.setStatusCode(httpResponse.getStatusCode());
            response.setStatusMessage(httpResponse.getStatusMessage());
            response.setResultStr(responseStr);
        }

        //Logger.t(TAG_TIME).i("json parser time:%s", System.currentTimeMillis() - jsonParserTime);
        //Logger.t(TAG_TIME).i("all time:%s", System.currentTimeMillis() - startTime);

        return response;
    }

    protected void putHeaders(HttpHeaders headers) {
    }

    private HttpContent buildPostContent(String entityContent) {
        String format = mRequestContentFormat;

        byte[] array;
        if (TextUtils.isEmpty(entityContent)) {
            array = "".getBytes();
        } else {
            array = entityContent.getBytes();
        }
        // --------------------------------------------------------------------
        if (FORMAT_XML.equals(format)) {
            return new ByteArrayContent("text/xml", array);
        } else if (FORMAT_JSON.equals(format)) {
            return new ByteArrayContent("text/json", array);
        } else if (FORMAT_URLENCODED.equals(format)) {
            Map<? extends String, ?> map = initFieldValuePairs();
            if (map == null) {
                map = new HashMap<String, String>();
            }
            return new UrlEncodedContent(map);
        } else {
            String current;
            if (format == null) {
                current = "NULL";
            } else if ("".equals(format)) {
                current = "EMPTY";
            } else {
                current = format;
            }
            throw new IllegalStateException("The Http Request entity format mast be XML,JSON,URLENCODED;but current is " + current);
        }
    }

    /**
     * 加密请求参数
     *
     * @param entityContent
     * @param phoneNumber
     * @return
     */
    private String encrypt(String entityContent, String phoneNumber) {
        Logger.t(getClass().getSimpleName()).i("request decrypt entity = %s", entityContent);

//        String pk = dealNumber(phoneNumber);
//        long md5StrStart = System.currentTimeMillis();
//        String key = getMD5Str("czgjService10" + getDate() + pk);
//        Logger.t(TAG_TIME).i("Get Key MD5 Str Time:%s", System.currentTimeMillis() - md5StrStart);
//        Logger.i("key = %s", key);
//
//        long encryptRequestTime = System.currentTimeMillis();
//        CZJAes jj = new CZJAes(key);
//        entityContent = jj.encrypt(entityContent, "utf-8");
//        Logger.t(TAG_TIME).i("encrypt request time:%s", System.currentTimeMillis() - encryptRequestTime);
//
//        Logger.t(getClass().getSimpleName()).i("request encrypt entity = %s", entityContent);

        return entityContent;
    }

    private String decrypt(String responseStr, String phoneNumber) {
        //Logger.t(getClass().getSimpleName()).i("response encrypt str = %s", responseStr);

        if (!AppConfig.isPark()) {//帕克停车项目不需要加密解密
            //long decryptTime = System.currentTimeMillis();

            String pk = dealNumber(phoneNumber);
            String key = getMD5Str("czgjService10" + getDate() + pk);

            CZJAes jj = new CZJAes(key);
            responseStr = jj.decrypt(responseStr, "utf-8");

            //Logger.t(TAG_TIME).i("decrypt time:%s", System.currentTimeMillis() - decryptTime);
        }
        Logger.t(getClass().getSimpleName()).json(responseStr);

        return responseStr;
    }
    // =========================================================================================

    /**
     * 带请求参数的url
     *
     * @return
     */
    public String getUrl() {
        buildGetEntity();
        return mHttpClientUrl.build();
    }

    /**
     * 不带请求参数的url
     *
     * @return
     */
    public String getUrlHosts() {
        return mHttpClientUrl.build();
    }

    public void setRequestMethod(String requestMethod) {
        this.mRequestMethod = requestMethod;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.mConnectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.mReadTimeout = readTimeout;
    }

    public void setHttpClientUrl(HttpClientUrl httpClientUrl) {
        this.mHttpClientUrl = httpClientUrl;
    }

    protected HttpRequestFactory getHttpRequestFactory() {
        return mHttpRequestFactory;
    }

    /* package */void setContext(Context context) {
        this.mContext = context;
    }

    /* package */void setHttpRequestFactory(
            HttpRequestFactory httpRequestFactory) {
        this.mHttpRequestFactory = httpRequestFactory;
    }

    public void setRequestContentFormat(String requestContentFormat) {
        this.mRequestContentFormat = requestContentFormat;
    }

    /* package */String getResponseContentFormat() {
        return mResponseContentFormat;
    }

    public void setResponseContentFormat(String responseContentFormat) {
        this.mResponseContentFormat = responseContentFormat;
    }

    /* package */boolean isParser() {
        return mParser;
    }

    public void setParser(boolean isParser) {
        this.mParser = isParser;
    }

    // =======================================================================================

    private String getDate() {
        Date currentdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.CHINA);
        String formatedDate = sdf.format(currentdate);
        int year = Integer.parseInt(formatedDate);
        year = year - 1;
//        if (year > 9) {
//            formatedDate = year + formatedDate.substring(2, 6);
//        } else {
//            formatedDate = "0" + year + formatedDate.substring(2, 6);
//        }
        return String.valueOf(year);
    }

    /**
     * MD5 加密
     */
    private String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            Logger.t(getClass().getSimpleName()).e(e, "getMD5Str(%s)", str);
        } catch (UnsupportedEncodingException e) {
            Logger.t(getClass().getSimpleName()).e(e, "getMD5Str(%s)", str);
        }
        if (messageDigest == null) {
            return "";
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    private String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        Logger.t(getClass().getSimpleName()).i("number = %s", number);
        if (number == null) {
            number = "0000000000";
        } else {
            int length = number.length();
            if (length > 10) {
            } else if (length < 10) {
                number = number + getStringZero(10 - length);
            }
        }
        return number;
    }

    private String dealNumber(String pn) {
        String res = "";
        int size = pn.length();
        for (int i = size; i > (size - 10); i--) {
            char current = pn.charAt(i - 1);
            res = res + current;
        }
        return res;
    }

    private String getStringZero(int size) {
        String res = "";
        for (int i = 0; i < size; i++) {
            res += "0";
        }
        return res;
    }
}