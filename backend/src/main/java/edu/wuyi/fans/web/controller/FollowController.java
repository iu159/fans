package edu.wuyi.fans.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.model.entity.Follow;
import edu.wuyi.fans.service.FollowService;
import edu.wuyi.fans.service.NotificationService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@Api(value = "关注相关接口", tags = "关注相关接口")
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationService notificationService;

    @ApiOperation("关注")
    @GetMapping("/follow/{uid}")
    void follow(@PathVariable("uid") String followerId){
        String fanId = FansUtils.getUid();
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFanId(fanId);
        notificationService.saveFollow(followerId, fanId);
        followService.save(follow);
    }

    @ApiOperation("取消关注")
    @GetMapping("/unfollow/{uid}")
    void unfollow(@PathVariable("uid") String followerId){
        String fanId = FansUtils.getUid();
        QueryWrapper<Follow> unfollowWrapper = new QueryWrapper();
        unfollowWrapper.eq("follower_id",followerId);
        unfollowWrapper.eq("fan_id",fanId);
        followService.remove(unfollowWrapper);
    }
}

