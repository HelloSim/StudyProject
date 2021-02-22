package com.sim.traveltool.bean;

import java.util.List;

/**
 * @author Sim --- 位置搜索返回的数据bean类
 */
public class BusLocationDataBean {

    /**
     * status : 1
     * count : 3
     * info : OK
     * infocode : 10000
     * tips : [{"id":"BV10250452","name":"省科干学院(公交站)","district":"广东省珠海市金湾区","adcode":"440404","location":"113.351585,22.131067","address":"(停运)826-1路;204路;204路(周日700-1030);540路;540路短线;541路;605路;803路;826-2路;K7路;K8路;湖心路口总站-平沙夜间线;金湾-深圳","typecode":"150700","city":[]},{"id":"B0FFK9OIYC","name":"省科干教师家园10栋","district":"广东省珠海市金湾区","adcode":"440404","location":"113.358860,22.124976","address":[],"typecode":"190000","city":[]},{"id":"B0FFJH4JJD","name":"省科干教师家园9栋楼","district":"广东省珠海市金湾区","adcode":"440404","location":"113.358424,22.124922","address":"德馨东路与德馨南路交叉口南200米","typecode":"190403","city":[]}]
     */

    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<TipsBean> tips;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<TipsBean> getTips() {
        return tips;
    }

    public void setTips(List<TipsBean> tips) {
        this.tips = tips;
    }

    public static class TipsBean {
        /**
         * id : BV10250452
         * name : 省科干学院(公交站)
         * district : 广东省珠海市金湾区
         * adcode : 440404
         * location : 113.351585,22.131067
         * address : (停运)826-1路;204路;204路(周日700-1030);540路;540路短线;541路;605路;803路;826-2路;K7路;K8路;湖心路口总站-平沙夜间线;金湾-深圳
         * typecode : 150700
         * city : []
         */

        private Object id;
        private Object name;
        private Object district;
        private Object adcode;
        private Object location;
        private Object address;
        private Object typecode;
        private List<?> city;

        public Object getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Object getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getTypecode() {
            return typecode;
        }

        public void setTypecode(String typecode) {
            this.typecode = typecode;
        }

        public List<?> getCity() {
            return city;
        }

        public void setCity(List<?> city) {
            this.city = city;
        }
    }

}
