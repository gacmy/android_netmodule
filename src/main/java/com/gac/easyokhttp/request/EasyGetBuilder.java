package com.gac.easyokhttp.request;

import android.util.Log;

import com.gac.easyokhttp.EasyOkHttp;
import com.gac.easyokhttp.callback.EasyCallback;
import com.gac.easyokhttp.response.IResponseHandler;


import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2018/3/13.
 */

public class EasyGetBuilder extends EasyRequestBuilder<EasyGetBuilder> {
    public EasyGetBuilder(EasyOkHttp client) {
        super(client);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try{
            if(mUrl == null || mUrl.length() == 0){
                throw new IllegalArgumentException("url can not be null");
            }
            if(mParams != null && mParams.size() > 0){
                mUrl = appendParams(mUrl,mParams);
            }
            Log.e("http","url:"+mUrl);
            Request.Builder builder = new Request.Builder().url(mUrl).get();
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }
            Request request = builder.build();

            mClient.getOkHttpClient().
                    newCall(request).
                    enqueue(new EasyCallback(responseHandler));
        }catch (Exception e){
            //LogUtils.e("Get enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params to url
    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
