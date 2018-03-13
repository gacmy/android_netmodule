package com.gac.easyokhttp.response;

import android.util.Log;

import com.gac.easyokhttp.EasyOkHttp;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by gacmy on 2018/3/12.
 */

public abstract class CommonResponseHandler implements IResponseHandler {

    public CommonResponseHandler(){

    }


    @Override
    public void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "";
        try {
            responseBodyStr = responseBody.string();
            Log.e("httputils","response:"+responseBodyStr);
        } catch (IOException e) {
            e.printStackTrace();
            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                   onFailure(response.code(),"failed read response body");
                }
            });
            return;
        }finally {
            try {
                responseBody.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String finalResponseBodyStr = responseBodyStr;

        try {

            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(response.code(), finalResponseBodyStr);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.e("onResponse fail parse gson, body=" + finalResponseBodyStr);
            EasyOkHttp.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail parse gson, body=" + finalResponseBodyStr);
                }
            });

        }

    }
    public abstract void onSuccess(int statusCode, String response);
}


























