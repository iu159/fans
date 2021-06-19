package edu.wuyi.fans.handler;

import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.model.entity.Log;
import edu.wuyi.fans.service.LogService;
import edu.wuyi.fans.util.FansUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;


/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/12/25
 */
@Component
@Aspect
public class FansLogAopAspect {
    @Autowired
    private LogService logService;

    @Pointcut("@annotation(edu.wuyi.fans.annotation.FansLog)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        this.savaLog(point);
        return result;
    }

    void savaLog(ProceedingJoinPoint point) {
        Log log = new Log();
        log.setUserId(FansUtils.getUidEvenNotLogin());
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        FansLog annotation = method.getAnnotation(FansLog.class);
        if (!ObjectUtils.isEmpty(annotation)) {
            log.setContent(annotation.value());
            log.setLevel(annotation.level());
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        log.setIp(request.getRemoteAddr());
        logService.save(log);
    }
}
