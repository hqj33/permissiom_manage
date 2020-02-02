package com.oldman.manage.utils;

public class NormalResponse extends BaseResponse {

    private static final long serialVersionUID = -6291902016570443950L;
    private Object data;
    private int count;

    public NormalResponse() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NormalResponse(Object data) {
        super();
        this.setData(data);
    }

    public NormalResponse(Integer code) {
        super(code);
    }

    public NormalResponse(Integer code, String msg) {
        super(code, msg);
    }

    public NormalResponse(String msg) {
        super(msg);
    }

    public Object getData() {
        return data;
    }

    public NormalResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public NormalResponse setCount(Integer count) {
        this.count = count;
        return this;
    }

}