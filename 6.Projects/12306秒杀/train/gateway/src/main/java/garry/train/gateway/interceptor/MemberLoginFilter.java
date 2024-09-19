package garry.train.gateway.interceptor;

import cn.hutool.core.util.StrUtil;
import garry.train.gateway.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Garry
 * 2024-09-12 20:58
 */

/**
 * 登录校验拦截器
 */
@SuppressWarnings("LoggingSimilarMessage")
@Slf4j
@Component
public class MemberLoginFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("------------- 开始 {} -------------", path);

        // 排除不需要过滤的接口
        if (path.contains("/admin")
                || path.contains("/hello")
                || path.contains("/member/member/register")
                || path.contains("/member/member/send-code")
                || path.contains("/member/member/login")
                || path.contains("/business")) {
            log.info("{} 不需要登录", path);
        } else {
            String token = exchange.getRequest().getHeaders().getFirst("token");
            log.info("会员登录验证开始，token = {}", token);
            if (StrUtil.isBlank(token) || !JWTUtil.validate(token)) {
                log.info("token为空、无效或已过期");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("------------- 结束 {} -------------\n", path);
                return exchange.getResponse().setComplete();
            } else {
                log.info("登录校验通过");
            }
        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 在请求处理之后执行的逻辑
            log.info("------------- 结束 {} -------------\n", path);
        }));
    }

    @Override
    public int getOrder() {
        return -1; // 设置过滤器的优先级，数字越小优先级越高
    }
}
