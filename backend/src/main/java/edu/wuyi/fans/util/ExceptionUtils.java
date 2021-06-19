package edu.wuyi.fans.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class ExceptionUtils {
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
