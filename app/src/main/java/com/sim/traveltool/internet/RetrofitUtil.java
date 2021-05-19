package com.sim.traveltool.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.sim.baselibrary.bean.HttpResult;
import com.sim.baselibrary.internet.APIException;
import com.sim.baselibrary.internet.RxUtils;
import com.sim.traveltool.internet.interceptor.BaseUrlInterceptor;
import com.sim.traveltool.internet.interceptor.MyCacheInterceptor;

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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Sim --- 初始化OKHttpClient、Retrofit，切换线程
 */
public class RetrofitUtil {

    private Context mContext;

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
        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 设置缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(getCacheDir(mContext), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时
                .readTimeout(20, TimeUnit.SECONDS)//读取超时
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时
                .retryOnConnectionFailure(true)//错误重连
                .sslSocketFactory(RxUtils.createSSLSocketFactory())//绕过证书验证
                .cache(cache)//缓存
                .addInterceptor(new MyCacheInterceptor(mContext))//缓存拦截器
                .addInterceptor(new BaseUrlInterceptor())//baseUrl拦截器
//                .addInterceptor(new HeaderInterceptor())//头部拦截器
//                .addInterceptor(new QueryParameterInterceptor())//公共参数拦截器
                .addInterceptor(loggingInterceptor);//设置 Debug Log 模式
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
