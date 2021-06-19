package edu.wuyi.fans.model.param;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/14
 */
@Data
public class PictureESParam {
    private String pid;

    private String userId;

    private String username;

    private String title;

    private String summary;

    private String tags;

    private String category;
}
