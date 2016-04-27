
package com.bestgood.commons.network.http;

import android.content.Context;

import com.bestgood.commons.util.GsonFactory;
import com.bestgood.commons.util.config.AppConfig;
import com.bestgood.commons.util.log.Logger;
import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ddc
 * @date: Apr 20, 2014 9:28:39 PM
 */
public abstract class AbsHttpRequest<RESULT extends AbsHttpResponse> extends SpiceRequest<RESULT> {
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
    private final int DEFAULT_CONNECT_TIMEOUT = 15 * 1000;
    /* 读取数据超时时间 */
    private final int DEFAULT_READ_TIMEOUT = 10 * 1000;
    /* 构造xml请求参数时，通用的xml头 */
    private final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    // ============================================================================

    private Context mContext;

    /* HTTP request URL. */
    private HttpUrl mHttpUrl;
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

    /**
     * requestMethod 默认为 {@link #HTTP_METHOD_POST}
     *
     * @param baiguGenericUrl baiguGenericUrl
     */
    public AbsHttpRequest(Class<RESULT> clazz, HttpUrl baiguGenericUrl) {
        this(clazz, baiguGenericUrl, HTTP_METHOD_POST);
    }

    /**
     * @param clazz
     * @param httpUrl
     * @param requestMethod HttpMethod {@link #HTTP_METHOD_POST} or  {@link #HTTP_METHOD_GET}
     */
    public AbsHttpRequest(Class<RESULT> clazz, HttpUrl httpUrl, String requestMethod) {
        super(clazz);
        setRetryPolicy(null);// 请求失败后，不尝试 重新 请求
        this.mHttpUrl = httpUrl;
        this.mRequestMethod = requestMethod;
    }

    // ============================================================================

    @Override
    public RESULT loadDataFromNetwork() throws Exception {
        return excute();
    }

    protected RESULT excute() throws IOException, InstantiationException, IllegalAccessException {

        String responseStr;

        //用户手机号码,加密解密用,需放进头字段中
        String mimatype = CryptoUtils.getPhoneNumber(mContext);

        if (HTTP_METHOD_GET.equals(mRequestMethod)) {
            responseStr = getRequestResponse(mimatype);
        } else if (HTTP_METHOD_POST.equals(mRequestMethod)) {
            responseStr = postRequestResponse(mimatype);
        } else {
            throw new IllegalStateException("The Http Request Method mast be GET or POST.");
        }

        // -----------------------------------------------------------------------------


        responseStr = CryptoUtils.decrypt(responseStr, mimatype);
        Logger.t(getClass().getSimpleName()).json(responseStr);

        RESULT response = getResultType().newInstance();
        if (mParser) {
            Gson gson = GsonFactory.buildOnlyIncludeExposeAnnotationGson();
            response = gson.fromJson(responseStr, getResultType());
        } else {
            response.setResultStr(responseStr);
        }
        return response;
    }


    private String getRequestResponse(String mimatype) throws IOException {
        buildGetEntity();

        URL url = new URL(getUrl());
        Logger.t(getClass().getSimpleName()).i("%s", url.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setConnectTimeout(mConnectTimeout);
        conn.setReadTimeout(mReadTimeout);
        conn.setRequestProperty("mimatype", mimatype);
        setRequestProperty(conn);
        conn.connect();

        return IOUtils.toString(new InputStreamReader(conn.getInputStream(), CharEncoding.UTF_8));
    }

    private String postRequestResponse(String mimatype) throws IOException {
        String entityContent = buildPostEntity();

        URL url = new URL(getUrlHosts());
        Logger.t(getClass().getSimpleName()).i("%s", url.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("mimatype", mimatype);
        setRequestProperty(conn);
        if (FORMAT_XML.equals(mRequestContentFormat)) {
            conn.setRequestProperty("Content-Type", "application/xml; charset=utf-8");

            Logger.t(getClass().getSimpleName()).xml(entityContent);
        } else if (FORMAT_JSON.equals(mRequestContentFormat)) {
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            Logger.t(getClass().getSimpleName()).json(entityContent);
        }
        Logger.t(getClass().getSimpleName()).object(conn.getRequestProperties());


        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(entityContent); //Writes out the string to the underlying output stream as a sequence of bytes
        out.flush(); // Flushes the data output stream.
        out.close(); // Closing the output stream.

        return IOUtils.toString(new InputStreamReader(conn.getInputStream(), CharEncoding.UTF_8));
    }

    /**
     * Sets the value of the specified request header field.
     *
     * @param conn
     */
    protected void setRequestProperty(HttpURLConnection conn) {

    }

    // =========================================================================================


    // ============================================================================

    /**
     * 初始化所有接口都有的三个通用字段
     * 注意：因为要使用到Context,所以必须在mContext参数赋值后调用,目前mContext是在Request中添加到Service时赋值,
     * 在excute()方法执行中都可使用Context,现在buildPostEntity()方法中调用,子类如果覆盖buildPostEntity()方法,需要注意在构造json串前条用次方法赋值
     *
     * @param context
     */
    protected abstract void buildCommQueryParameter(Context context);

    /**
     * 获取所有的Key-Value形式的请求参数集合。其中： Key: 请求参数名 Value: 请求参数值。GET 请求方式需覆盖此方法，获取请求参数
     *
     * @return 请求参数集合
     */
    private Map<? extends String, ?> buildQueryParameter() {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) || !Modifier.isPublic(modifiers)) {
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
        buildCommQueryParameter(mContext);
        //mHttpClientUrl.build(buildQueryParameter());
    }

    /**
     * 获取String类型的 entity 请求参数。POST 请求方式 如不是 json 格式 需覆盖此方法，获取请求参数
     *
     * @return
     */
    protected String buildPostEntity() {
        buildCommQueryParameter(mContext);

        String postEntity = "";
        if (FORMAT_JSON.equals(mRequestContentFormat)) {
            postEntity = GsonFactory.buildOnlyIncludePublicFieldsGson().toJson(this);
        }

        if (AppConfig.isRequestEncrypt()) {
            postEntity = CryptoUtils.encrypt(postEntity, "");
        }
        return postEntity;
    }


    // ============================================================================

    // ============================================================================

    /**
     * 带请求参数的url
     *
     * @return
     */
    public String getUrl() {
        return mHttpUrl.build(buildQueryParameter());
    }

    /**
     * 不带请求参数的url
     *
     * @return
     */
    public String getUrlHosts() {
        return mHttpUrl.build();
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

    public void setHttpClientUrl(HttpUrl httpUrl) {
        this.mHttpUrl = httpUrl;
    }

    /* package */void setContext(Context context) {
        this.mContext = context;
    }

    protected Context getContext() {
        return mContext;
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
}