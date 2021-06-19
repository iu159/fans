package edu.wuyi.fans.service;

import edu.wuyi.fans.model.dto.NotificationDTO;
import edu.wuyi.fans.model.entity.Notification;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.wuyi.fans.model.param.CommentParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface NotificationService extends IService<Notification> {
    void saveLike(String userId, String pictureId);

    void saveComment(String userId, CommentParam commentParam);

    void saveFollow(String followerId, String fanId);

    void saveSystemNotification(String userId, String content);

    int getNotificationSize(String userId);

    List<NotificationDTO> listNotifications(String userId);
}
