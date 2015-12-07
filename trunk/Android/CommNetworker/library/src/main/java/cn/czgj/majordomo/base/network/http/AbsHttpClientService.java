
package cn.czgj.majordomo.base.network.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.IBinder;

import com.baigu.util.log.Logger;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.XmlObjectParser;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * @author ddc
 * @date Apr 20, 2014 7:43:29 PM
 */
public abstract class AbsHttpClientService extends SpiceService {

    // private final HttpTransport HTTP_TRANSPORT = newCompatibleTransport();

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v("onCreate()");
    }

    @Override
    public void addRequest(CachedSpiceRequest<?> request, Set<RequestListener<?>> listRequestListener) {
        SpiceRequest<?> spiceRequest = request.getSpiceRequest();
        if (spiceRequest instanceof HttpClientRequest) {
            HttpClientRequest<?> req = (HttpClientRequest<?>) spiceRequest;
            req.setContext(this);
            req.setHttpRequestFactory(createRequestFactory(req));
        }
        super.addRequest(request, listRequestListener);
    }

    private HttpRequestFactory createRequestFactory(HttpClientRequest<?> request) {
        return newCompatibleTransport().createRequestFactory(
                new Initializer(request.isParser(),
                        request.getResponseContentFormat(),
                        request.getXmlNamespaceDictionaryMap()));
    }

    private class Initializer implements HttpRequestInitializer,
            HttpUnsuccessfulResponseHandler {
        boolean isParser;
        String format;
        Map<String, String> namespace;

        public Initializer(boolean isParser, String format, Map<String, String> namespace) {
            this.isParser = isParser;
            this.format = format;
            this.namespace = namespace;
        }

        @Override
        public boolean handleResponse(HttpRequest request, HttpResponse response, boolean retrySupported) throws IOException {
            Logger.v("Initializer handleResponse: %s", response.getStatusCode() + " " + response.getStatusMessage());
            return false;
        }

        @Override
        public void initialize(HttpRequest request) throws IOException {
            request.setUnsuccessfulResponseHandler(this);
            if (!isParser) {
                return;
            }
            if (HttpClientRequest.FORMAT_XML.equals(format)) {
                request.setParser(buildXmlObjectParser(namespace));
            } else if (HttpClientRequest.FORMAT_JSON.equals(format)) {
                request.setParser(buildJsonObjectParser());
            } else {
                // TODO...解析其他文本格式
            }
        }

        private JsonObjectParser buildJsonObjectParser() {
            JsonFactory JSON_FACTORY = new JacksonFactory();
            return new JsonObjectParser(JSON_FACTORY);
        }

        private XmlObjectParser buildXmlObjectParser(Map<String, String> namespace) {
            XmlNamespaceDictionary xmlNamespaceDictionary = new XmlNamespaceDictionary();
            if (namespace != null && namespace.size() > 0) {
                for (String alias : namespace.keySet()) {
                    xmlNamespaceDictionary.set(alias, namespace.get(alias));
                }
            }
            return new XmlObjectParser(xmlNamespaceDictionary);
        }
    }

    // =============================================================================================================

    @Override
    public int getThreadCount() {
        return 3;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.t(getClass().getSimpleName()).d(intent, "onStart(intent,%d)", startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.t(getClass().getSimpleName()).d(intent, "onStartCommand(intent,%d,%d", flags, startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.t(getClass().getSimpleName()).v("onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.t(getClass().getSimpleName()).d(intent, "onBind(intent)");
        return super.onBind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Logger.t(getClass().getSimpleName()).d(intent, "onRebind(intent)");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.t(getClass().getSimpleName()).d(intent, "onUnbind(intent)");
        return super.onUnbind(intent);
    }

    // 生成HttpTransport=============================================================================================================
    private static SSLSocketFactory sAllHostsValidSocketFactory;
    private static HostnameVerifier sAllHostsValidVerifier;
    private static DefaultHttpClient sAllHostsValidApatchHttpClient;

    /**
     * AndroidHttp.newCompatibleTransport() 构造不受https证书访问限制的HttpTransport
     *
     * @return
     */
    private HttpTransport newCompatibleTransport() {
        return new ApacheHttpTransport();
        // return AndroidHttp.newCompatibleTransport();
        // return AndroidUtils.isMinimumSdkLevel(9) ? (new
        // NetHttpTransport.Builder())
        // .setSslSocketFactory(getAllHostsValidSocketFactory())
        // .setHostnameVerifier(getAllHostsValidVerifier()).build()
        // : new ApacheHttpTransport(getAllHostsValidApatchHttpClient());
    }

    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory getAllHostsValidSocketFactory() {
        if (sAllHostsValidSocketFactory == null) {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidSocketFactory()");
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidSocketFactory()");
            }
            sAllHostsValidSocketFactory = sslContext == null ? null : sslContext.getSocketFactory();
        }
        return sAllHostsValidSocketFactory;
    }

    private static HostnameVerifier getAllHostsValidVerifier() {
        if (sAllHostsValidVerifier == null) {
            sAllHostsValidVerifier = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }
        return sAllHostsValidVerifier;
    }

    // --------------------------------------------------------------------------------------------------------------------

    /**
     * 获取允许所有证书验证的 HttpClient
     *
     * @return
     */
    private static DefaultHttpClient getAllHostsValidApatchHttpClient() {
        if (sAllHostsValidApatchHttpClient == null) {
            // 初始化工作
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                org.apache.http.conn.ssl.SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

                HttpParams params = new BasicHttpParams();

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
                HttpProtocolParams.setUseExpectContinue(params, true);

                // 设置连接管理器的超时
                ConnManagerParams.setTimeout(params, 10 * 1000);
                // 设置连接超时
                HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
                // 设置socket超时
                HttpConnectionParams.setSoTimeout(params, 10 * 1000);

                // 设置http https支持
                SchemeRegistry schReg = new SchemeRegistry();
                schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schReg.register(new Scheme("https", sf, 443));

                ClientConnectionManager conManager = new ThreadSafeClientConnManager(params, schReg);

                sAllHostsValidApatchHttpClient = new DefaultHttpClient(conManager, params);
            } catch (KeyStoreException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidApatchHttpClient()");
                return new DefaultHttpClient();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidApatchHttpClient()");
                return new DefaultHttpClient();
            } catch (CertificateException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidApatchHttpClient()");
                return new DefaultHttpClient();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidApatchHttpClient()");
                return new DefaultHttpClient();
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidApatchHttpClient()");
                return new DefaultHttpClient();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
                Logger.e(e, "getAllHostsValidApatchHttpClient()");
                return new DefaultHttpClient();
            }
        }
        return sAllHostsValidApatchHttpClient;
    }
}

class SSLSocketFactoryEx extends org.apache.http.conn.ssl.SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public SSLSocketFactoryEx(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        TrustManager tm = new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }
        };
        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
    // end=============================================================================================================
}
