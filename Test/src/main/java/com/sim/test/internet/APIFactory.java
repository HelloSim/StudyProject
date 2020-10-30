package com.sim.test.internet;

import com.sim.test.bean.SmzdmDataBean;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Grugsum on 2019/4/22.
 * 单例
 */

public class APIFactory extends RetrofitUtil {

    private APIFactory() {
    }

    public static APIFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final APIFactory INSTANCE = new APIFactory();
    }

    /**
     * 获取网络请求bean类
     */
    /*public <T> void getHttpBean(final Subscriber subscriber, final String tbInterfaceCode, final String tbBiz , final Class<T> beanClass) {
        Observable observable = apiService.getToken()
                .map(new TokenFunc())
                .flatMap(new Func1<String, Observable<HttpResult>>() {
                    @Override
                    public Observable<HttpResult> call(String s) {
                        return apiService.getHttpResult(tbInterfaceCode, s, tbBiz);
                    }
                })
                .map(new HttpResultFunc())
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        Object o;
                        try {
                            o =  JsonUtil.jsonToObject(s, beanClass);
                        }catch (Exception e){
                            throw new  APIException("40002", "数据异常");
                        }
                        return o;
                    }
                });
        toSubscribe(observable, subscriber);
    }*/

    /**
     * Home的Data的网络请求
     */
    /*public void getHomeData(Subscriber <HomeBean> subscriber, String tab_id, String device_id, String smzdm_id, String page, String limit, String time_sort, String update_timestamp, String past_num, String is_show_guide, String widget_id, String ad_info, String recfeed_switch, String f, String v, String weixin, String time, String sign) {
    Observable observable = apiService.getHomeData( tab_id, device_id, smzdm_id, page, limit, time_sort, update_timestamp, past_num, is_show_guide, widget_id, ad_info, recfeed_switch, f, v, weixin, time, sign );
    toSubscribe( observable, subscriber );
    }*/
    public void getHome(Subscriber<SmzdmDataBean> subscriber, String page, String limit, String time) {
        Observable observable = apiService.getHome(page, limit, time);
        toSubscribe(observable, subscriber);
    }

}
