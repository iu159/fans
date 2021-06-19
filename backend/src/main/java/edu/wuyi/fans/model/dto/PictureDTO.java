package edu.wuyi.fans.model.dto;

import edu.wuyi.fans.model.entity.Picture;
import lombok.Data;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/18
 */

@Data
public class PictureDTO extends Picture {
    private List<UserDTO> likes;

    private UserDTO provider;

    private Boolean liked;
}
