package edu.wuyi.fans.service;

import edu.wuyi.fans.model.entity.Permission;
import edu.wuyi.fans.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface RoleService extends IService<Role> {
    void saveUserAsRegular(String uid);

    void saveUserAsSupervisor(String uid);

    void saveUserAsAdmin(String uid);

    Role getRoleByPermissionId(int permissionId);

    List<Role> listRolesByUid(String uid);
}
