package beinet.cn.demospringgateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BeinetFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().request(builder -> {
            //ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            addTimeToRequestHeader(builder);
            builder.build();
        }).build())
                .then(Mono.fromRunnable(() -> {
                    addTimeToResponseHeader(exchange);
                }));
    }

    private void addTimeToRequestHeader(ServerHttpRequest.Builder builder) {
        builder.header("beinet.request.now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void addTimeToResponseHeader(ServerWebExchange exchange) {
        // 收到响应后，往前端输出时，添加header
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.add("beinet.response.now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
