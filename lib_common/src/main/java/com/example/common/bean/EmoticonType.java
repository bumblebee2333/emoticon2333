package com.example.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EmoticonType implements Serializable {
    public static class EmoticonTypeList implements Serializable {
        private String msg;
        private int status;
        @SerializedName("data")
        private List<DataBean> dataList;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<DataBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataBean> dataList) {
            this.dataList = dataList;
        }
    }

    public static class Data implements Serializable {

        /**
         * msg : ok
         * data : {"id":13,"title":"测试","icon":"hh.com","emoticon_num":0,"follow_num":0}
         * status : 200
         */

        private String msg;
        private DataBean data;
        private int status;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class DataBean {
            /**
             * id : 13
             * title : 测试
             * icon : hh.com
             * emoticon_num : 0
             * follow_num : 0
             */

            private int id;
            private String title;
            private String icon;
            private int emoticon_num;
            private int follow_num;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getEmoticon_num() {
                return emoticon_num;
            }

            public void setEmoticon_num(int emoticon_num) {
                this.emoticon_num = emoticon_num;
            }

            public int getFollow_num() {
                return follow_num;
            }

            public void setFollow_num(int follow_num) {
                this.follow_num = follow_num;
            }
        }
    }

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * title : 自闭
         * icon : http://140.143.154.18:8080/web/image/miao1548563061114.jpg
         * emoticon_num : 0
         * follow_num : 2
         */

        private int id;
        private String title;
        private String icon;
        private int emoticon_num;
        private int follow_num;
        private List<Emoticon> emoticons;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getEmoticon_num() {
            return emoticon_num;
        }

        public void setEmoticon_num(int emoticon_num) {
            this.emoticon_num = emoticon_num;
        }

        public int getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(int follow_num) {
            this.follow_num = follow_num;
        }

        public List<Emoticon> getEmoticons() {
            return emoticons;
        }

        public void setEmoticons(List<Emoticon> emoticons) {
            this.emoticons = emoticons;
        }
    }
}
