package com.sim.traveltool.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ author: Sim
 * @ time： 2021/5/19 10:06
 * @ description：头部拦截器
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //添加头部信息
        Request.Builder requestBuilder = request.newBuilder()
//                .header("AppType", "TPOS")
//                .header("Content-Type", "application/json")
//                .header("Accept", "application/json")
                .method(request.method(), request.body());
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

}
