package edu.wuyi.fans.web.controller.admin;


import edu.wuyi.fans.model.entity.Permission;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/admin/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/savePermission")
    BaseResponse savePermission(@Valid Permission permission,Integer roleId){
        permissionService.savePermission(permission,roleId);
        return BaseResponse.ok("保存成功");
    }

    @GetMapping("/listPerms/{roleId}")
    BaseResponse listPermsByRoleId(@PathVariable("roleId") Integer roleId){
        List<Permission> permissions = permissionService.listPermsByRoleId(roleId);
        return BaseResponse.ok("查询成功",permissions);
    }
}

