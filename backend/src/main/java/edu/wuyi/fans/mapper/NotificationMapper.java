package edu.wuyi.fans.mapper;

import edu.wuyi.fans.model.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface NotificationMapper extends BaseMapper<Notification> {
    void updateNotificationStatus(String uid);
}
