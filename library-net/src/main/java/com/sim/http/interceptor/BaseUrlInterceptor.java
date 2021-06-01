package com.sim.http.interceptor;

import com.sim.BaseUrl;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ author: Sim
 * @ time： 2021/5/19 9:58
 * @ description： baseUrl拦截器：处理多baseUrl
 */
public class BaseUrlInterceptor implements Interceptor {

    @Override
    public Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
        Request request = chain.request();//获取原始的originalRequest
        HttpUrl oldUrl = request.url();//获取老的url
        Request.Builder builder = request.newBuilder();//获取originalRequest的创建者builder

        List<String> urlnameList = request.headers("host");//获取头信息的集合如：bus,route

        if (urlnameList.size() > 0) {
            builder.removeHeader("host"); //删除原有配置中的值,就是namesAndValues集合里的值

            String urlname = urlnameList.get(0);//获取头信息中配置的value,如：bus或者route

            HttpUrl baseURL = null;
            if ("wangyi".equals(urlname)) {
                baseURL = HttpUrl.parse(BaseUrl.base_wangyi);
            } else if ("bus".equals(urlname)) {//根据头信息中配置的value,来匹配新的base_url地址
                baseURL = HttpUrl.parse(BaseUrl.base_bus);
            } else if ("route".equals(urlname)) {
                baseURL = HttpUrl.parse(BaseUrl.base_route);
            } else if ("wanandroid".equals(urlname)) {
                baseURL = HttpUrl.parse(BaseUrl.base_wanandroid);
            }

            HttpUrl newHttpUrl = oldUrl.newBuilder()//重建新的HttpUrl，需要重新设置的url部分
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
