package edu.wuyi.fans.web.controller;


import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.exception.AuthenticationException;
import edu.wuyi.fans.model.dto.CommentDTO;
import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.Comment;
import edu.wuyi.fans.model.param.CommentParam;
import edu.wuyi.fans.service.CommentService;
import edu.wuyi.fans.service.NotificationService;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/comment")
@Api(value = "评论相关接口", tags = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;


    @GetMapping
    CommentDTO listComment(String pid){
        List<Comment> comments = commentService.listComment(pid);
        CommentDTO commentDTO = new CommentDTO();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        comments.forEach(
                comment -> {
                    UserDTO userInfo = userService.getUserInfo(comment.getUserId());
                    if (StringUtils.isEmpty(comment.getParentId())){
                        CommentDTO tempPCommentDTO = new CommentDTO();
                        BeanUtils.copyProperties(comment,tempPCommentDTO);
                        tempPCommentDTO.setComments(new ArrayList<CommentDTO>());
                        tempPCommentDTO.setUsername(userInfo.getUsername());
                        tempPCommentDTO.setProfilePictureUrl(userInfo.getProfilePictureUrl());
                        commentDTOList.add(tempPCommentDTO);
                    } else {
                        CommentDTO tempCommentDTO = new CommentDTO();
                        BeanUtils.copyProperties(comment,tempCommentDTO);
                        tempCommentDTO.setUsername(userInfo.getUsername());
                        tempCommentDTO.setProfilePictureUrl(userInfo.getProfilePictureUrl());
                        List<CommentDTO> collect = commentDTOList.stream().filter(commentDTO1 -> {
                            if (commentDTO1.getId().equals(tempCommentDTO.getParentId())) {
                                return true;
                            } else {
                                return false;
                            }
                        }).collect(Collectors.toList());
                        collect.get(0).getComments().add(tempCommentDTO);
                    }
                }
        );
        commentDTO.setComments(commentDTOList);
        return commentDTO;
    }

    @ApiOperation("删除评论")
    @DeleteMapping
    @FansLog(value = "删除评论", level = 3)
    void delete(String id){
        String uid = FansUtils.getUid();
        Comment comment = commentService.getById(id);
        if (!uid.equals(comment.getUserId())){
            throw new AuthenticationException("无法删除非本人评论");
        }
        commentService.delete(comment);
    }

    @ApiOperation("发布评论")
    @PostMapping()
    @FansLog("发布评论")
    void comment(@RequestBody CommentParam commentParam){
        String userId = FansUtils.getUid();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setIsDeleted(1);
        BeanUtils.copyProperties(commentParam,comment);
        commentService.save(comment);
        notificationService.saveComment(userId,commentParam);
    }
}


