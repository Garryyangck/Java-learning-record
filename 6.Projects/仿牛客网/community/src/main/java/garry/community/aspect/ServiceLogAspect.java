package garry.community.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Garry
 * ---------2024/3/23 17:24
 **/
@Slf4j
@Component
@Aspect
public class ServiceLogAspect {

    //切点集：impl下所有类的所有方法
    @Pointcut("execution(* garry.community.service.impl.*.*(..))")
    public void pointcut() {
    }

    /**
     * 对所有Service层的方法进行环绕通知，记录用户的操作调用的Service层方法，
     * 将Service层的异常抛到Controller层，进行统一异常处理。
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("pointcut()")//环绕注解必须要有返回值！
    public Object serviceAround(ProceedingJoinPoint proceedingJoinPoint) {
        //获取用户ip地址
        String hostPath = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest(); // 故意没有考虑attributes为空，这样会产生异常，被捕获到从而打印日志
            hostPath = request.getRemoteHost();
        } catch (Exception e) {
            log.warn("【检测到Controller层以外的方法，尝试调用Service层的方法】");
        }
        //获取方法全名
        String declaringTypeName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String simpleMethodName = proceedingJoinPoint.getSignature().getName();
        String methodName = declaringTypeName + "." + simpleMethodName;
        //记录开始结束时间
        long before = System.currentTimeMillis();
        Object proceed = new Object();
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("【方法 {} 出现了错误】", methodName);
            //把Service层的所有错误抛给Controller层，由统一异常管理器(ControllerAdvice)处理
            throw new RuntimeException(throwable);
        }
        long after = System.currentTimeMillis();
        long executeTime = after - before;
        log.info("【用户[{}]调用 {}，用时{}ms】", hostPath, methodName, executeTime);
        //检测较慢的方法
        if (after - before >= 200) {
            log.warn("【方法：{} 运行时间({}ms)较长，可能需要优化】", methodName, executeTime);
        }
        return proceed;
    }
}
