package edu.wuyi.fans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.Likes;
import edu.wuyi.fans.mapper.LikesMapper;
import edu.wuyi.fans.service.LikesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
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
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements LikesService {

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<UserDTO> listLikes(String pictureId) {
        List<UserDTO> userDtoList = new ArrayList<>();
        QueryWrapper<Likes> likesWrapper = new QueryWrapper();
        likesWrapper.eq("picture_id",pictureId);
        List<Likes> likesList = likesMapper.selectList(likesWrapper);
        likesList.forEach(
            like ->{
                userDtoList.add(userService.getUserInfo(like.getUserId()));
            }
        );
        return userDtoList;
    }

    @Override
    public List<Likes> listMyLikes(String userId) {
        QueryWrapper likesWrapper = new QueryWrapper();
        likesWrapper.eq("user_id",userId);
        likesWrapper.orderByDesc("create_time");
        return likesMapper.selectList(likesWrapper);
    }

    @Override
    public Boolean isLike(String pictureId, String userId) {
        QueryWrapper likesWrapper = new QueryWrapper();
        likesWrapper.eq("picture_id",pictureId);
        likesWrapper.eq("user_id",userId);
        Likes like = likesMapper.selectOne(likesWrapper);
        if (ObjectUtils.isEmpty(like)){
            return false;
        }else {
            return true;
        }
    }
}
