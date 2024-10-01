package garry.train.member.config;

import garry.train.common.interceptor.LogIdInterceptor;
import garry.train.common.interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Garry
 * 2024-09-13 21:26
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private MemberInterceptor memberInterceptor;

    @Resource
    private LogIdInterceptor logIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logIdInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                );

        // 路径不要包含context-path(即 application.yml 中配置的，最前面的：/member)
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/hello",
                        "/member/register",
                        "/member/send-code",
                        "/member/login"
                        );
    }
}
