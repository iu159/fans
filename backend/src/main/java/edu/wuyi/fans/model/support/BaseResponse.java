package edu.wuyi.fans.model.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/9
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    /** 返回的状态码 */
    private Integer status;

    /** 返回的消息 */
    private String message;

    /** 返回的数据 */
    private T data;

    /**
     * Creates an ok result with message and data. (Default status is 200)
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message, @Nullable T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), message, data);
    }

    /**
     * Creates an ok result with message only. (Default status is 200)
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message) {
        return ok(message, null);
    }

    /**
     * Creates an ok result with data only. (Default message is OK, status is 200)
     */
    public static <T> BaseResponse<T> ok(@NonNull T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

}
