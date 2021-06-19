package edu.wuyi.fans.service.impl;

import cn.hutool.core.util.IdUtil;
import edu.wuyi.fans.exception.AuthenticationException;
import edu.wuyi.fans.mapper.UserRoleMapper;
import edu.wuyi.fans.model.constant.RoleType;
import edu.wuyi.fans.model.entity.Permission;
import edu.wuyi.fans.model.entity.Role;
import edu.wuyi.fans.mapper.RoleMapper;
import edu.wuyi.fans.model.entity.UserRole;
import edu.wuyi.fans.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Role getRoleByPermissionId(int permissionId) {
        Role role = roleMapper.getRoleByPermissionId(permissionId);
        return role;
    }

    @Override
    public List<Role> listRolesByUid(String uid) {
        List<Role> roles = roleMapper.listRolesByUid(uid);
        if (roles.size() == 0){
            throw new AuthenticationException("当前用户无任何权限");
        }
        return roles;
    }

    @Override
    public void saveUserAsRegular(String uid) {
        UserRole userRole = new UserRole();
        userRole.setId(IdUtil.simpleUUID());
        userRole.setUserId(uid);
        userRole.setRoleId(RoleType.REGULAR.getStatus());
        userRoleMapper.insert(userRole);
    }

    @Override
    public void saveUserAsSupervisor(String uid) {
        UserRole userRole = new UserRole();
        userRole.setId(IdUtil.simpleUUID());
        userRole.setUserId(uid);
        userRole.setRoleId(RoleType.SUPERVISOR.getStatus());
        userRoleMapper.insert(userRole);
    }

    @Override
    public void saveUserAsAdmin(String uid) {
        UserRole userRole = new UserRole();
        userRole.setId(IdUtil.simpleUUID());
        userRole.setUserId(uid);
        userRole.setRoleId(RoleType.ADMIN.getStatus());
        userRoleMapper.insert(userRole);
    }


}
