package edu.wuyi.fans.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/19
 */
@Data
public class ReportParam {
    @NotBlank
    private String type;

    @NotBlank
    private String targetId;

    @NotBlank
    private String accusedId;

    @NotBlank
    private String reason;
}
