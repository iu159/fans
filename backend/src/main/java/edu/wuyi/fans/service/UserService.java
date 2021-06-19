package edu.wuyi.fans.service;

import edu.wuyi.fans.model.constant.LoginType;
import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.User;
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
public interface UserService extends IService<User> {
    void updateUser(User user);

    void checkUserIsRegister(User user, LoginType method);

    void sendRegisterMail(User user);

    void block(String accusedId);

    @Override
    boolean save(User user);

    String active(String cdkey);

    User getUser(String account);

    User getUserIgnoreStatus(String account);

    User getUserByUsernameIgnoreStatus(String username);

    UserDTO getUserInfo(String uid);

    LoginType checkLoginParam(String account);

    List<User> listUser();
}
