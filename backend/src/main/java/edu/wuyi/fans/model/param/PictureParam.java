package edu.wuyi.fans.model.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/16
 */

@Data
public class PictureParam {
    @NotBlank(message = "标题不能为空")
    @Size(min = 2, max = 20, message = "标题长度需在2~20之间")
    private String title;

    private String summary;

    private String[] tags;

    @Min(value = 1L)
    private Integer category;
}
