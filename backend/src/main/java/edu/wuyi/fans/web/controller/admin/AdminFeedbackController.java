package edu.wuyi.fans.web.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.wuyi.fans.model.entity.Feedback;
import edu.wuyi.fans.model.entity.Log;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
@RestController
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    Page<Feedback> listFeedback(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        return feedbackService.listFeedback(pageNo);
    }

    @PutMapping("/read")
    BaseResponse updateFeedback(Integer id){
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setStatus(1);
        feedbackService.updateById(feedback);
        return BaseResponse.ok("ok");
    }

    @PutMapping("/reply")
    BaseResponse updateFeedbackHandled(Integer id){
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setStatus(2);
        feedbackService.updateById(feedback);
        return BaseResponse.ok("ok");
    }
}
