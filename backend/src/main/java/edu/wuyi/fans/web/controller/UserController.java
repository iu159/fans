package edu.wuyi.fans.web.controller;


import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ReUtil;
import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.exception.AlreadyExistsException;
import edu.wuyi.fans.exception.BadRequestException;
import edu.wuyi.fans.exception.ServiceException;
import edu.wuyi.fans.model.constant.LoginType;
import edu.wuyi.fans.model.constant.UserStatus;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.model.param.EditProfileParam;
import edu.wuyi.fans.model.param.LoginParam;
import edu.wuyi.fans.model.param.RegisterParam;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.model.vo.HomeProfileVO;
import edu.wuyi.fans.service.FollowService;
import edu.wuyi.fans.service.PictureService;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "用户相关接口", tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private PictureService pictureService;

    @Value("${fans.config.upload-path}")
    private String UPLOAD_PATH;

    @Value("${fans.config.base-url}")
    private String BASE_URL;


    @GetMapping("/countUser")
    Integer countUser() {
        return userService.count();
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    BaseResponse logout() {
        SecurityUtils.getSubject().logout();
        return BaseResponse.ok("成功退出");
    }

    @ApiOperation("修改个人头像")
    @PostMapping("/profile")
    @FansLog(value = "修改个人头像", level = 2)
    void updateProfilePicture(@RequestPart MultipartFile profilePicture) {
        User user = FansUtils.getUser();
        String extName = FileNameUtil.extName(profilePicture.getOriginalFilename());
        String fileName = user.getUid() + "." + extName;
        String filePath = UPLOAD_PATH + "profile-picture/";
        File dest = new File(filePath, fileName);
        try {
            profilePicture.transferTo(dest);
            user.setProfilePictureUrl(BASE_URL + "profile-picture/" + fileName);
            userService.updateById(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("修改个人信息")
    @PutMapping("/profile")
    @FansLog(value = "修改个人信息", level = 2)
    BaseResponse updateProfile(@RequestBody EditProfileParam editProfileParam) {
        User user = FansUtils.getUser();
        User isExist = userService.getUserByUsernameIgnoreStatus(editProfileParam.getUsername());
        String telephone = editProfileParam.getTelephone();
        if (!StringUtils.isEmpty(telephone)) {
            if (!ReUtil.isMatch("^1[3-9][0-9]{9}$", telephone)) {
                throw new BadRequestException("手机号格式有误");
            }
            isExist = userService.getUserIgnoreStatus(telephone);
            if (!ObjectUtils.isEmpty(isExist) && !user.equals(isExist)) {
                throw new AlreadyExistsException("手机号码已被占用，请重新选择");
            }
        }
        if (!ObjectUtils.isEmpty(isExist) && !user.equals(isExist)) {
            throw new AlreadyExistsException("用户名已被占用，请重新选择");
        }
        BeanUtils.copyProperties(editProfileParam, user);
        userService.updateById(user);
        return BaseResponse.ok("修改用户信息成功");
    }


    @ApiOperation("获取个人主页信息")
    @GetMapping("/profile/{uid}")
    HomeProfileVO getProfile(@PathVariable String uid) {
        User user = userService.getUser(uid);
        HomeProfileVO homeProfileVO = new HomeProfileVO();
        BeanUtils.copyProperties(user, homeProfileVO);
        homeProfileVO.setFans(followService.myFans(uid).stream().filter(fan ->
                !ObjectUtils.isEmpty(fan)
        ).collect(Collectors.toList()));
        homeProfileVO.setFollowers(followService.myFollowers(uid).stream().filter(follow ->
                !ObjectUtils.isEmpty(follow)
        ).collect(Collectors.toList()));
        homeProfileVO.setIsFollowed(followService.isFollowed(uid));
        homeProfileVO.setMyPictures(pictureService.listMyPictures(user.getUid()));
        return homeProfileVO;
    }

    @ApiOperation("修改密码")
    @PutMapping("/profile/password")
    @FansLog(value = "修改密码", level = 2)
    BaseResponse updatePassword(String oldPassword, String newPassword) {
        User user = FansUtils.getUser();
        String oldEncryptPassword = new Md5Hash(oldPassword, WebConst.MD5_SALT, 1024).toHex();
        String newEncryptPassword = new Md5Hash(newPassword, WebConst.MD5_SALT, 1024).toHex();
        if (user.getPassword().equals(oldEncryptPassword)) {
            user.setPassword(newEncryptPassword);
            userService.updateById(user);
            return BaseResponse.ok("修改密码成功");
        } else {
            throw new BadRequestException("请求出错");
        }
    }

    @ApiOperation("获取登录信息")
    @GetMapping("/login")
    UserDTO login() {
        User user = ((User) SecurityUtils.getSubject().getPrincipal());
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(user, userDto);
        user.setUpdateTime(null);
        userService.updateById(user);
        return userDto;
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    @FansLog("登录")
    UserDTO login(@Valid LoginParam loginParam) {
        userService.checkLoginParam(loginParam.getUsername());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginParam.getUsername(), loginParam.getPassword());
        User userIgnoreStatus = userService.getUserIgnoreStatus(loginParam.getUsername());
        if (ObjectUtils.isEmpty(userIgnoreStatus)) {
            throw new edu.wuyi.fans.exception.AuthenticationException("账号不存在，请先注册");
        } else if (!userIgnoreStatus.getPassword().equals(new Md5Hash(loginParam.getPassword(), WebConst.MD5_SALT, 1024).toHex())) {
            throw new edu.wuyi.fans.exception.AuthenticationException("密码错误");
        } else if (userIgnoreStatus.getUserStatus() == UserStatus.BLOCK.getStatus()) {
            throw new edu.wuyi.fans.exception.AuthenticationException("账号状态异常，请尝试联系管理员");
        } else if (userIgnoreStatus.getUserStatus() == UserStatus.UNACTIVATED.getStatus()) {
            throw new edu.wuyi.fans.exception.AuthenticationException("请先激活账号");
        }
        try {
            subject.login(token);
            UserDTO userDto = new UserDTO();
            BeanUtils.copyProperties(userIgnoreStatus, userDto);
            return userDto;
        } catch (IncorrectCredentialsException e) {
            throw new edu.wuyi.fans.exception.AuthenticationException("密码错误");
        } catch (UnknownAccountException e) {
            throw new edu.wuyi.fans.exception.AuthenticationException("账号不存在，请先注册");
        } catch (AuthenticationException e) {
            throw new AuthenticationException("系统异常, 请联系管理员");
        }
    }


    @ApiOperation("激活")
    @GetMapping("/active/{cdkey}")
    BaseResponse active(@PathVariable("cdkey") String cdkey) {
        String active = userService.active(cdkey);
        return BaseResponse.ok(active);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    BaseResponse register(@RequestBody @Valid RegisterParam registerParam) {
        User user = new User();
        BeanUtils.copyProperties(registerParam, user);
        //判断用户注册类型，手机号码或邮箱
        String account = StringUtils.isEmpty(user.getEmail()) ? user.getTelephone() : user.getEmail();
        LoginType method = userService.checkLoginParam(account);
        userService.checkUserIsRegister(user, method);
        boolean save = userService.save(user);
        if (save) {
            return BaseResponse.ok("注册成功，请于一小时内点击邮箱注册链接完成注册");
        } else {
            throw new ServiceException("服务器异常，请稍后再试");
        }
    }

    @GetMapping("/unLogin")
    void unLogin() {
        throw new edu.wuyi.fans.exception.AuthenticationException("请先登录后再进行操作");
    }

    @GetMapping("/unAuth")
    void unAuth() {
        throw new edu.wuyi.fans.exception.AuthenticationException("账号权限不足，请联系管理员");
    }
}

