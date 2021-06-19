package edu.wuyi.fans.annotation;

import java.lang.annotation.*;
/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/25
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FansLog {
    String value();

    /** Level 1 info
     *  Level 2 warning
     *  Level 3 danger
     * */
    int level() default 1;
}
