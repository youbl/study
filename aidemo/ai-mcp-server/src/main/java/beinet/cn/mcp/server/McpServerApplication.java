package beinet.cn.mcp.server;

import beinet.cn.mcp.server.services.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class McpServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        log.info("{} inited.", weatherService);
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }

    public void run(String... args) throws Exception {
        log.info("McpServerApplication starting...");
    }
}
