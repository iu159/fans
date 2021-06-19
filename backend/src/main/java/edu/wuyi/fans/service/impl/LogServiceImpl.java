package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.entity.Log;
import edu.wuyi.fans.mapper.LogMapper;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Autowired
    private LogMapper logMapper;
    @Override
    public Page<Log> listLogs(int pageNo) {
        Page<Log> logIPage = new Page<>(pageNo, WebConst.PAGE_COMM_SIZE);
        QueryWrapper logWrapper = new QueryWrapper();
        logWrapper.orderByDesc("create_time");
        return logMapper.selectPage(logIPage,logWrapper);
    }
}
