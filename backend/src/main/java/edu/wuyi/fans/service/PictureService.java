package edu.wuyi.fans.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface PictureService extends IService<Picture> {

    void upload(MultipartFile image, Picture picture);

    void deleteById(String pid);

    void recoverPicture(String pid);

    String getUid(String pictureId);

    List<Picture> listMyPictures(String uid);

    List<Picture> listPictureByCategory(Integer categoryId);

    List<Picture> listPictureByRecommend();

    List<Picture> listPictureIgnoreStatus();

    List<Picture> listPictureBySearch(String keyword);

    Page<Picture> listPicture(int pageNo);

    Page<Picture> listPictureLatest(int pageNo);
}
