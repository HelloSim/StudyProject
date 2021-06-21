package com.sim.util;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sim.http.ApiService;
import com.sim.adapter.DoubleDefault0Adapter;
import com.sim.adapter.IntegerDefault0Adapter;
import com.sim.adapter.LongDefault0Adapter;
import com.sim.adapter.StringDefault0Adapter;
import com.sim.interceptor.BaseUrlInterceptor;
import com.sim.interceptor.HeaderInterceptor;
import com.sim.interceptor.MyCacheInterceptor;
import com.sim.interceptor.QueryParameterInterceptor;
import com.sim.interceptor.WanAndroidInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Sim --- 初始化OKHttpClient、Retrofit，切换线程
 */
public class RetrofitUtil {

    private Context mContext;

    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private ApiService apiService;

    private WanAndroidInterceptor wanAndroidInterceptor = new WanAndroidInterceptor("408", "1");

    public void init(Context context) {
        this.mContext = context;
        initOKHttp();
        initRetrofit();
        getApiService();
    }

    public void setWanAndroidInterceptorParameter(String id, String page) {
        wanAndroidInterceptor.setId(id);
        wanAndroidInterceptor.setPage(page);
    }

    /**
     * 初始化OKHttpClient
     */
    private void initOKHttp() {
        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 设置缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(getCacheDir(mContext), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        // addInterceptor() 添加应用拦截器
        // ● 不需要担心中间过程的响应,如重定向和重试.
        // ● 总是只调用一次,即使HTTP响应是从缓存中获取.
        // ● 观察应用程序的初衷. 不关心OkHttp注入的头信息如: If-None-Match.
        // ● 允许短路而不调用 Chain.proceed(),即中止调用.
        // ● 允许重试,使 Chain.proceed()调用多次.
        // addNetworkInterceptor() 添加网络拦截器
        // ● 能够操作中间过程的响应,如重定向和重试.
        // ● 当网络短路而返回缓存响应时不被调用.
        // ● 只观察在网络上传输的数据.
        // ● 携带请求来访问连接.
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时
                .readTimeout(20, TimeUnit.SECONDS)//读取超时
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时
                .retryOnConnectionFailure(true)//错误重连
                .sslSocketFactory(SSLUtils.createSSLSocketFactory())//绕过证书验证
                .cache(cache)//缓存
                //缓存拦截器
                .addInterceptor(new MyCacheInterceptor(mContext))//无网络时
                .addNetworkInterceptor(new MyCacheInterceptor(mContext))//有网络时
                .addInterceptor(new BaseUrlInterceptor())//baseUrl拦截器
                .addInterceptor(new HeaderInterceptor())//头部拦截器
                .addInterceptor(new QueryParameterInterceptor())//公共参数拦截器
                .addInterceptor(wanAndroidInterceptor)
                .addInterceptor(loggingInterceptor);//设置 Debug Log 模式
        okHttpClient = builder.build();
    }

    /**
     * 初始化Retrofit
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.apiopen.top")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 增加后台返回""和"null"的处理
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     *
     * @return
     */
    static Gson gson = null;

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(String.class,new StringDefault0Adapter())
                    .create();
        }
        return gson;
    }

    public ApiService getApiService() {
        if (apiService == null && retrofit != null) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
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
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    /*public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new APIException(httpResult.error_code, httpResult.error_msg);
            }
            return httpResult.content;
        }
    }*/

}
