package edu.wuyi.fans.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.wuyi.fans.model.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface LogService extends IService<Log> {

    Page<Log> listLogs(int pageNo);
}
