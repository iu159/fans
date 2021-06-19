package edu.wuyi.fans.service;

import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface FollowService extends IService<Follow> {
    Boolean isFollowed(String followId);

    List<UserDTO> myFans(String uid);

    List<UserDTO> myFollowers(String uid);
}
