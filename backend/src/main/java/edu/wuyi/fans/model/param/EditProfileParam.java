package edu.wuyi.fans.model.param;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/3
 */
@Data
public class EditProfileParam {

    @Size(min = 2, max = 12, message = "用户名长度需在2~12之间")
    private String username;

    private String description;

    private String telephone;
}
