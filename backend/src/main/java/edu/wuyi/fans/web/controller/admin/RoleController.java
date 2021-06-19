package edu.wuyi.fans.web.controller.admin;


import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.exception.NotFoundException;
import edu.wuyi.fans.model.constant.RoleType;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.service.RoleService;
import edu.wuyi.fans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("/assign/{roleType}/{uid}")
    @FansLog(value = "管理员分配权限", level = 3)
    BaseResponse assignRole(
            @PathVariable("roleType") String roleType
            ,@PathVariable("uid") String uid){
        //检查是否存在用户
        userService.getUser(uid);
        if (roleType.equals(RoleType.ADMIN.getStatus())){
            roleService.saveUserAsAdmin(uid);
        }else if (roleType.equals(RoleType.SUPERVISOR.getStatus())) {
            roleService.saveUserAsSupervisor(uid);
        }else {
            throw new NotFoundException("未知的用户类型");
        }
        return BaseResponse.ok("修改权限成功");
    }
}

