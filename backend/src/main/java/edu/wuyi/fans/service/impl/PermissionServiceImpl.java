package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.mapper.RolePermissionMapper;
import edu.wuyi.fans.model.entity.Permission;
import edu.wuyi.fans.mapper.PermissionMapper;
import edu.wuyi.fans.model.entity.RolePermission;
import edu.wuyi.fans.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> listPermsByRoleId(Integer roleId) {
        List<Permission> permissions = permissionMapper.listPermsByRoleId(roleId);
        return permissions;
    }

    @Override
    public void savePermission(Permission permission, Integer roleId) {
        this.save(permission);
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("url",permission.getUrl());
        permission = permissionMapper.selectOne(permissionQueryWrapper);
        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermissionId(permission.getId());
        rolePermission.setRoleId(roleId);
        rolePermissionMapper.insert(rolePermission);
    }
}
