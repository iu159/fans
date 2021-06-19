package edu.wuyi.fans.model.param;

import lombok.Data;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/23
 */
@Data
public class ReportHandleParam {
    private Integer id;

    private String reply;

    private Boolean isBlock;

    private String accuserId;

    private String accusedId;
}
