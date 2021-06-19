package edu.wuyi.fans.web.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.entity.Comment;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping()
    PageInfo<Comment> listComment(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        PageHelper.startPage(pageNo, WebConst.PAGE_COMM_SIZE);
        List<Comment> commentList = commentService.listCommentIgnoreStatus();
        return new PageInfo<>(commentList);
    }

    @DeleteMapping()
    void removeComment(Integer id){
        commentService.delete(commentService.getById(id));
    }
}
