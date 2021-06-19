package edu.wuyi.fans.model.dto;

import edu.wuyi.fans.model.entity.Report;
import lombok.Data;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/23
 */
@Data
public class ReportDTO extends Report {
    private UserDTO accuser;

    private UserDTO accused;
}
