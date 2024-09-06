package garry.train.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import garry.train.common.consts.CommonConst;
import garry.train.common.util.CommonUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Aspect
@Component
public class LogAspect {
    private final String[] excludeProperties = {};

    public LogAspect() {
        log.info("Common LogAspect");
    }

    /**
     * 定义一个切点
     * *: 所有的返回值
     * garry: garry下的所有子包
     * ..*Controller: 结尾为Controller的所有类
     * .*: 这些类下的任何方法
     * (..): 任何返回值
     */
    @Pointcut("execution(public * garry..*Controller.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 前置通知
     */
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {

        // MDC 是 Slf4j 自带的，用于存放我们自定义的键值对，比如在logback-spring.xml中的LOG_ID
        MDC.put("LOG_ID", CommonUtil.generateUUID(CommonConst.LOG_ID_LENGTH));

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印请求信息
        log.info("------------- 开始 -------------");
        log.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        log.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        log.info("远程地址: {}", request.getRemoteAddr());

        // 打印请求参数
        Object[] args = joinPoint.getArgs();

        // 排除特殊类型的参数，如文件类型
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        log.info("请求参数: {}", JSONObject.toJSONString(arguments, excludeFilter));
    }

    /**
     * 环绕通知
     */
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        log.info("返回结果: {}", JSONObject.toJSONString(result, excludeFilter));
        log.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }

}
