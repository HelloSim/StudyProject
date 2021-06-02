package com.sim.user.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

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

}
