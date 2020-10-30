package com.sim.test.internet;

import android.content.Context;

import com.sim.test.utils.AppUtils;
import com.sim.test.utils.RxUtils;

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
 * Created by Grugsum on 2019/4/22.
 * 初始化OKHttpClient、Retrofit，切换线程
 */

public class RetrofitUtil {

    /**
     * 服务器地址
     */
    private static final String BASE_URL = "https://homepage-api.smzdm.com";

    public APIService apiService;
    private static Retrofit retrofit = null;
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

    public APIService getApiService() {
        if (apiService == null && retrofit != null) {
            apiService = retrofit.create( APIService.class );
        }
        return apiService;
    }
    public void init(Context context) {
        this.mContext = context;
        initOKHttp();
        initRetrofit();
        if (apiService == null && retrofit != null) {
            apiService = retrofit.create( APIService.class );
        }
    }


    /**
     * 初始化OKHttpClient
     */
    private void initOKHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File( AppUtils.getCacheDir( mContext ), "httpCache" );
        Cache cache = new Cache( cacheFile, 1024 * 1024 * 50 );
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppUtils.isNetworkConnected( mContext ) || isUseCache) {//如果网络不可用或者设置只用网络
                    request = request.newBuilder().cacheControl( CacheControl.FORCE_CACHE ).build();
//                    Log.d("OkHttp", "网络不可用请求拦截");
                } else if (AppUtils.isNetworkConnected( mContext ) && !isUseCache) {//网络可用
                    request = request.newBuilder().cacheControl( CacheControl.FORCE_NETWORK ).build();
//                    Log.d("OkHttp", "网络可用请求拦截");
                }
                Response response = chain.proceed( request );
                if (AppUtils.isNetworkConnected( mContext )) {//如果网络可用
//                    Log.d("OkHttp", "网络可用响应拦截");
                    response = response.newBuilder()
                            //覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
                            .header( "Cache-Control", "public,max-age=" + maxCacheTime ).removeHeader( "Pragma" ).build();
                }
                return response;
            }
        };
        // 设置缓存
        builder.cache( cache );
        builder.interceptors().add( cacheInterceptor );
        builder.networkInterceptors().add( cacheInterceptor );
        //设置超时
        builder.connectTimeout( 15, TimeUnit.SECONDS );
        //忽略证书
        builder.sslSocketFactory( RxUtils.createSSLSocketFactory() );
        builder.readTimeout( 20, TimeUnit.SECONDS );
        builder.writeTimeout( 20, TimeUnit.SECONDS );
        //错误重连
        builder.retryOnConnectionFailure( true );
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        //移除旧的
                        .removeHeader("User-Agent")
                        //添加User-Agent
                        .addHeader("User-Agent", "smzdm_android_V9.4.1 rv:531 (M5 Note;Android7.0;zh)smzdmapp")
                        .addHeader("Cookie", "smzdm_version=9.4.1;device_type=MeizuM5+Note;client_id=c28544ee8b1aadd14917d4bb259a5800.1556553428584;rs_id2=;rs_id4=;imei=;login=0;smzdm_id=;session_id=c28544ee8b1aadd14917d4bb259a5800.1556553429515;partner_name=meizu;android_id=d405ef5f6cf955607bd83d8e513ef081;partner_id=14;rs_id1=;rs_id3=;pid=9Jd9raKWVDNqWUZR8JCwticG2QBPx7NU8MzInof1iXhzbfQDpoHn1A%3D%3D;smzdm_user_source=d405ef5f6cf955607bd83d8e513ef081;new_device_id=d405ef5f6cf955607bd83d8e513ef081;device_smzdm_version_code=531;mac=A4%3A44%3AD1%3AF3%3A57%3A2A;rs_id5=;network=1;device_system_version=7.0;device_id=c28544ee8b1aadd14917d4bb259a5800;device_push=0;sessionID=c28544ee8b1aadd14917d4bb259a5800.1556553429515;device_smzdm=android;device_s=d405ef5f6cf955607bd83d8e513ef081;device_smzdm_version=9.4.1;")
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
        retrofit = new Retrofit.Builder().baseUrl( BASE_URL ).client( okHttpClient ).addConverterFactory( GsonConverterFactory.create() ).addCallAdapterFactory( RxJavaCallAdapterFactory.create() ).build();

    }

    /**
     * 线程切换
     */
    public <T> void toSubscribe(Observable <T> o, Subscriber <T> s) {
        o.subscribeOn( Schedulers.io() ).unsubscribeOn( Schedulers.io() ).observeOn( AndroidSchedulers.mainThread() ).subscribe( s );
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1 <HttpResult <T>, T> {

        @Override
        public T call(HttpResult <T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new APIException( httpResult.error_code, httpResult.error_msg );
            }
            return httpResult.content;
        }
    }

}
