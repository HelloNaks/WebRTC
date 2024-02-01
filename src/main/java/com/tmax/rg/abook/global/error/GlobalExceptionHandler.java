package com.tmax.rg.abook.global.error;

import com.fasterxml.jackson.core.JsonParseException;
import com.tmax.rg.abook.global.error.exception.BusinessException;
import com.tmax.rg.abook.global.error.exception.ErrorCode;
import com.tmax.rg.abook.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Regular Expression으로 ';' 전까지의 문자열 추출
    public static String extractTextBeforeSemicolon(String errorMessage) {
        if (errorMessage == null) {
            return null;
        }

        Matcher matcher = Pattern.compile("^(.*?);").matcher(errorMessage);

        return matcher.find() ? matcher.group(1) : errorMessage;
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldErrors -> fieldErrors.getObjectName() + "." + fieldErrors.getField() + " : " + fieldErrors.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("handleMethodArgumentNotValidException", errorMessage);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> HttpMessageNotReadableException(JsonParseException e) {
        // 예외에 대한 로깅을 수행
        return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, e.getMessage());

        if(errorCode.getStatus() / 100 == 4) {
            log.error("\nBusinessException occurred caused by client: {}", errorCode);
            log.error("\n{}", Arrays.stream(e.getStackTrace()).findFirst().orElse(null));
        }
        else if(errorCode.getStatus() / 100 == 5) {
            log.error("\nBusinessException occurred caused by server: {}", errorCode.getMessage());
            log.error("\n{}", Arrays.stream(e.getStackTrace()).findFirst().orElse(null));
        }
        else {
            log.error("BusinessException", e);
        }

        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, extractTextBeforeSemicolon(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
