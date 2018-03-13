package com.gac.easyokhttp.callback;

import com.gac.easyokhttp.EasyOkHttp;
import com.gac.easyokhttp.response.IResponseHandler;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gacmy on 2018/3/12.
 */

public  class EasyCallback implements Callback {
    private IResponseHandler mResponseHandler;
    public EasyCallback(IResponseHandler handler){
        mResponseHandler = handler;
    }


    @Override
    public void onFailure(Call call,final IOException e) {
        EasyOkHttp.mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailure(0,e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call,final Response response) throws IOException {
        if(response.isSuccessful()) {
            mResponseHandler.onSuccess(response);
        } else {
            //LogUtils.e("onResponse fail status=" + response.code());

            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(response.code(), "fail status=" + response.code());
                }
            });
        }
    }
}
