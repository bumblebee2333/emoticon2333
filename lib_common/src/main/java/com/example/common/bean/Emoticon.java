package com.example.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Emoticon implements Serializable{
    /**
     * id : 13
     * label : ["加菲猫"]
     * imgUrl : http://140.143.154.18:8080/web/image/timg1548562982180.gif
     * user : {"id":12,"name":"skit","email":"2368242633@qq.com","createTime":"2019-01-13"}
     * type : {"id":2,"title":"顶住","emoticon_num":1,"follow_num":0}
     * gif : false
     * createTime : 2019-01-15
     */

    private int id;
    private String imgUrl;
    private UserBean user;
    @SerializedName("emoticonType")
    private TypeBean type;
    private boolean gif;
    private String createTime;
    private List<String> label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public boolean isGif() {
        return gif;
    }

    public void setGif(boolean gif) {
        this.gif = gif;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public static class UserBean {
        /**
         * id : 12
         * name : skit
         * email : 2368242633@qq.com
         * createTime : 2019-01-13
         */

        private int id;
        private String name;
        private String email;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    public static class TypeBean {
        /**
         * id : 2
         * title : 顶住
         * emoticon_num : 1
         * follow_num : 0
         */

        private int id;
        private String title;
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
