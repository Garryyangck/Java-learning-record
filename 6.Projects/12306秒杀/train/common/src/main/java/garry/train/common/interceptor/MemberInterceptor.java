package garry.train.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import garry.train.common.util.HostHolder;
import garry.train.common.util.JWTUtil;
import garry.train.common.vo.MemberLoginVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Garry
 * 2024-09-13 20:43
 */
@Slf4j
@Component
public class MemberInterceptor implements HandlerInterceptor {
    @Resource
    private HostHolder hostHolder;

    /**
     * 获取 request header 中的 token，由此获取 token 中的原始信息，保存到 hostHolder 中
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("------------- MemberInterceptor 开始 -------------");
        String path = request.getContextPath() + request.getServletPath();
        log.info("MemberInterceptor 拦截路径 = {}", path);
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            log.info("获取会员登录 token = {}", token);
            JSONObject loginMember = JWTUtil.getJSONObject(token);
            MemberLoginVo memberLoginVo = JSONUtil.toBean(loginMember, MemberLoginVo.class);
            memberLoginVo.setToken(token);
            log.info("当前登录会员：{}", memberLoginVo);
            hostHolder.setMember(memberLoginVo);
        } else {
            log.info("{} 的 token 不存在或已过期", path);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        hostHolder.remove();
    }
}
