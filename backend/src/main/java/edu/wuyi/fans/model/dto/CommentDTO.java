package edu.wuyi.fans.model.dto;

import edu.wuyi.fans.model.entity.Comment;
import lombok.Data;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/8
 */
@Data
public class CommentDTO  extends Comment {
    private List<CommentDTO> comments;

    private String username;

    private String profilePictureUrl;

    private String homeUrl;

    private Boolean inputShow = false;

}
