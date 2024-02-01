package com.tmax.rg.abook.global.response;

import com.tmax.rg.abook.global.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private String description;

    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.description = code.getMessage();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    private ErrorResponse(final ErrorCode code, String description) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.description = description;
    }

    public static ErrorResponse of(final ErrorCode code, String description) {
        return new ErrorResponse(code, description);
    }
}