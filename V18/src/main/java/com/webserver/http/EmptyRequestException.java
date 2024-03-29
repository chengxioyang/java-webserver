package com.webserver.http;

/**
 * 空请求异常
 * 当HttpServletRequest在解析请求时如果发现是空的请求就会抛出该异常
 */
public class EmptyRequestException extends Exception{
    public EmptyRequestException() {

    }

    public EmptyRequestException(String message) {
        super(message);
    }

    public EmptyRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRequestException(Throwable cause) {
        super(cause);
    }

    public EmptyRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
