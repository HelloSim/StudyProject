package com.sim.traveltool.bean;

import java.util.List;

/**
 * @author Sim --- 实时公交数据bean类
 */
public class BusRealTimeDataBean {

    /**
     * flag : 1002
     * data : [{"BusNumber":"粤C05672D","CurrentStation":"海虹总站","LastPosition":"8"},{"BusNumber":"粤C00536D","CurrentStation":"桃园新村","LastPosition":"8"},{"BusNumber":"粤C00664D","CurrentStation":"新村","LastPosition":"5"},{"BusNumber":"粤C00265D","CurrentStation":"新香洲万家","LastPosition":"8"},{"BusNumber":"粤C00529D","CurrentStation":"上冲","LastPosition":"8"},{"BusNumber":"粤C07803D","CurrentStation":"明珠北","LastPosition":"5"},{"BusNumber":"粤C00655D","CurrentStation":"明珠中","LastPosition":"8"},{"BusNumber":"粤C07065D","CurrentStation":"南屏","LastPosition":"8"},{"BusNumber":"粤C03359D","CurrentStation":"湾仔中学","LastPosition":"5"},{"BusNumber":"粤C07603D","CurrentStation":"湾仔旅游码头","LastPosition":"5"}]
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
         * BusNumber : 粤C05672D
         * CurrentStation : 海虹总站
         * LastPosition : 8
         */

        private String BusNumber;
        private String CurrentStation;
        private String LastPosition;

        public String getBusNumber() {
            return BusNumber;
        }

        public void setBusNumber(String BusNumber) {
            this.BusNumber = BusNumber;
        }

        public String getCurrentStation() {
            return CurrentStation;
        }

        public void setCurrentStation(String CurrentStation) {
            this.CurrentStation = CurrentStation;
        }

        public String getLastPosition() {
            return LastPosition;
        }

        public void setLastPosition(String LastPosition) {
            this.LastPosition = LastPosition;
        }
    }
}
