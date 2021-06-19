package edu.wuyi.fans.web.controller;

import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.exception.BadRequestException;
import edu.wuyi.fans.model.dto.NotificationDTO;
import edu.wuyi.fans.model.entity.Category;
import edu.wuyi.fans.model.entity.Feedback;
import edu.wuyi.fans.model.entity.Report;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.model.param.ReportParam;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.service.CategoryService;
import edu.wuyi.fans.service.FeedbackService;
import edu.wuyi.fans.service.NotificationService;
import edu.wuyi.fans.service.ReportService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/30
 */
@RestController
@Api(value = "整合接口", tags = "整合接口")
public class CommonController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReportService reportService;


    @ApiOperation("通知")
    @GetMapping("/notification")
    List<NotificationDTO> notification() {
        return notificationService.listNotifications(FansUtils.getUid());
    }

    @ApiOperation("通知")
    @GetMapping("/notificationSize")
    int notificationSize() {
        User user = ((User) SecurityUtils.getSubject().getPrincipal());
        if (ObjectUtils.isEmpty(user)) {
            return 0;
        }
        return notificationService.getNotificationSize(user.getUid());
    }

    @ApiOperation("分类")
    @GetMapping("/category")
    BaseResponse category() {
        List<Category> categories = categoryService.listCategory();
        return BaseResponse.ok(categories);
    }

    @ApiOperation("反馈")
    @PostMapping("/feedback")
    BaseResponse feedback(String contactInfo
            , String reason) {
        if (StringUtils.isEmpty(reason)) {
            throw new BadRequestException("理由不能为空");
        } else if (reason.length() > 255){
            throw new BadRequestException("理由长度最大为255");
        }
        Feedback feedback = new Feedback();
        feedback.setContactInfo(contactInfo);
        feedback.setReason(reason);
        feedback.setStatus(0);
        feedbackService.save(feedback);
        return BaseResponse.ok("反馈成功，请等待管理员处理");
    }

    @ApiOperation("举报")
    @PostMapping("/report")
    @FansLog(value = "举报", level = 2)
    BaseResponse report(@RequestBody ReportParam reportParam) {
        Report report = new Report();
        BeanUtils.copyProperties(reportParam, report);
        report.setAccuserId(FansUtils.getUid());
        report.setProgress(0);
        reportService.report(report);
        return BaseResponse.ok("举报成功，等待管理员处理");
    }
}
