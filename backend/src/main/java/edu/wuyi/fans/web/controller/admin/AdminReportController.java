package edu.wuyi.fans.web.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.dto.ReportDTO;
import edu.wuyi.fans.model.entity.Report;
import edu.wuyi.fans.model.param.ReportHandleParam;
import edu.wuyi.fans.service.NotificationService;
import edu.wuyi.fans.service.ReportService;
import edu.wuyi.fans.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
@RestController
@RequestMapping("/admin/report")
public class AdminReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    PageInfo<Report> listReport(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo){
        PageHelper.startPage(pageNo, WebConst.PAGE_COMM_SIZE);
        List<Report> reportList = reportService.list();
        return new PageInfo<>(reportList);
    }

    @GetMapping("/detail/{reportId}")
    ReportDTO getReport(@PathVariable("reportId") String reportId){
        Report report = reportService.getById(reportId);
        ReportDTO reportDTO = new ReportDTO();
        BeanUtils.copyProperties(report,reportDTO);
        reportDTO.setAccuser(userService.getUserInfo(report.getAccuserId()));
        reportDTO.setAccused(userService.getUserInfo(report.getAccuserId()));
        //更新状态
        report.setUpdateTime(null);
        report.setProgress(1);
        reportService.updateById(report);
        return reportDTO;
    }

    @PostMapping("/handle")
    @FansLog(value = "管理员处理举报", level = 2)
    void handleReport(@RequestBody ReportHandleParam reportHandleParam){
        notificationService.saveSystemNotification(reportHandleParam.getAccuserId(),reportHandleParam.getReply());
        if (reportHandleParam.getIsBlock()){
            userService.block(reportHandleParam.getAccusedId());
        }
        Report report = reportService.getById(reportHandleParam.getId());
        report.setUpdateTime(null);
        report.setProgress(2);
        reportService.updateById(report);
    }
}
