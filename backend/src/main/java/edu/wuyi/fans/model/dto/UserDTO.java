package edu.wuyi.fans.model.dto;

import lombok.Data;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/29
 */

@Data
public class UserDTO {
    private String uid;

    private String username;

    private String description;

    private String profilePictureUrl;
}
