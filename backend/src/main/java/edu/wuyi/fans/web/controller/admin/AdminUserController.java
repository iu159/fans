package edu.wuyi.fans.web.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.dto.AdminUserDTO;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.model.param.LoginParam;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @PutMapping("/edit")
    @FansLog(value = "管理员修改用户", level = 3)
    BaseResponse editUser(@RequestBody User user){
        userService.updateUser(user);
        return BaseResponse.ok("ok");
    }

    @PutMapping("/block/{uid}")
    @FansLog(value = "管理员封禁账户", level = 3)
    User blockUser(@PathVariable("uid")String uid){
        userService.block(uid);
        return userService.getUserIgnoreStatus(uid);
    }

    @GetMapping("/listUser")
    PageInfo<User> listUser(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        PageHelper.startPage(pageNo, WebConst.PAGE_COMM_SIZE);
        List<User> userList = userService.listUser();
        return new PageInfo<>(userList);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    AdminUserDTO login(@RequestBody @Valid LoginParam loginParam) {
        userService.checkLoginParam(loginParam.getUsername());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), loginParam.getPassword());
        List<String> roles = new ArrayList<>();
        try {
            subject.login(token);
            roles.add("user");
        } catch (IncorrectCredentialsException e) {
            throw new edu.wuyi.fans.exception.AuthenticationException("密码错误");
        } catch (UnknownAccountException e) {
            throw new edu.wuyi.fans.exception.AuthenticationException("账号不存在，请先注册");
        } catch (AuthenticationException e) {
            throw new edu.wuyi.fans.exception.AuthenticationException("认证异常，请稍后再试");
        }
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        if (!isAdmin){
            throw new edu.wuyi.fans.exception.AuthenticationException("不是管理员");
        }
        roles.add("admin");
        boolean isSupervisor = SecurityUtils.getSubject().hasRole("supervisor");
        if (isSupervisor){
            roles.add("supervisor");
        }
        AdminUserDTO userDto = new AdminUserDTO();
        BeanUtils.copyProperties(FansUtils.getUser(), userDto);
        userDto.setRoles(roles);
        return userDto;
    }

    @GetMapping()
    String test(){
        Subject subject = SecurityUtils.getSubject();
        User principal = (User) subject.getPrincipal();
        System.out.println(principal);
        boolean user = subject.hasRole("user");
        System.out.println(user);
        boolean admin = subject.hasRole("admin");
        System.out.println(admin);
        boolean supervisor = subject.hasRole("supervisor");
        System.out.println(supervisor);
        return "lll";
    }
}
