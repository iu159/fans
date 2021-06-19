package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.wuyi.fans.mapper.LogMapper;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.entity.Feedback;
import edu.wuyi.fans.mapper.FeedbackMapper;
import edu.wuyi.fans.model.entity.Log;
import edu.wuyi.fans.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public Page<Feedback> listFeedback(int pageNo) {
        Page<Feedback> logIPage = new Page<>(pageNo, WebConst.PAGE_COMM_SIZE);
        QueryWrapper feedbackWrapper = new QueryWrapper();
        feedbackWrapper.orderByAsc("status");
        feedbackWrapper.orderByDesc("create_time");
        return feedbackMapper.selectPage(logIPage, feedbackWrapper);
    }
}
