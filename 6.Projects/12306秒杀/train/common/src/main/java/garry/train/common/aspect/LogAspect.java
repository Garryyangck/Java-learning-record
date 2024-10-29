package garry.train.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import garry.train.common.util.ApiDetail;
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
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Aspect
@Component
public class LogAspect {
    private final String[] excludeProperties = {};

    /**
     * 定义一个切点
     * *: 所有的返回值
     * garry: garry下的所有子包
     * ..*Controller: 结尾为Controller的所有类
     * .*: 这些类下的任何方法
     * (..): 任何数量和类型的参数
     */
    @Pointcut("execution(public * garry..*Controller.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 前置通知
     */
    @SuppressWarnings("DuplicatedCode")
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) throws URISyntaxException {

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印请求信息
        log.info("------------- LogAspect 开始 -------------");
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

        // success = false 的 apiDetail 插入
        ServletRequestAttributes _attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest _request = _attributes.getRequest();
        String fullApiPath = new URI(_request.getRequestURL().toString()).getPath();
        String apiMethod = _request.getMethod();
        String moduleName = fullApiPath.split("/")[1];
        fullApiPath = handlePathVariable(joinPoint, fullApiPath);
        synchronized (this) { // 防止并发线程，修改 ApiDetail 导致调用次数不准确，因此在修改 ApiDetail 的时候加 JDK 锁
            ApiDetail.putApiDetails(fullApiPath, apiMethod, moduleName, 0L, false);
        }
    }

    /**
     * 环绕通知
     */
    @SuppressWarnings("DuplicatedCode")
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        log.info("返回结果: {}", JSONObject.toJSONString(result, excludeFilter));
        long executeMills = System.currentTimeMillis() - startTime;
        log.info("------------- 结束 耗时：{} ms -------------\n", executeMills);

        // success = true 的 apiDetail 插入
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String fullApiPath = new URI(request.getRequestURL().toString()).getPath();
        // 检查参数是否右 @PathVariable 注解
        String apiMethod = request.getMethod();
        String moduleName = fullApiPath.split("/")[1];
        Long mills = executeMills;
        fullApiPath = handlePathVariable(proceedingJoinPoint, fullApiPath);
        synchronized (this) { // 防止并发线程，修改 ApiDetail 导致调用次数不准确，因此在修改 ApiDetail 的时候加 JDK 锁
            ApiDetail.putApiDetails(fullApiPath, apiMethod, moduleName, mills, true);
        }

        return result;
    }

    private static String handlePathVariable(JoinPoint joinPoint, String fullApiPath) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            Annotation[] annotations = parameter.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == PathVariable.class) {
                    // 去掉最后一个 /xx
                    fullApiPath = fullApiPath.substring(0, fullApiPath.lastIndexOf("/"));
                }
            }
        }
        return fullApiPath;
    }
}
