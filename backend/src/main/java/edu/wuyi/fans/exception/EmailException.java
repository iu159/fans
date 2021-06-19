package edu.wuyi.fans.exception;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class EmailException extends ServiceException {
    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
