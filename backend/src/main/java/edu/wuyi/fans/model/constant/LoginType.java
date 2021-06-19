package edu.wuyi.fans.model.constant;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
public enum LoginType {
    TELEPHONE("telephone","手机号码"),

    EMAIL("email","邮箱"),

    //考虑用户名和邮箱正则可能重复，用户名登陆暂不采用
    USERNAME("username","用户名");

    private String method;

    private String description;

    LoginType(String method, String description) {
        this.method = method;
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }
}
