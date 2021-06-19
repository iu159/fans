package edu.wuyi.fans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * BaseException
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public abstract class AbstractFansException extends RuntimeException{
    /** Error errorData */
    private Object errorData;

    /** Http status code */
    public abstract HttpStatus getStatus();

    public AbstractFansException(String message) {
        super(message);
    }

    public AbstractFansException(String message, Throwable cause) {
        super(message, cause);
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }
}
