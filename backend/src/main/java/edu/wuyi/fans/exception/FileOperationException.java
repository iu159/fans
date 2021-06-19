package edu.wuyi.fans.exception;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/16
 */
public class FileOperationException extends ServiceException {
    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
