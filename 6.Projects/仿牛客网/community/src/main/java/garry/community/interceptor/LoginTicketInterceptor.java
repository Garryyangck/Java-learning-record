package garry.community.interceptor;

/**
 * @author Garry
 * ---------2024/3/18 21:14
 **/

import com.google.gson.Gson;
import garry.community.consts.CommunityConst;
import garry.community.enums.LoginTicketStatusEnum;
import garry.community.pojo.LoginTicket;
import garry.community.pojo.User;
import garry.community.service.MessageService;
import garry.community.service.UserService;
import garry.community.utils.CookieUtil;
import garry.community.utils.HostHolder;
import garry.community.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 该拦截器用于查询请求中是否携带有效的登录凭证，
 * 如果有凭证，则获取对应的User存入当前线程的ThreadLocalMap中，
 * 并在postHandle中将改User存入Controller被拦截的方法的modelAndView中，
 * 便于在templates的头部中显示
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private RedisTemplate redisTemplate;

    private final Gson gson = new Gson();

    /**
     * 查询登录凭证，判断凭证是否有效，从数据库中获取User。
     * <p>
     * 注意：从数据库中获取的User不是在该方法中使用，
     * 而是要在templates或Controller在处理业务时使用
     * 因此需要将User存到内存中以便被调用。
     * <p>
     * 但是服务器是并发处理多个用户的请求，对于每一个用户，
     * 服务器都会开辟一个独立的线程用于和该用户保持连接，
     * 因此必须分辨不同线程存储的User，实现线程间的隔离
     * <p>
     * 方法：使用ThreadLocal对象，
     * 该对象可以给调用它的每一个不同的线程，
     * 单独分配一个ThreadLocalMap用于set User。
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【登录凭证拦截器 请求路径 = {}】", request.getContextPath() + request.getServletPath());

        String ticket = CookieUtil.getCookieValue(request, CommunityConst.LOGIN_TICKET_COOKIE_NAME);
        //cookie存在且不为空
        if (!StringUtils.isBlank(ticket)) {
            String loginTicketKey = RedisKeyUtil.getLoginTicketKey(ticket);
            ValueOperations opsForValue = redisTemplate.opsForValue();
            String loginTicketJson = (String) opsForValue.get(loginTicketKey);
            //redis中的凭证不为空
            if (!StringUtils.isBlank(loginTicketJson)) {
                LoginTicket loginTicket = gson.fromJson(loginTicketJson, LoginTicket.class);
                //凭证有效
                if (loginTicket != null
                        && loginTicket.getStatus().equals(LoginTicketStatusEnum.EFFECTIVE.getCode())
                        && loginTicket.getExpired().after(new Date())) {
                    //凭证有效，获取user
                    User user = userService.findByUserId(loginTicket.getUserId());
                    //将该线程中，查询到的user存入ThreadLocal，实现线程间的隔离
                    hostHolder.setUser(user);

                    //构建用户认证的结果，并存入SecurityContext,以便于Security进行授权
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user, user.getPassword(), userService.getAuthorities(user.getId()));
                    //将认证结果存入SecurityContextHolder
                    SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
                }
            }
        }
        return true;
    }

    /**
     * 从hostHolder中获取该线程对应的User，存入modelAndView中
     * 注意：此处的moderAndView就是Controller被拦截的方法中的modelAndView(引用传递)
     * <p>
     * 不同的线程get存储在ThreadLocal中的User时，
     * ThreadLocal会先根据当前线程的名称，找出它们对应的ThreadLocalMap，
     * 然后从该ThreadLocalMap中查出正确的User对象
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            //传入登录用户
            modelAndView.addObject("loginUser", user);
            //查询用户未读消息总数
            int unreadMessageCount = 0;
            int unreadLettersCount = messageService.findUnreadLettersCount(user.getId(), null);
            int unreadNoticeCount = messageService.findUnreadNoticeCount(user.getId());
            unreadMessageCount = unreadLettersCount + unreadNoticeCount;
            //传入用户未读消息总数(未登录则为0)
            modelAndView.addObject("unreadMessageCount", unreadMessageCount);
        }
    }

    /**
     * 在整个请求结束后，清理hostHolder中的User
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.remove();
        SecurityContextHolder.clearContext();
    }
}
