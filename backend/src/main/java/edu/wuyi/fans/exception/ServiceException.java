package edu.wuyi.fans.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class ServiceException extends AbstractFansException{
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
