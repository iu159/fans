package edu.wuyi.fans.service;

import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.Likes;
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
public interface LikesService extends IService<Likes> {
    List<UserDTO> listLikes(String pictureId);

    List<Likes> listMyLikes(String userId);

    Boolean isLike(String pictureId, String userId);
}
