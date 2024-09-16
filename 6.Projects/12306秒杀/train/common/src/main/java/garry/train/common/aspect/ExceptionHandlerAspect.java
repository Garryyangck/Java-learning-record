package garry.train.common.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Garry
 * 2024-09-16 20:17
 */

@Slf4j
@Aspect
@Component
public class ExceptionHandlerAspect {
    /**
     * 以 garry 包开始，controller 包结束，类名以 ExceptionHandler 结尾的所有类的所有公共方法
     */
    @Pointcut("execution(public * garry..*controller..*ExceptionHandler.*(..))")
    public void exceptionHandlerPointcut() {
    }

    @Around("exceptionHandlerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        log.info("返回结果: {}", JSONObject.toJSONString(result));
        log.info("------------- 产生异常结束 -------------\n");
        return result;
    }
}
