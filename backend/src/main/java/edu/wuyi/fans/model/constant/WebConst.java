package edu.wuyi.fans.model.constant;

import org.springframework.stereotype.Component;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */

@Component
public class WebConst {
    /** MD5 加密 盐值 */
    public static final String MD5_SALT = "fans";

    /** 每页图片量 */
    public static final Integer PAGE_SIZE = 20;

    /** 每页显示量 */
    public static final Integer PAGE_COMM_SIZE = 10;

    public static final  String EMAIL_REGEX = "^[A-Za-z0-9_-\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public static final  String TELEPHONE_REGEX = "^1[3-9][0-9]{9}$";
}
