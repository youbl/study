package beinet.cn.mcp.server.services;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * 新类
 * @author youbl
 * @since 2025/4/17 20:58
 */
@Service
public class WeatherService {
    private final RestClient restClient;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.weather.gov")
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build();
    }

    @Tool(description = "Get weather forecast for a specific latitude,longitude")
    public String getWeatherForecastByLocation(
            double latitude,   // 纬度坐标
            double longitude   // 经度坐标
    ) {
        // 返回详细预报，包括：
        // - 温度和单位
        // - 风速和方向
        // - 详细预报描述
        return latitude + ":" + longitude + ", the weather is sunny.";
    }

    @Tool(description = "Get weather alerts for states in the United States")
    public String getAlerts(
            @ToolParam(description = "Two letter US state code (e.g. CA, NY)") String state) {
        // 返回活跃警报，包括：
        // - 事件类型
        // - 受影响区域
        // - 严重性
        // - 描述
        // - 安全说明
        return state + " no alert, and any wind here.";
    }
}
