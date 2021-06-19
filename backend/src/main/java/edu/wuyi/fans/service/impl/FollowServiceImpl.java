package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.Follow;
import edu.wuyi.fans.mapper.FollowMapper;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.util.FansUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<UserDTO> myFans(String uid) {
        QueryWrapper followWrapper = new QueryWrapper();
        followWrapper.eq("follower_id",uid);
        List<Follow> list = followMapper.selectList(followWrapper);
        List<UserDTO> fans = new ArrayList<>();
        list.forEach(follow ->
            fans.add(userService.getUserInfo(follow.getFanId()))
        );
        return fans;
    }

    @Override
    public List<UserDTO>  myFollowers(String uid) {
        QueryWrapper followWrapper = new QueryWrapper();
        followWrapper.eq("fan_id",uid);
        List<Follow> list = followMapper.selectList(followWrapper);
        List<UserDTO> followers = new ArrayList<>();
        list.forEach(follow ->
                followers.add(userService.getUserInfo(follow.getFollowerId()))
        );
        return followers;
    }

    @Override
    public Boolean isFollowed(String followId) {
        User principal = (User) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(principal)){
            QueryWrapper<Follow> followWrapper = new QueryWrapper<>();
            followWrapper.eq("fan_id",principal.getUid());
            followWrapper.eq("follower_id",followId);
            Follow follow = followMapper.selectOne(followWrapper);
            if (!ObjectUtils.isEmpty(follow)){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
