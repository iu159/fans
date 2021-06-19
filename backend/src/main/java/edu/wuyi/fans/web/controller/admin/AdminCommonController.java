package edu.wuyi.fans.web.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.wuyi.fans.model.entity.Log;
import edu.wuyi.fans.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/25
 */
@RestController
@RequestMapping("/admin/common")
public class AdminCommonController {
    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    Page<Log> listLogs(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        return logService.listLogs(pageNo);
    }
}
