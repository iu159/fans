package edu.wuyi.fans.mapper;

import edu.wuyi.fans.model.entity.Picture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface PictureMapper extends BaseMapper<Picture> {

    List<Picture> listPictureIgnoreStatus();

    void recoverPicture(String pid);
}
