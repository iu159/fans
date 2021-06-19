package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.mapper.NotificationMapper;
import edu.wuyi.fans.model.dto.NotificationDTO;
import edu.wuyi.fans.model.entity.Notification;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.model.param.CommentParam;
import edu.wuyi.fans.service.CommentService;
import edu.wuyi.fans.service.NotificationService;
import edu.wuyi.fans.service.PictureService;
import edu.wuyi.fans.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service

public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Override
    public int getNotificationSize(String userId) {
        QueryWrapper<Notification> notificationWrapper = new QueryWrapper<>();
        notificationWrapper.eq("receiver_id",userId);
        notificationWrapper.eq("is_read",0);
        return notificationMapper.selectCount(notificationWrapper);
    }

    @Override
    public List<NotificationDTO> listNotifications(String userId) {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        QueryWrapper<Notification> notificationWrapper = new QueryWrapper<>();
        notificationWrapper.eq("receiver_id",userId);
        notificationWrapper.orderByDesc("create_time");
        List<Notification> notifications = notificationMapper.selectList(notificationWrapper);
        if (notifications.size()>0){
            notifications.forEach(notification -> {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification,notificationDTO);
                notificationDTO.setSender(userService.getUserInfo(notification.getSenderId()));
                if(!StringUtils.isEmpty(notification.getPictureId())) {
                    notificationDTO.setTitle(pictureService.getById(notification.getPictureId()).getTitle());
                }
                notificationDTOList.add(notificationDTO);
            });
            notificationMapper.updateNotificationStatus(userId);
            return notificationDTOList;
        }else {
            return null;
        }
    }

    @Override
    public void saveLike(String userId, String pictureId) {
        Notification notification = new Notification();
        User sender = userService.getUser(userId);
        notification.setSenderId(userId);
        notification.setReceiverId(pictureService.getUid(pictureId));
        notification.setContent("喜欢了你的");
        notification.setRead(false);
        notification.setPictureId(pictureId);
        notificationMapper.insert(notification);
    }

    @Override
    public void saveComment(String userId, CommentParam commentParam) {
        String pictureId = commentParam.getPictureId();
        Notification notification = new Notification();
        User sender = userService.getUser(userId);
        notification.setSenderId(userId);
        notification.setReceiverId(pictureService.getUid(pictureId));
        notification.setContent("评论了你的");
        notification.setRead(false);
        notification.setPictureId(pictureId);
        notificationMapper.insert(notification);

        // 如不是一级评论，应向一级评论发送通知
        Integer parentId = commentParam.getParentId();
        if (!StringUtils.isEmpty(parentId)){
            Notification toParent = new Notification();
            toParent.setSenderId(userId);
            toParent.setReceiverId(commentService.getById(parentId).getUserId());
            toParent.setContent("回复了你的评论，");
            toParent.setRead(false);
            toParent.setPictureId(pictureId);
            notificationMapper.insert(toParent);
        }
    }

    @Override
    public void saveFollow(String followerId, String fanId) {
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setReceiverId(followerId);
        notification.setSenderId(fanId);
        notification.setContent("关注了你");
        notificationMapper.insert(notification);
    }

    @Override
    public void saveSystemNotification(String userId, String content) {
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setReceiverId(userId);
        notification.setContent(content);
        notificationMapper.insert(notification);
    }
}
