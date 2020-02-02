package com.oldman.manage.utils;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    
	private static final long serialVersionUID = -6682907237798192284L;

	private Integer code = Code.SUCCESS;
    
    private String msg;

    public BaseResponse() {
    }

    public BaseResponse(String msg) {
        this.msg = msg;
    }

    public BaseResponse(Integer code) {
        this.code = code;
    }

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public BaseResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}