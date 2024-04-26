package garry.community.config;

import com.google.gson.Gson;
import garry.community.enums.ResponseEnum;
import garry.community.enums.UserTypeEnum;
import garry.community.vo.ResponseVo;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Garry
 * ---------2024/3/28 15:59
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 忽略对静态资源的拦截
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    /**
     * 为需要权限的页面授权，并处理未登录和权限不足的逻辑
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
            授权
         */
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/post",
                        "/user/reply",
                        "/user/upload",
                        "/user/update/password",
                        "/user/update/header",
                        "/message/letter/**",
                        "/message/notice/**",
                        "/message/delete",
                        "/like",
                        "/follow",
                        "/unfollow",
                        "/post/add",
                        "/comment/add"
                )
                .hasAnyAuthority(
                        UserTypeEnum.USER.getDesc(),
                        UserTypeEnum.ADMIN.getDesc(),
                        UserTypeEnum.MODERATOR.getDesc()
                )
                .antMatchers(
                        "/post/top",
                        "/post/wonderful"
                ).hasAnyAuthority(
                UserTypeEnum.ADMIN.getDesc(),
                UserTypeEnum.MODERATOR.getDesc()
        )
                .antMatchers(
                        "/post/blacklist",
                        "/data/**",
                        "/actuator/**"
                ).hasAnyAuthority(
                UserTypeEnum.ADMIN.getDesc()
        )
                .anyRequest().permitAll();
        //.and().csrf().disable();//最后加上禁用csrf检查，也可以自行给所有涉及异步请求的页面手动加上csrf令牌

        /*
            权限不够时的处理
         */
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    /*
                        没有登录时的处理
                     */
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        //判断是同步请求还是异步请求
                        String xRequestedWith = request.getHeader("x-requested-with");
                        //如果是异步请求
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(new Gson().toJson(ResponseVo.error(ResponseEnum.NOT_LOGIN)));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    /*
                        权限不足时的处理
                     */
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        //判断是同步请求还是异步请求
                        String xRequestedWith = request.getHeader("x-requested-with");
                        //如果是异步请求
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(new Gson().toJson(ResponseVo.error(ResponseEnum.LACK_AUTHORITY)));
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        /*
            Security底层存在会自动拦截/logout的filter，并执行它的退出处理，发生在DispatcherServlet获取到请求之前，
            而我们自己实现的logout逻辑就不会被执行，因此我们必须覆盖它默认的逻辑。
            让Security底层默认拦截的路径从/logout变成根本不存在的/wrong/url，从而可以执行我们的业务逻辑
         */
        http.logout().logoutUrl("/wrong/url");
    }
}
