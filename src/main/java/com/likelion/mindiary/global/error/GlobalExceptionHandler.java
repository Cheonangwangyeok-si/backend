package com.likelion.mindiary.global.error;

import com.likelion.mindiary.global.error.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_LOG_MESSAGE = "Exception: {}, Message: {}";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(DEFAULT_LOG_MESSAGE, ex.getClass(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE,
                ex.getBindingResult());
        return ResponseEntity.badRequest().headers(headers).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        log.error(DEFAULT_LOG_MESSAGE, ex.getClass(), ex.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(CommonErrorCode.INVALID_REQUEST));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error(DEFAULT_LOG_MESSAGE, ex.getClass(), ex.getMessage());

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * javax.validation.ConstraintViolationException 으로 제약조건을 위반할 때 발생한다. 주로 @Min, @Max 등 제약을 두는
     * 어노테이션에서 발생
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        log.error(DEFAULT_LOG_MESSAGE, e.getClass(), e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE));
    }

    /**
     * BusinessException 을 처리, 비즈니스 로직상에서 BusinessException을 상속한 예외 발생시 처리된다
     *
     * @param e
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e,
            HttpServletRequest request) {
        log.error(DEFAULT_LOG_MESSAGE, e.getClass(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }
}