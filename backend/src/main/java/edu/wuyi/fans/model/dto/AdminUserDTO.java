package edu.wuyi.fans.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/23
 */
@Data
public class AdminUserDTO {
    private String uid;

    private String username;

    private String profilePictureUrl;

    private List<String> roles;
}
