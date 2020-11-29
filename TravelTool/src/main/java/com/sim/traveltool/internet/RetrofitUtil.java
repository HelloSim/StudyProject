package com.sim.traveltool.internet;

import android.content.Context;
import android.util.Log;

import com.sim.baselibrary.bean.HttpResult;
import com.sim.baselibrary.internet.APIException;
import com.sim.baselibrary.internet.AppUtil;
import com.sim.baselibrary.internet.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
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
 * @Auther Sim
 * @Time 2019/4/22 1:05
 * @Description 初始化OKHttpClient、Retrofit，切换线程
 */
public class RetrofitUtil {
    private static final String TAG = "Sim_RetrofitUtil";

    /**
     * 服务器地址
     */
    private static final String USER_BASE_URL = "https://api.apiopen.top";
    private static final String BUS_BASE_URL = "http://www.zhbuswx.com";
    private static final String ROUTE_BASE_URL = "http://restapi.amap.com";

    private UserAPIService userAPIService;
    private BusAPIService busAPIService;
    private RouteAPIService routeAPIService;

    private static Retrofit userRetrofit = null;
    private static Retrofit busRetrofit = null;
    private static Retrofit routeRetrofit = null;

    private static OkHttpClient okHttpClient = null;

    private Context mContext;

    private boolean isUseCache;
    private int maxCacheTime = 60;

    public void setMaxCacheTime(int maxCacheTime) {
        this.maxCacheTime = maxCacheTime;
    }

    public void setUseCache(boolean useCache) {
        this.isUseCache = useCache;
    }

    public UserAPIService getUserApiService() {
        if (userAPIService == null && userRetrofit != null) {
            userAPIService = userRetrofit.create(UserAPIService.class);
        }
        return userAPIService;
    }

    public BusAPIService getBusAPIService() {
        if (busAPIService == null && busRetrofit != null) {
            busAPIService = busRetrofit.create(BusAPIService.class);
        }
        return busAPIService;
    }

    public RouteAPIService getRouteAPIService() {
        if (routeAPIService == null && routeRetrofit != null) {
            routeAPIService = routeRetrofit.create(RouteAPIService.class);
        }
        return routeAPIService;
    }

    public void init(Context context) {
        this.mContext = context;
        initOKHttp();
        initRetrofit();
        getUserApiService();
        getBusAPIService();
        getRouteAPIService();
    }

    /**
     * 初始化OKHttpClient
     */
    private void initOKHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(AppUtil.getCacheDir(mContext), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppUtil.isNetworkConnected(mContext) || isUseCache) {//如果网络不可用或者设置只用网络
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                    Log.d(TAG, "网络不可用请求拦截");
                } else if (AppUtil.isNetworkConnected(mContext) && !isUseCache) {//网络可用
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
                    Log.d(TAG, "网络可用请求拦截");
                }
                Response response = chain.proceed(request);
                if (AppUtil.isNetworkConnected(mContext)) {//如果网络可用
                    Log.d(TAG, "网络可用响应拦截");
                    response = response.newBuilder()
                            //覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
                            .header("Cache-Control", "public,max-age=" + maxCacheTime)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        // 设置缓存
        builder.cache(cache);
        builder.interceptors().add(cacheInterceptor);
        builder.networkInterceptors().add(cacheInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        //忽略证书
        builder.sslSocketFactory(RxUtils.createSSLSocketFactory());
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .build();
                return chain.proceed(request);
            }
        });
        okHttpClient = builder.build();
    }

    /**
     * 初始化Retrofit
     */
    private void initRetrofit() {
        userRetrofit = new Retrofit.Builder()
                .baseUrl(USER_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        busRetrofit = new Retrofit.Builder()
                .baseUrl(BUS_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        routeRetrofit = new Retrofit.Builder()
                .baseUrl(ROUTE_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
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
