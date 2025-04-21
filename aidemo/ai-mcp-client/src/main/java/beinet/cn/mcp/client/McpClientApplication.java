package beinet.cn.mcp.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 参考官方说明：
 * <a href="https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html">...</a>
 * Claude申请APIKey需要先付5美元，申请url：<a href="https://console.anthropic.com/login?returnTo=%2F%3F">...</a>
 */
@SpringBootApplication
@Slf4j
public class McpClientApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class, args);
    }

    public void run(String... args) throws Exception {
        log.info("McpClientApplication starting...");
    }
}