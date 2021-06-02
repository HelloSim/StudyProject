package com.sim.user.bean;

import com.sim.user.utils.SuccessOrFailListener;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim --- 收藏实体类
 */
public class NewsBean extends BmobObject implements Serializable {

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


    /**
     * 获取用户收藏的所有NewsBean
     *
     * @param successOrFailListener
     */
    public static void getNewsBean(SuccessOrFailListener successOrFailListener) {
        BmobQuery<NewsBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user", User.getInstance().getUsername());
        bmobQuery.findObjects(new FindListener<NewsBean>() {
            @Override
            public void done(List<NewsBean> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    successOrFailListener.success(list);
                } else if (e == null && (list == null || list.size() == 0)) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }
        });
    }

    /**
     * 删除收藏的NewsBean
     *
     * @param bean
     * @param successOrFailListener
     */
    public static void deleteNewsBean(NewsBean bean, SuccessOrFailListener successOrFailListener) {
        bean.setObjectId(bean.getObjectId());
        bean.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }

        });
    }

}
