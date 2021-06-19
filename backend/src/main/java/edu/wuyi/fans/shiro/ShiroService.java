package edu.wuyi.fans.shiro;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.model.entity.Permission;
import edu.wuyi.fans.model.entity.Role;
import edu.wuyi.fans.service.PermissionService;
import edu.wuyi.fans.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
@Service
public class ShiroService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    public Map<String, String> loadFilterChainDefinitions(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/user/**", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/kickout", "anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/admin/user/login", "anon");
        //filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        //指定按优先级降序排列
        QueryWrapper<Permission> permissionWrapper = new QueryWrapper<>();
        permissionWrapper.orderByDesc("priority");
        List<Permission> permissions = permissionService.list(permissionWrapper);
        permissions.forEach(
            permission -> {
                Role role = roleService.getRoleByPermissionId(permission.getId());
                filterChainDefinitionMap.put(permission.getUrl(),"roles["+role.getType()+"]");
            }
        );
        //filterChainDefinitionMap.put("/**","roles[admin]");
        filterChainDefinitionMap.put("/**","anon");
        return filterChainDefinitionMap;
    }


}
