package com.tasfe.sis.base.model.response;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by Lait on 2017/7/27.
 */
@Getter
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = -720807478055084231L;
    private String status;
    private String error;
    private String msg;
    private String code;
    private T data;
    public ResponseData() {
    }

    public static ResponseData<?> success(){
        ResponseData responseData = new ResponseData();
        responseData.setStatus("0");
        responseData.setMsg("");
        responseData.setData(null);
        return responseData;
    }

    public static ResponseData<?> error(){
        ResponseData responseData = new ResponseData();
        responseData.setStatus("1");
        responseData.setMsg("");
        responseData.setData(null);
        return responseData;
    }

    public ResponseData<T> setStatus(String status) {
        this.status = status;
        return this;
    }

    public ResponseData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public ResponseData<T> setData(T data) {
        this.data = data;
        return this;
    }
    public ResponseData<T> setError(String error) {
        this.error = error;
        return this;
    }

    public ResponseData setCode(String code) {
        this.code = code;
        return this;
    }



   /* public ResponseData(String status) {
        this.status = status;
    }

    public ResponseData(String status, String error) {
        this.status = status;
        this.error = error;
    }*/

    /*public ResponseData(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseData(String status, T data) {
        this.status = status;
        this.data = data;
        //this.pageCount = pageCount;
    }*/

    /*public ResponseData(String status, String error, String msg, T data) {
        this.status = status;
        this.error = error;
        this.msg = msg;
        this.data = data;
    }*/

 /*   public String getStatus() {
        return this.status;
    }*/



    /*public String getError() {
        return this.error;
    }

    public ResponseData<T> setError(String error) {
        this.error = error;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public ResponseData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public ResponseData<T> setData(T data) {
        this.data = data;
        return this;
    }*/

    /*public String getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }*/

   /* public String getCode() {
        return this.code;
    }

    public ResponseData setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(!(o instanceof ResponseData)) {
            return false;
        } else {
            ResponseData<?> other = (ResponseData)o;
            if(!other.canEqual(this)) {
                return false;
            } else {
                Object this$status = this.getStatus();
                Object other$status = other.getStatus();
                if(this$status == null) {
                    if(other$status != null) {
                        return false;
                    }
                } else if(!this$status.equals(other$status)) {
                    return false;
                }

                Object this$error = this.getError();
                Object other$error = other.getError();
                if(this$error == null) {
                    if(other$error != null) {
                        return false;
                    }
                } else if(!this$error.equals(other$error)) {
                    return false;
                }

                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if(this$msg == null) {
                    if(other$msg != null) {
                        return false;
                    }
                } else if(!this$msg.equals(other$msg)) {
                    return false;
                }

                label62: {
                    Object this$data = this.getData();
                    Object other$data = other.getData();
                    if(this$data == null) {
                        if(other$data == null) {
                            break label62;
                        }
                    } else if(this$data.equals(other$data)) {
                        break label62;
                    }

                    return false;
                }*/
/*
                label55: {
                    Object this$pageCount = this.getPageCount();
                    Object other$pageCount = other.getPageCount();
                    if(this$pageCount == null) {
                        if(other$pageCount == null) {
                            break label55;
                        }
                    } else if(this$pageCount.equals(other$pageCount)) {
                        break label55;
                    }

                    return false;
                }*/
/*
                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if(this$code == null) {
                    if(other$code != null) {
                        return false;
                    }
                } else if(!this$code.equals(other$code)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResponseData;
    }*/



}
