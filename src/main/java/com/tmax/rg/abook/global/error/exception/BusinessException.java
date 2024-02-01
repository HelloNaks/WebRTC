package com.tmax.rg.abook.global.error.exception;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}