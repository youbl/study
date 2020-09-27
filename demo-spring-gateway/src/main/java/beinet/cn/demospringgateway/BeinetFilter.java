package beinet.cn.demospringgateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
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
            addTimeToRequestHeader(exchange, builder);
            builder.build();
        }).build())
                .then(Mono.fromRunnable(() -> addTimeToResponseHeader(exchange)));
    }

    private void addTimeToRequestHeader(ServerWebExchange exchange, ServerHttpRequest.Builder builder) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders(); // 读取Header，不需要转发，网关会自动转发

        String url = String.valueOf(request.getURI()); // 带有完整查询串
        // MultiValueMap<String, String> query = request.getQueryParams();
        builder.header("beinet.url", url);

        builder.header("beinet.request.now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void addTimeToResponseHeader(ServerWebExchange exchange) {
        // 收到响应后，往前端输出时，添加header
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.add("beinet.response.now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
