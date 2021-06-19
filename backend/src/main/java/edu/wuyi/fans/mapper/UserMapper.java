package edu.wuyi.fans.mapper;

import edu.wuyi.fans.model.entity.Role;
import edu.wuyi.fans.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface UserMapper extends BaseMapper<User> {
    User getUserByIdIgnoreStatus(String uid);

    int updateUserStatusById(String uid, int status,Date updateTime);

    User getUserByEmailIgnoreStatus(String email);

    User getUserByTelIgnoreStatus(String telephone);

    User getUserByUsernameIgnoreStatus(String username);

    List<User> listUser();

    int updateUser(@Param("user") User user);
}
