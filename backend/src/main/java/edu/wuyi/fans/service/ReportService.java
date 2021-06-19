package edu.wuyi.fans.service;

import edu.wuyi.fans.model.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface ReportService extends IService<Report> {
    void report(Report report);
}
