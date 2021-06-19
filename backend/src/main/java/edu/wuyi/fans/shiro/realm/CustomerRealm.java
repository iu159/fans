package edu.wuyi.fans.shiro.realm;

import edu.wuyi.fans.model.constant.LoginType;
import edu.wuyi.fans.model.constant.UserStatus;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.entity.Role;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.service.RoleService;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.shiro.salt.FansByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class CustomerRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        if (!ObjectUtils.isEmpty(user)) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            List<Role> roles = roleService.listRolesByUid(user.getUid());
            List<String> rolesS = new ArrayList<>();
            roles.forEach(role -> rolesS.add(role.getType()));
            authorizationInfo.addRoles(rolesS);
            return authorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        User user = userService.getUser(principal);
        if (!ObjectUtils.isEmpty(user)) {
            return new SimpleAuthenticationInfo(user, user.getPassword(), new FansByteSource(WebConst.MD5_SALT), getName());
        }
        return null;
    }
}
