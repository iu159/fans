package edu.wuyi.fans.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.model.entity.Likes;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.service.LikesService;
import edu.wuyi.fans.service.NotificationService;
import edu.wuyi.fans.service.PictureService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/like")
@Api(value = "喜欢相关接口", tags = "喜欢相关接口")
public class LikesController {
    @Autowired
    private LikesService likesService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private NotificationService notificationService;

    @ApiOperation("我的喜欢列表")
    @GetMapping("/myLikes")
    List<Picture> listMyLikes() {
        String uid = FansUtils.getUid();
        List<Likes> likes = likesService.listMyLikes( uid);
        List<Picture> pictures = new ArrayList<>();
        likes.forEach(
                like -> {
                    Picture picture = pictureService.getById(like.getPictureId());
                    pictures.add(picture);
                }
        );
        return pictures;
    }

    @ApiOperation("喜欢")
    @GetMapping
    void like(@RequestParam("pid") String pictureId) {
        String userId = FansUtils.getUid();
        Likes likes = new Likes(userId, pictureId);
        notificationService.saveLike(userId, pictureId);
        likesService.save(likes);
    }

    @ApiOperation("不喜欢")
    @GetMapping("/unLike/{pid}")
    Likes unLike(@PathVariable("pid") String pictureId) {
        String uid = FansUtils.getUid();
        QueryWrapper likesWrapper = new QueryWrapper();
        likesWrapper.eq("user_id", uid);
        likesWrapper.eq("picture_id", pictureId);
        Likes like = likesService.getOne(likesWrapper);
        likesService.removeById(like.getId());
        return like;
    }
}

