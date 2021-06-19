package edu.wuyi.fans.model.constant;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
public enum RoleType {
    SUPERVISOR(1,"超级管理员"),

    ADMIN(2,"管理员"),

    REGULAR(3,"普通用户");

    private int Status;

    private String description;

    RoleType(int status, String description) {
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
