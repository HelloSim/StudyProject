package com.sim.traveltool.bean;

import java.util.List;

/**
 * @author Sim --- 搜索指定公交的路线站点返回的数据bean类
 */
public class BusRealTimeBusStopDataBean {

    /**
     * flag : 1002
     * data : [{"Id":"109e18ad0fec42fcafac75f41160f8b2","Name":"香洲","Lng":"113.562073","Lat":"22.282845","Description":""},{"Id":"37e0f37540d342cc8c37746c6fcd207a","Name":"为农市场","Lng":"113.562845","Lat":"22.288423","Description":""},{"Id":"599e939db4134f2a9e82e6dfdaf6941d","Name":"邮政大厦","Lng":"113.569861","Lat":"22.284594","Description":""},{"Id":"008ed6992df240f18136f3d2b9b3cfb6","Name":"百货公司","Lng":"113.573468","Lat":"22.278952","Description":""},{"Id":"5e0098333f4044ceaf45d2a513c0f75b","Name":"人民医院","Lng":"113.570490","Lat":"22.276650","Description":""},{"Id":"caa5fd1dbed14632875158d5dbe79ed9","Name":"二中","Lng":"113.569898","Lat":"22.273832","Description":""},{"Id":"2a0a1b5051c94eb89857bddd1c80bd6c","Name":"滨海楼","Lng":"113.561217","Lat":"22.274028","Description":""},{"Id":"22e05ea260c1428baf3d5b5cd032e63e","Name":"银桦新村","Lng":"113.555503","Lat":"22.274633","Description":""},{"Id":"419aae94bc4d4d7f85fc2cbc52a9df5f","Name":"新村","Lng":"113.549798","Lat":"22.274052","Description":""},{"Id":"8bf54deada644f61b5bea3f8090cc1ce","Name":"南村","Lng":"113.540685","Lat":"22.272480","Description":""},{"Id":"7a2b8cd7b1ae412194357e684b8bdf4c","Name":"香洲区府","Lng":"113.534852","Lat":"22.271537","Description":""},{"Id":"e8de03d1600144d0b6e24a2d2ee9ae2b","Name":"红山、仁恒星园","Lng":"113.531358","Lat":"22.270957","Description":""},{"Id":"a9d903998f354db98a9aa9c2aad8c8e5","Name":"安居园","Lng":"113.525528","Lat":"22.270069","Description":""},{"Id":"76c82cc4394e46efb6ded972aadb95d6","Name":"新香洲万家","Lng":"113.522370","Lat":"22.269587","Description":""},{"Id":"5b7d996a8cb342d1b264add76e40f949","Name":"恒隆学校","Lng":"113.520018","Lat":"22.267975","Description":""},{"Id":"f38203b124484ca5996fd7943cfeb94d","Name":"翠微市场（百安门诊部）","Lng":"113.520427","Lat":"22.264573","Description":""},{"Id":"047deea726b34b2e977fcaf4030ef86a","Name":"招商花园城","Lng":"113.520583","Lat":"22.261591","Description":""},{"Id":"a1ec06c8e6d74b8a9e1f14184dd81e1d","Name":"招商花园城南","Lng":"113.517810","Lat":"22.258008","Description":""},{"Id":"c6c4b8a19a8c487a8719e1ce018fff69","Name":"翠景工业区","Lng":"113.514747","Lat":"22.256042","Description":""},{"Id":"2376630ca548456dbf375111abdce37a","Name":"明珠中","Lng":"113.515175","Lat":"22.252183","Description":""},{"Id":"35cba6c8878041a698fb52adc2fc8ff7","Name":"明珠商业广场","Lng":"113.515553","Lat":"22.248461","Description":""},{"Id":"25107e5078d348839dfd616ed684f08a","Name":"漾湖明居","Lng":"113.516697","Lat":"22.241025","Description":""},{"Id":"4e5f3b98cc6e4bb09e844826e2e212f8","Name":"明珠山庄","Lng":"113.519303","Lat":"22.239697","Description":""},{"Id":"23888a4c15f04339835b4b4891cd5987","Name":"白石南、银石雅园南","Lng":"113.531312","Lat":"22.234682","Description":""},{"Id":"12bff5589ff340448915aec570834fd7","Name":"供水总公司","Lng":"113.534304","Lat":"22.233607","Description":""},{"Id":"e9a46a03322c42aeaa02f68c3ea2f7dd","Name":"合罗山","Lng":"113.539481","Lat":"22.231647","Description":""},{"Id":"543831c74fd24756acc0489a3f29fa6b","Name":"凉粉桥","Lng":"113.545528","Lat":"22.229622","Description":""},{"Id":"57c5d2808dc74ebbb70e26d85368405f","Name":"拱北","Lng":"113.547950","Lat":"22.224850","Description":""},{"Id":"8b30950285a24e929bbbab85ce56ccd1","Name":"拱北口岸总站","Lng":"113.550592","Lat":"22.220958","Description":""}]
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
         * Id : 109e18ad0fec42fcafac75f41160f8b2
         * Name : 香洲
         * Lng : 113.562073
         * Lat : 22.282845
         * Description :
         */

        private String Id;
        private String Name;
        private String Lng;
        private String Lat;
        private String Description;

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

        public String getLng() {
            return Lng;
        }

        public void setLng(String Lng) {
            this.Lng = Lng;
        }

        public String getLat() {
            return Lat;
        }

        public void setLat(String Lat) {
            this.Lat = Lat;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }
    }
}
