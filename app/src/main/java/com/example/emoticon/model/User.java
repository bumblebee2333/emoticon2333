package com.example.emoticon.model;

public class User {

    /**
     * msg : 登陆成功
     * data : {"id":12,"name":"skit","email":"2368242633@qq.com","createtime":"2019-01-13","token":"f8519dab9f05a09417f1d373311259c6"}
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
         * id : 12
         * name : skit
         * email : 2368242633@qq.com
         * createtime : 2019-01-13
         * token : f8519dab9f05a09417f1d373311259c6
         */

        private int id;
        private String name;
        private String email;
        private String createtime;
        private String token;
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

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

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}