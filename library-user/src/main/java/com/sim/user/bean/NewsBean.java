package com.sim.user.bean;

import android.util.Log;

import com.sim.user.utils.CallBack;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 收藏实体类
 */
public class NewsBean extends BmobObject {

    private static final String TAG = "【【【Sim_" + NewsBean.class.getSimpleName() + "】】】";

    private BmobUser user;//用户
    private String path;
    private String image;
    private String title;
    private String passtime;

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "user=" + user +
                ", path='" + path + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", passtime='" + passtime + '\'' +
                '}';
    }

    public NewsBean(String path, String image, String title, String passtime) {
        this.path = path;
        this.image = image;
        this.title = title;
        this.passtime = passtime;
    }

    public static class Util {
        /**
         * 获取用户收藏的所有NewsBean
         *
         * @param callBack
         */
        public static void getNewsBean(CallBack callBack) {
            BmobQuery<NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
            bmobQuery.findObjects(new FindListener<NewsBean>() {
                @Override
                public void done(List<NewsBean> list, BmobException e) {
                    if (e == null && list != null && list.size() > 0) {
                        callBack.success(list);
                    } else if (e == null && (list == null || list.size() == 0)) {
                        callBack.success();
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "获取所有收藏error：" + e.getMessage());
                    }
                }
            });
        }

        /**
         * 查询指定用户title数据
         *
         * @param title
         * @param callBack
         */
        public static void getNewsBean(String title, CallBack callBack) {
            if (!User.isLogin())
                callBack.fail("未登录");
            BmobQuery<NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
            bmobQuery.addWhereEqualTo("title", title);
            bmobQuery.findObjects(new FindListener<NewsBean>() {
                @Override
                public void done(List<NewsBean> list, BmobException e) {
                    if (e == null && list != null && list.size() > 0) {
                        callBack.success(list);
                    } else if (e == null && (list == null || list.size() == 0)) {
                        callBack.success(null);
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "查询指定用户title数据error：" + e.getMessage());
                    }
                }
            });
        }

        /**
         * 收藏NewsBean
         *
         * @param bean
         * @param callBack
         */
        public static void saveNewsBean(NewsBean bean, CallBack callBack) {
            bean.setUser(BmobUser.getCurrentUser(BmobUser.class));
            bean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        callBack.success();
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "收藏error：" + e.getMessage());
                    }
                }
            });
        }

        /**
         * 删除收藏的NewsBean
         *
         * @param title
         * @param callBack
         */
        public static void deleteNewsBean(String title, CallBack callBack) {
            if (!User.isLogin())
                callBack.fail("未登录");
            BmobQuery<NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
            bmobQuery.addWhereEqualTo("title", title);
            bmobQuery.findObjects(new FindListener<NewsBean>() {
                @Override
                public void done(List<NewsBean> list, BmobException e) {
                    if (e == null && list != null && list.size() > 0) {
                        list.get(0).delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    callBack.success();
                                } else {
                                    callBack.fail(e.getMessage());
                                    Log.e(TAG, "删除收藏error：" + e.getMessage());
                                }
                            }
                        });
                    } else if (e == null && (list == null || list.size() == 0)) {
                        callBack.success(null);
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "查询指定用户title数据error：" + e.getMessage());
                    }
                }
            });
        }
    }

}
