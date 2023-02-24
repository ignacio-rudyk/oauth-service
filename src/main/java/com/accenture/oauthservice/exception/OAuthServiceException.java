package com.accenture.oauthservice.exception;

public class OAuthServiceException extends Exception {

    private Integer code;

    private static String OAUTH_SERVICE_EXCEPTION = "Hubo un error en el servicio";

    public OAuthServiceException() {
        this(OAUTH_SERVICE_EXCEPTION, -1);
    }

    public OAuthServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
