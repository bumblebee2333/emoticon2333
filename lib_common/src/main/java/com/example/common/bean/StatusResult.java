package com.example.common.bean;

public class StatusResult<T> {

    /**
     * msg : 提交成功
     * status : 200
     * data : 返回的数据
     */

    private String msg;
    private int status;
    private T data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){
        return status == 200;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
