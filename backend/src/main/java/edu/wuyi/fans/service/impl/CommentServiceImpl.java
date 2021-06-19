package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.mapper.CommentMapper;
import edu.wuyi.fans.model.entity.Comment;
import edu.wuyi.fans.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> listComment(String pid) {
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("picture_id",pid);
        List<Comment> comments = commentMapper.selectByMap(columnMap);
        return comments;
    }

    @Override
    public void delete(Comment comment) {
        if (comment.getParentId() != null){
            commentMapper.deleteById(comment.getId());
        }
        deleteChilds(comment.getId());
        commentMapper.deleteById(comment.getId());
    }

    private void deleteChilds(Integer parentId) {
        QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
        commentWrapper.eq("parent_id",parentId);
        commentMapper.delete(commentWrapper);
    }

    @Override
    public List<Comment> listCommentIgnoreStatus() {
        return commentMapper.listCommentIgnoreStatus();
    }
}
