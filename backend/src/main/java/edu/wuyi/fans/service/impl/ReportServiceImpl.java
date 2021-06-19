package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.model.entity.Report;
import edu.wuyi.fans.mapper.ReportMapper;
import edu.wuyi.fans.service.ReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserService userService;


    @Override
    public void report(Report report) {
        reportMapper.insert(report);
        String accusedId = report.getAccusedId();
        QueryWrapper<Report> reportWrapper = new QueryWrapper<>();
        reportWrapper.eq("accused_id",accusedId);
        reportWrapper.eq("progress",0);
        Integer count = reportMapper.selectCount(reportWrapper);
        if (count >= 3){
            userService.block(accusedId);
        }
    }
}
