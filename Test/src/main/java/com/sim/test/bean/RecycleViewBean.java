package com.sim.test.bean;

/**
 * RecycleViewActivity.class中的Bean类
 */
public class RecycleViewBean {

    //名字
    private String name;
    //图片id
    private int imageId;

    public RecycleViewBean(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}
