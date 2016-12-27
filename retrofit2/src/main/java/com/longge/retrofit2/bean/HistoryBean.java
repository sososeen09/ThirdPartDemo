package com.longge.retrofit2.bean;

import java.util.List;

/**
 * Created by suyunlong on 2016/12/27.
 */

public class HistoryBean {
    public InfoRepo info;
    public List<DatasRepo> datas;

    public static class InfoRepo {
        /**
         * id : 403
         * count : 917
         * updatetime : 1482768000000
         * nickname : AG100
         * name : 现货白银100G
         * tradedate : 20161227
         * preclose : 3.544
         */

        public int id;
        public int count;
        public long updatetime;
        public String nickname;
        public String name;
        public String tradedate;
        public double preclose;

        @Override
        public String toString() {
            return "InfoRepo{" +
                    "id=" + id +
                    ", count=" + count +
                    ", updatetime=" + updatetime +
                    ", nickname='" + nickname + '\'' +
                    ", name='" + name + '\'' +
                    ", tradedate='" + tradedate + '\'' +
                    ", preclose=" + preclose +
                    '}';
        }
    }

    public static class DatasRepo {
        /**
         * open : 0
         * updatetime : 20161227 06:00:00
         * status : 0
         * high : 0
         * low : 0
         * close : 0
         */

        public double open;
        public String updatetime;
        public int status;
        public double high;
        public double low;
        public double close;

        @Override
        public String toString() {
            return "DatasRepo{" +
                    "open=" + open +
                    ", updatetime='" + updatetime + '\'' +
                    ", status=" + status +
                    ", high=" + high +
                    ", low=" + low +
                    ", close=" + close +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HistoryBean{" +
                "info=" + info +
                ", datas=" + datas +
                '}';
    }
}
