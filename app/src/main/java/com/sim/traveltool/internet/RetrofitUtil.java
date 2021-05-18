package com.sim.traveltool.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.sim.baselibrary.bean.HttpResult;
import com.sim.baselibrary.internet.APIException;
import com.sim.baselibrary.internet.RxUtils;
import com.sim.baselibrary.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Sim --- 初始化OKHttpClient、Retrofit，切换线程
 */
public class RetrofitUtil {

    private Context mContext;

    private boolean isUseCache = true;//是否使用缓存

    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private ApiService apiService;

    public void init(Context context) {
        this.mContext = context;
        initOKHttp();
        initRetrofit();
        getApiService();
    }

    /**
     * 初始化OKHttpClient
     */
    private void initOKHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //忽略证书
        builder.sslSocketFactory(RxUtils.createSSLSocketFactory());

        // 设置缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(getCacheDir(mContext), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        builder.cache(cache);
        //缓存拦截器
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkConnected(mContext) || !isUseCache) {//如果网络不可用或者设置使用缓存
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    LogUtil.d(getClass(), "网络不可用请求拦截");
                } else if (isNetworkConnected(mContext) && isUseCache) {//网络可用 且设置不用缓存
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
                }
                Response response = chain.proceed(request);
                if (isNetworkConnected(mContext)) {//如果网络可用
                    response = response.newBuilder()
                            .build();
                }
                return response;
            }
        };
        builder.interceptors().add(cacheInterceptor);
        builder.networkInterceptors().add(cacheInterceptor);

        //baseUrl拦截器
        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();//获取原始的originalRequest
                HttpUrl oldUrl = originalRequest.url();//获取老的url
                Request.Builder builder = originalRequest.newBuilder();//获取originalRequest的创建者builder
                List<String> urlnameList = originalRequest.headers("urlname");//获取头信息的集合如：bus,route
                if (urlnameList.size() > 0) {
                    builder.removeHeader("urlname"); //删除原有配置中的值,就是namesAndValues集合里的值
                    String urlname = urlnameList.get(0);//获取头信息中配置的value,如：bus或者route
                    HttpUrl baseURL = null;
                    if ("bus".equals(urlname)) {//根据头信息中配置的value,来匹配新的base_url地址
                        baseURL = HttpUrl.parse(API.bus_base_url);
                    } else if ("route".equals(urlname)) {
                        baseURL = HttpUrl.parse(API.route_base_url);
                    } else if ("wangyi".equals(urlname)) {
                        baseURL = HttpUrl.parse(API.wangyi_base_url);
                    }
                    HttpUrl newHttpUrl = oldUrl.newBuilder()//重建新的HttpUrl，需要重新设置的url部分
                            .scheme(baseURL.scheme())//http协议如：http或者https
                            .host(baseURL.host())//主机地址
                            .port(baseURL.port())//端口
                            .build();
                    Request newRequest = builder.url(newHttpUrl).build();//获取处理后的新newRequest
                    return chain.proceed(newRequest);
                } else {
                    return chain.proceed(originalRequest);
                }
            }
        });

        okHttpClient = builder.build();
    }

    /**
     * 初始化Retrofit
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API.wangyi_base_url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public ApiService getApiService() {
        if (apiService == null && retrofit != null) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    /**
     * 只关注是否联网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
     */
    public static String getCacheDir(Context context) {
        String cacheDir;
        if (context.getExternalCacheDir() != null && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = context.getExternalCacheDir().toString();
        } else {
            cacheDir = context.getCacheDir().toString();
        }
        return cacheDir;
    }

    /**
     * 线程切换
     */
    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new APIException(httpResult.error_code, httpResult.error_msg);
            }
            return httpResult.content;
        }
    }

}
