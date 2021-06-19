package edu.wuyi.fans.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */

@Data
public class LoginParam {
    @NotBlank(message = "账号不能为空")
    @Size(min = 7,max = 64,message = "账户长度需在7~64之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 32, max = 32, message = "密码格式错误")
    private String password;

    /** 同时包含字母和数字，可以有字符 */
    //@Pattern(regexp = "(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$", message = "密码为6到20位，须同时包含数字以及字母")
}
