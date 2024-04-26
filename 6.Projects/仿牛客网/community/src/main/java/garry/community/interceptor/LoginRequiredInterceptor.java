package garry.community.interceptor;

import garry.community.annotation.LoginRequired;
import garry.community.utils.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Garry
 * ---------2024/3/20 10:49
 **/

/**
 * 该拦截器用于拦截需要处于登录状态下才能访问的页面的请求
 */
@Slf4j
@Deprecated/*使用SpringSecurity进行权限管理和csrf预防*/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Resource
    private HostHolder hostHolder;

    /**
     * 在方法执行前，检验是否LoginRequired且用户未登录，
     * 如果是，则重定向到登录页面
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【登录限制拦截器 请求路径 = {}】", request.getContextPath() + request.getServletPath());

        //拦截到的是方法
        if (handler instanceof HandlerMethod) {
            //向下转型
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取Method对象
            Method method = handlerMethod.getMethod();
            //检查其是否含有LoginRequired注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //该非登录状态尝试访问LoginRequired的方法
            if (loginRequired != null && hostHolder.getUser() == null) {
                log.warn("【检测到非登录用户尝试访问登录限制网页 请求路径 = {}】", request.getContextPath() + request.getServletPath());

                //重定向到登录页面
                response.sendRedirect(request.getContextPath() + "/login");
                //终止访问
                return false;
            }
        }
        //不需要登录即可访问的方法 或 处于登录状态
        return true;
    }
}
