package edu.wuyi.fans.handler;

import edu.wuyi.fans.exception.AbstractFansException;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
@RestControllerAdvice(value = "edu.wuyi.fans.web.controller")
@Slf4j
public class ControllerExceptionHandler {


    /**
     * 对方法参数校验异常处理方法
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<?>>  handlerNotValidException(BindException e) {
        BaseResponse<Object> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        BindingResult result = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                sb.append(error.getDefaultMessage());
                sb.append("-");
            });
        } else {
            sb.append("请求参数违规-");
        }
        baseResponse.setMessage(sb.substring(0,sb.length()-1));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BaseResponse<Object> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        BindingResult result = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                sb.append(error.getDefaultMessage());
                sb.append("-");
            });
        } else {
            sb.append("请求参数违规-");
        }
        baseResponse.setMessage(sb.substring(0,sb.length()-1));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }


    @ExceptionHandler(AbstractFansException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<?>> handleFansException(AbstractFansException e) {
        BaseResponse<Object> baseResponse = handleBaseException(e);
        baseResponse.setStatus(e.getStatus().value());
        baseResponse.setData(e.getErrorData());
        return ResponseEntity.status(e.getStatus()).body(baseResponse);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleGlobalException(Exception e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        baseResponse.setStatus(status.value());
        baseResponse.setMessage(status.getReasonPhrase());
        return baseResponse;
    }

    private <T> BaseResponse<T> handleBaseException(Throwable t) {
        Assert.notNull(t, "Throwable must not be null");

        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(t.getMessage());

        if (log.isDebugEnabled()) {
            log.error("Captured an exception:", t);
            baseResponse.setMessage(ExceptionUtils.getStackTrace(t));
        } else {
            log.error("Captured an exception: [{}]", t.getMessage());
        }
        return baseResponse;
    }
}
