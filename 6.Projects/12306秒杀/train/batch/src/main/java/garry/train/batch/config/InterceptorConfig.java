package garry.train.batch.config;

import garry.train.common.interceptor.LogIdInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Garry
 * 2024-09-13 21:26
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private LogIdInterceptor logIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logIdInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                );
    }
}
