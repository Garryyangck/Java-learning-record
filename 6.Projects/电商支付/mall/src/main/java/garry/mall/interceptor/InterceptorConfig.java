package garry.mall.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Garry
 * ---------2024/3/8 13:43
 **/

/**
 * 配置拦截器的作用范围
 */
@SuppressWarnings("all")
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器，设置其拦截的范围
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(//排除
                        "/user/register",/*注册*/
                        "/user/login",/*登录*/
                        "/categories",/*多级目录查询*/
                        "/products",/*商品列表*/
                        "/products/*",/*商品详情*/
                        "/error"/*
                        在统一处理参数异常前，SpringBoot发现请求参数错误后，
                        会重定向到"/error"，因此拦截用户未登录请求需要将其排除在外*/
                        /*购物车模块的所有操作都需要登录拦截*/
                );
    }
}