package com.sim.http.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Sim --- 公共参数拦截器：添加公共参数
 */
public class QueryParameterInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl httpUrl = request.url();//获取url
        if (httpUrl.toString().contains("https://api.apiopen.top")) {
            Request newRequest;
            HttpUrl modifiedUrl = request.url().newBuilder()
                    .addQueryParameter("count", "20")
                    .build();
            newRequest = request.newBuilder().url(modifiedUrl).build();
            return chain.proceed(newRequest);
        } else if (httpUrl.toString().contains("http://www.zhbuswx.com")){
            Request newRequest;
            HttpUrl modifiedUrl = request.url().newBuilder()
                    .addQueryParameter("_", String.valueOf(System.currentTimeMillis()))
                    .build();
            newRequest = request.newBuilder().url(modifiedUrl).build();
            return chain.proceed(newRequest);
        } else if (httpUrl.toString().contains("http://restapi.amap.com")) {
            Request newRequest;
            HttpUrl modifiedUrl = request.url().newBuilder()
                    .addQueryParameter("s", "rsv3")
                    .addQueryParameter("key", "ceb54024fae4694f734b1006e8dc8324")
                    .addQueryParameter("city", "珠海")
                    .addQueryParameter("callback", "")
                    .addQueryParameter("platform", "JS")
                    .addQueryParameter("logversion", "2.0")
                    .addQueryParameter("sdkversion", "1.3")
                    .addQueryParameter("appname", "http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/")
                    .addQueryParameter("csid", "759CACE2-2197-4E0A-ADCB-1456B16775DA")
                    .build();
            newRequest = request.newBuilder().url(modifiedUrl).build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(request);
        }
    }

}
