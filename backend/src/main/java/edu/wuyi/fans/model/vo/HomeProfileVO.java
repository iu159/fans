package edu.wuyi.fans.model.vo;

import edu.wuyi.fans.model.dto.UserDTO;
import edu.wuyi.fans.model.entity.Picture;
import lombok.Data;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/1
 */
@Data
public class HomeProfileVO extends UserDTO {
    private List<UserDTO> fans;

    private List<UserDTO> followers;

    private List<Picture> myPictures;

    private Boolean isFollowed;
}
