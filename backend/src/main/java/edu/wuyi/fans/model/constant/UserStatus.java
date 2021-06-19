package edu.wuyi.fans.model.constant;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
public enum UserStatus {

    UNACTIVATED(0,"未激活"),

    REGULAR(1,"正常"),

    BLOCK(4,"封禁");

    private int Status;

    private String description;

    UserStatus(int status, String description) {
        Status = status;
        this.description = description;
    }

    public int getStatus() {
        return Status;
    }

    public String getDescription() {
        return description;
    }
}
