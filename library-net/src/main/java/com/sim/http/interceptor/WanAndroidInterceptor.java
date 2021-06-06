package com.sim.http.interceptor;

import com.sim.http.BaseUrl;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class WanAndroidInterceptor implements Interceptor {

    private String id = "408";
    private String page = "1";

    public WanAndroidInterceptor(String id, String page) {
        this.id = id;
        this.page = page;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();//获取原始的originalRequest
        HttpUrl oldUrl = request.url();//获取老的url
        Request.Builder builder = request.newBuilder();//获取originalRequest的创建者builder

        HttpUrl baseURL = null;
        if (oldUrl.toString().contains(BaseUrl.base_wanandroid.getPublicArticle)) {
            baseURL = HttpUrl.parse(BaseUrl.base_wanandroid.base + "/wxarticle/list/" + id + "/" + page + "/json");
            HttpUrl newHttpUrl = baseURL.newBuilder()//重建新的HttpUrl，需要重新设置的url部分
                    .scheme(baseURL.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build();
            Request newRequest = builder.url(newHttpUrl).build();//获取处理后的新newRequest
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(request);
        }
    }

}
