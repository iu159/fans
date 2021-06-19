package edu.wuyi.fans.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.exception.*;
import edu.wuyi.fans.mapper.UserMapper;
import edu.wuyi.fans.model.constant.LoginType;
import edu.wuyi.fans.model.constant.UserStatus;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.service.RoleService;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.util.RedisUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JavaMailSenderImpl sender;

    @Value("${fans.config.base-url}")
    private String BASE_URL;

    @Override
    public void checkUserIsRegister(User user, LoginType method) {
        if (LoginType.EMAIL.equals(method)){
            User emailIgnoreStatus = userMapper.getUserByEmailIgnoreStatus(user.getEmail());
            if (!ObjectUtils.isEmpty(emailIgnoreStatus)){
                throw new AlreadyExistsException("该邮箱已被注册！");
            }
        }
        if (LoginType.TELEPHONE.equals(method)){
            User telIgnoreStatus = userMapper.getUserByTelIgnoreStatus(user.getTelephone());
            if (!ObjectUtils.isEmpty(telIgnoreStatus)){
                throw new AlreadyExistsException("该号码已被注册！");
            }
        }
        User usernameIgnoreStatus = userMapper.getUserByUsernameIgnoreStatus(user.getUsername());
        if (!ObjectUtils.isEmpty(usernameIgnoreStatus)){
            throw  new AlreadyExistsException("账户名已被占用");
        }
    }

    @Override
    public LoginType checkLoginParam(String account) {
        if (ReUtil.isMatch(WebConst.TELEPHONE_REGEX, account)){
            return LoginType.TELEPHONE;
        } else if(ReUtil.isMatch(WebConst.EMAIL_REGEX,account)){
            return LoginType.EMAIL;
        } else {
            throw new BadRequestException("仅支持手机号或邮箱验证，请确认账号格式");
        }
    }

    @Override
    public void block(String accusedId) {
        userMapper.updateUserStatusById(accusedId,UserStatus.BLOCK.getStatus(),new Date());
    }

    @Override
    public User getUserByUsernameIgnoreStatus(String username) {
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username",username);
        return userMapper.getUserByUsernameIgnoreStatus(username);
    }

    @Override
    public List<User> listUser() {
        return userMapper.listUser();
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }


    @Override
    @Async
    public void sendRegisterMail(User user) {
        String cdkey = user.getUid() + IdUtil.simpleUUID();
        redisUtils.set("cdkey-"+cdkey,user.getEmail(),3600L);
        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("fans注册链接");

            helper.setText("尊敬的"+user.getUsername()+"<br>&nbsp;&nbsp;欢迎使用fans，<a href='"+BASE_URL+"user/active/"+cdkey+"'>"
                    +BASE_URL+"user/active/"+cdkey+"</a><br>&nbsp;&nbsp;激活码有效期60分钟，链接已过期？点击激活链接可再发送一条邮件！",true);
            //helper.addAttachment("1.jpg", ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"static/wall.png"));
            helper.setTo(user.getEmail());
            helper.setFrom("wuyi_edu@163.com");
            sender.send(message);
        } catch (MessagingException e) {
            System.out.println("发送失败");
            e.printStackTrace();
        }
    }

    @Override
    public String active(String cdkey) {
        boolean hasKey = redisUtils.hasKey("cdkey-" + cdkey);
        String uid = cdkey.substring(0,32);
        User user = userMapper.getUserByIdIgnoreStatus(uid);
        if (ObjectUtils.isEmpty(user)){
            throw new BadRequestException("无效的激活码");
        } else if (user.getUserStatus()==UserStatus.REGULAR.getStatus()){
            throw new AlreadyExistsException("账号已激活，请不要重新激活");
        } else if (user.getUserStatus()==UserStatus.BLOCK.getStatus()){
            throw new AuthenticationException("账号被封，请联系管理员");
        } else if (hasKey && user.getUserStatus()==UserStatus.UNACTIVATED.getStatus()){
            int isSuccess = userMapper.updateUserStatusById(uid, 1, new Date());
            return isSuccess==1?"激活成功":"激活失败";
        } else if (!hasKey && user.getUserStatus()==UserStatus.UNACTIVATED.getStatus()){
            this.sendRegisterMail(user);
            return "重新发送验证码";
        } else {
            throw new ServiceException("服务器异常");
        }
    }

    @Override
    public UserDTO getUserInfo(String uid) {
        User user = userMapper.selectById(uid);
        if (ObjectUtils.isEmpty(user)){
            return null;
        }
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(user,userDto);
        return userDto;
    }

    @Override
    public User getUser(String account) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (ReUtil.isMatch(WebConst.TELEPHONE_REGEX, account)) {
            userQueryWrapper.eq(LoginType.TELEPHONE.getMethod(),account);
        } else if (ReUtil.isMatch(WebConst.EMAIL_REGEX,account)){
            userQueryWrapper.eq(LoginType.EMAIL.getMethod(), account);
        } else if (account.length() ==32){
            return userMapper.selectById(account);
        } else {
            throw new BadRequestException("仅支持手机号或邮箱查询，请确认账号格式");
        }
        return userMapper.selectOne(userQueryWrapper);
    }

    @Override
    public User getUserIgnoreStatus(String account) {
        if (ReUtil.isMatch(WebConst.TELEPHONE_REGEX, account)) {
            return userMapper.getUserByTelIgnoreStatus(account);
        } else if (ReUtil.isMatch(WebConst.EMAIL_REGEX,account)){
            return userMapper.getUserByEmailIgnoreStatus(account);
        } else if (account.length() ==32){
           return userMapper.getUserByIdIgnoreStatus(account);
        } else {
            throw new BadRequestException("仅支持手机号或邮箱查询，请确认账号格式");
        }
    }

    @Override
    public boolean save(User user) {
        String uid = IdUtil.simpleUUID();
        user.setUid(uid);
        //user.setProfilePictureUrl("http://localhost:8888/profile-picture/fans.png");
        //服务器
        user.setProfilePictureUrl("http://116.62.65.154/img/my/fans.png");
        user.setUserStatus(0);
        String encryptPassword = new Md5Hash(user.getPassword(), WebConst.MD5_SALT, 1024).toHex();
        user.setPassword(encryptPassword);
        userMapper.insert(user);
        roleService.saveUserAsRegular(uid);
        if (!StringUtils.isEmpty(user.getEmail())){
            this.sendRegisterMail(user);
        }
        return true;
    }
}
