package edu.wuyi.fans.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class NotFoundException extends AbstractFansException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
