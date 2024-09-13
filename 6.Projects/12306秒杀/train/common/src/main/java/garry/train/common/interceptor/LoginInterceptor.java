package garry.train.common.interceptor;

import garry.train.common.consts.CommonConst;
import garry.train.common.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Garry
 * 2024-09-13 20:43
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("------------- LoginInterceptor 开始 -------------");
        // MDC 是 Slf4j 自带的，用于存放我们自定义的键值对，比如在logback-spring.xml中的LOG_ID
        String logId = CommonUtil.generateUUID(CommonConst.LOG_ID_LENGTH);
        MDC.put("LOG_ID", logId);
        log.info("LOG_ID = {}", logId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
