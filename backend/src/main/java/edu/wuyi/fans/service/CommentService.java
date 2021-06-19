package edu.wuyi.fans.service;

import edu.wuyi.fans.model.entity.Comment;
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
public interface CommentService extends IService<Comment> {
    void delete(Comment comment);

    List<Comment> listComment(String pid);

    List<Comment> listCommentIgnoreStatus();
}
