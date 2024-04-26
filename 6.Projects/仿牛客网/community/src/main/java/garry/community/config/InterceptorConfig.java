package garry.community.config;

import garry.community.interceptor.DataInterceptor;
import garry.community.interceptor.LoginRequiredInterceptor;
import garry.community.interceptor.LoginTicketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/19 10:01
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;

    @Resource
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Resource
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录凭证拦截器配置
        registry.addInterceptor(loginTicketInterceptor)
                //除了静态资源外，所有页面都要进行用户登录凭证的拦截，因为你要判断头部页面长什么样嘛
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/error"
                );

        //登录限制拦截器配置
        registry.addInterceptor(loginRequiredInterceptor)
                .addPathPatterns("/")
                .excludePathPatterns(
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/error",
                        "/**"
                );

        //数据统计拦截器配置
        registry.addInterceptor(dataInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/error"
                );
    }
}
