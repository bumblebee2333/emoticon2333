package com.example.emoticon.model;

public class Upload {
    /**
     * status : 200
     * msg : 上传成功
     * url : http://140.143.154.18:8080/web/image/miao1549034990052.jpg
     */

    private int status;
    private String msg;
    private String url;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
