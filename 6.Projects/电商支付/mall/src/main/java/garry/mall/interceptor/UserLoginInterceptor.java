package garry.mall.interceptor;

/**
 * @author Garry
 * ---------2024/3/8 13:35
 **/

import garry.mall.consts.MallConst;
import garry.mall.exception.UserLoginException;
import garry.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户登录请求拦截器（Interceptor只能拦截基于HTTP协议的请求）
 * 本质是AOP面向切面编程
 */
@SuppressWarnings("all")
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
     * true表示继续业务流程，false表示中断
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入用户登录拦截.....");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        if (user == null) {//说明用户处于未登录状态
            throw new UserLoginException("用户未登录异常");
        }
        return true;
    }
}
