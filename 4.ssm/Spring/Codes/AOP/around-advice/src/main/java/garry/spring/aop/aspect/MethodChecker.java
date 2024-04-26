package garry.spring.aop.aspect;

/**
 * @author Garry
 * ---------2024/3/2 14:59
 **/

import org.aspectj.lang.ProceedingJoinPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 方法检查类，演示环绕通知(Around Aspect)
 */
public class MethodChecker {
    /**
     * ProceedingJoinPoint在保留JoinPoint原有功能的前提下，还能控制方法是否执行
     * 找出执行时间超过一秒的方法
     *
     * @param proceedingJoinPoint
     * @return
     */
    public Object check(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            long startTime = new Date().getTime();
            //控制目标方法是否执行，不写这句话，目标方法不会执行！
            Object ret = proceedingJoinPoint.proceed();
            long endTime = new Date().getTime();
            long duration = endTime - startTime;
            //调用时间超过1秒
            if (duration >= 1000) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");
                String now = dateFormat.format(new Date());

                String className = proceedingJoinPoint.getTarget().getClass().getName();
                String methodName = proceedingJoinPoint.getSignature().getName();
                System.out.println("----->" + now + ":" + className + "." + methodName + "(" + duration + ")");
            }
            return ret;
        } catch (Throwable throwable) {
            System.out.println("----->Exception Message:" + throwable.getMessage());
            throw throwable;
        }
    }
}
