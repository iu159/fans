package edu.wuyi.fans.model.dto;

import lombok.Data;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/11
 */
@Data
public class NotificationDTO {
    private UserDTO sender;

    private String content;

    private String pictureId;

    private String title;

    private boolean isRead;
}
