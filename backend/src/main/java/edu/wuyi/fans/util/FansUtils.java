package edu.wuyi.fans.util;

import edu.wuyi.fans.exception.BadRequestException;
import edu.wuyi.fans.model.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.ObjectUtils;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/16
 */
public class FansUtils {
    public static String getUid(){
       User user = ((User) SecurityUtils.getSubject().getPrincipal());
       if (ObjectUtils.isEmpty(user)){
           throw new BadRequestException("清先登录");
       }
       return user.getUid();
    }

    public static  User getUser(){
        User user = ((User) SecurityUtils.getSubject().getPrincipal());
        if (ObjectUtils.isEmpty(user)){
            throw new BadRequestException("清先登录");
        }
        return user;
    }

    public static String getUidEvenNotLogin(){
        User user = ((User) SecurityUtils.getSubject().getPrincipal());
        if (ObjectUtils.isEmpty(user)){
            return null;
        }
        return user.getUid();
    }
}
