package edu.wuyi.fans.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/18
 */
@Data
public class CommentParam {
    @NotBlank(message = "评论不能为空")
    private String content;

    @Size(min = 32, max = 32, message = "图片Id错误")
    private String pictureId;

    private Integer parentId;
}
