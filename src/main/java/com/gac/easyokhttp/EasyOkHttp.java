package com.gac.easyokhttp;

import android.os.Handler;
import android.os.Looper;


import com.gac.easyokhttp.cookie.CookiesManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;


import okhttp3.Dispatcher;

import okhttp3.OkHttpClient;


/**
 * Created by gacmy on 2018/3/12.
 */

public class EasyOkHttp {
    private static OkHttpClient mClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    private static boolean isSupportHttps = false;//是否支持https

    public OkHttpClient getOkHttpClient(){
        return mClient;
    }
    public EasyOkHttp(){
        this(null,false,null);
    }
    public EasyOkHttp(InputStream certicicates){
        this(null,false,certicicates);
    }
    public EasyOkHttp(boolean isSupportCookie){
        this(null,isSupportCookie,null);
    }
    public EasyOkHttp(OkHttpClient client,boolean isSupportCookie,InputStream certificates){
        if(mClient == null){
            synchronized (EasyOkHttp.class){
                if(client == null){
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    if(isSupportCookie){
                        builder.cookieJar(new CookiesManager());
                    }
                    if(isSupportHttps){
                        if (certificates != null){
                            builder.sslSocketFactory(getSSLSocketFactory(certificates));
                        }
                    }
                    mClient = builder.build();
                }else{
                    mClient = client;
                }
            }
        }
    }
    /**
     * 获取SSLSocketFactory
     *
     * @param certificates 证书流文件
     * @return
     */
    private static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * do cacel by tag
     * @param tag tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = mClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
