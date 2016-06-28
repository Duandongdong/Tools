package com.bestgood.commons.network.http;

import com.bestgood.commons.util.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author wlf(Andy)
 * @datetime 2016-02-19 14:56 GMT+8
 * @email 411086563@qq.com
 */
public class HttpsUtils {

    public static SSLSocketFactory getSslSocketFactory(InputStream certificates) {
        SSLContext sslContext = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            Certificate ca;
            try {
                ca = certificateFactory.generateCertificate(certificates);
            } finally {
                certificates.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext != null ? sslContext.getSocketFactory() : null;
    }

    public static HostnameVerifier getHostnameVerifier() {
        // Create an HostnameVerifier that hardwires the expected hostname.
        // Note that is different than the URL's hostname:
        // example.com versus example.org
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //Logger.e(hostname);
                //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                //return hv.verify("example.com", session);
                if ("192.168.0.115".equals(hostname) || "114.215.99.131".equals(hostname)) {
                    return true;
                }
                return true;
            }
        };
    }

    /**
     * 获取双向认证的SSLSocketFactory
     *
     * @param needTrustServerCers 需要信任的所有服务器端公钥cer文件
     * @param clientBks           客户端bks密钥库
     * @param clientBksPassword   客户端bks密钥库的密码
     * @return 双向认证的SSLSocketFactory
     */
    public static SSLSocketFactory getSslSocketFactory(InputStream[] needTrustServerCers, InputStream clientBks, String clientBksPassword) {
        try {
            // 信任服务器端公钥管理器列表
            TrustManager[] trustManagers = prepareTrustManager(needTrustServerCers);
            // 当前客户端bks密钥库的所有证书管理器
            KeyManager[] keyManagers = prepareKeyManager(clientBks, clientBksPassword);
            // SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // 初始化
            sslContext.init(keyManagers, new TrustManager[]{new DefaultTrustManager(chooseX509TrustManager(trustManagers))}, new SecureRandom());
            // 得到SSLSocketFactory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取单向认证的SSLSocketFactory
     *
     * @param needTrustServerCers 需要信任的所有服务器端公钥cer文件
     * @return 单向认证的SSLSocketFactory
     */
    public static SSLSocketFactory getSslSocketFactory(InputStream[] needTrustServerCers) {
        return getSslSocketFactory(needTrustServerCers, null, null);
    }

    private static TrustManager[] prepareTrustManager(InputStream[] needTrustServerCers) {

        if (needTrustServerCers == null || needTrustServerCers.length <= 0) {
            return null;
        }

        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            int index = 0;
            for (InputStream certificate : needTrustServerCers) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TrustManagerFactory trustManagerFactory = null;
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {

        if (bksFile == null || password == null) {
            return null;
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(bksFile, password.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static X509TrustManager chooseX509TrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static class DefaultTrustManager implements X509TrustManager {

        private X509TrustManager allX509TrustManager;
        private X509TrustManager localTrustManager;

        public DefaultTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            allX509TrustManager = chooseX509TrustManager(trustManagerFactory.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                allX509TrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                e.printStackTrace();
                try {
                    localTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
