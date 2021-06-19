package edu.wuyi.fans.model.param;

import edu.wuyi.fans.model.constant.WebConst;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/16
 */
@Data
public class RegisterParam {

    @Size(min = 2, max = 12, message = "用户名长度需在2~12之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 32, max = 32, message = "密码格式错误")
    private String password;

    @Pattern(regexp = WebConst.EMAIL_REGEX, message = "邮箱格式不符合规范")
    private String email;
}
