package edu.wuyi.fans.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class AlreadyExistsException extends BadRequestException{

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
