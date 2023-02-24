package com.accenture.oauthservice.model;

public class ErrorResponse {

    private Integer code;

    private String desc;

    private Object data;

    public ErrorResponse() { this(null); }

    public ErrorResponse(Object data) {
        this(new Integer(0) , new String(), data);
    }

    public ErrorResponse(Integer code, String desc) {
        this(code , desc, null);
    }

    public ErrorResponse(Integer code, String desc, Object data) {
        super();
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }
}
