package garry.train.batch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import garry.train.common.interceptor.LogIdInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Garry
 * 2024-09-13 21:26
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LogIdInterceptor logIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logIdInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                );
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(new ObjectMapper()));
    }
}
