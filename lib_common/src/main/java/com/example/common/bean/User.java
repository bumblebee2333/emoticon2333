package com.example.common.bean;

import com.google.gson.annotations.SerializedName;

public class User {
    /**
     * id : 12
     * name : skit
     * email : 2368242633@qq.com
     * createTime : 2019-01-13
     * token : f8519dab9f05a09417f1d373311259c6
     */

    private int id;
    private String name;
    private String email;
    private String createTime;
    private String updateTime;
    private String token;
    private String device;

    @SerializedName("icon")
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

}
