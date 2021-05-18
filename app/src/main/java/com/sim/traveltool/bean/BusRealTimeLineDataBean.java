package com.sim.traveltool.bean;

import java.util.List;

/**
 * @author Sim --- 实时公交路线搜索返回的数据bean类
 */
public class BusRealTimeLineDataBean {

    /**
     * flag : 1002
     * data : [{"Id":"afec0bea-5532-4872-b06e-a1b5e97e4c3f","Name":"1路","LineNumber":"1路","Direction":1,"FromStation":"城轨珠海站","ToStation":"香洲","BeginTime":"06:20","EndTime":"01:15","Price":"1","Interval":"7.5","Description":"","StationCount":25},{"Id":"c72cd82b-be2f-4879-a4a2-f4c05ecf54fd","Name":"1路","LineNumber":"1路","Direction":0,"FromStation":"香洲","ToStation":"城轨珠海站","BeginTime":"06:20","EndTime":"00:25","Price":"1","Interval":"7.5","Description":"","StationCount":27}]
     */

    private int flag;
    private List<DataBean> data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Id : afec0bea-5532-4872-b06e-a1b5e97e4c3f
         * Name : 1路
         * LineNumber : 1路
         * Direction : 1
         * FromStation : 城轨珠海站
         * ToStation : 香洲
         * BeginTime : 06:20
         * EndTime : 01:15
         * Price : 1
         * Interval : 7.5
         * Description :
         * StationCount : 25
         */

        private String Id;
        private String Name;
        private String LineNumber;
        private int Direction;
        private String FromStation;
        private String ToStation;
        private String BeginTime;
        private String EndTime;
        private String Price;
        private String Interval;
        private String Description;
        private int StationCount;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getLineNumber() {
            return LineNumber;
        }

        public void setLineNumber(String LineNumber) {
            this.LineNumber = LineNumber;
        }

        public int getDirection() {
            return Direction;
        }

        public void setDirection(int Direction) {
            this.Direction = Direction;
        }

        public String getFromStation() {
            return FromStation;
        }

        public void setFromStation(String FromStation) {
            this.FromStation = FromStation;
        }

        public String getToStation() {
            return ToStation;
        }

        public void setToStation(String ToStation) {
            this.ToStation = ToStation;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public String getInterval() {
            return Interval;
        }

        public void setInterval(String Interval) {
            this.Interval = Interval;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public int getStationCount() {
            return StationCount;
        }

        public void setStationCount(int StationCount) {
            this.StationCount = StationCount;
        }
    }

}
