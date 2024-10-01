package garry.train.common.aspect;

/**
 * @author Garry
 * 2024-09-15 17:21
 */

import cn.hutool.core.util.ObjectUtil;
import garry.train.common.form.PageForm;
import garry.train.common.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 拦截 service 层所有涉及到分类的方法
 */
@Slf4j
@Aspect
@Component
public class PageAspect {

    /**
     * 在包 garry 及其子包中，
     * 类名包含 Service 的所有类中的所有公共方法，
     * 这些方法的返回值必须是 garry.train.common.vo.PageVo 类型，
     * 无论它们的方法名和参数是什么。
     */
    @Pointcut("execution(public garry.train.common.vo.PageVo garry..*Service*.*(..))")
    public void pageServicePointcut() {
    }

    @Around("pageServicePointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        PageForm form = null;
        for (Object arg : args) {
            if (arg instanceof PageForm) {
                form = (PageForm) arg;
                break;
            }
        }
        if (ObjectUtil.isNotNull(form)) {
            log.info("查询页码: {}", form.getPageNum());
            log.info("每页条数: {}", form.getPageSize());
        }

        Object result = proceedingJoinPoint.proceed();

        if (result instanceof PageVo) {
            PageVo vo = (PageVo) result;
            log.info("总行数: {}", vo.getSize());
            log.info("总页数: {}", vo.getPages());
            log.info("总记录数: {}", vo.getTotal());
        }

        return result;
    }
}
