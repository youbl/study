package beinet.cn.mcp.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 面向用户的API
 * @author youbl
 * @since 2025/4/21 11:57
 */
@RestController
@RequiredArgsConstructor
public class ChatController {

    // spring内置的client
    private final ChatClient chatClient;

    // 调用: http://localhost:8888/chat?msg=how%20is%20the%20weather%20in%20new%20york
    // 响应: {"response":"The weather in New York is currently sunny."}
    // 调用: http://localhost:8888/chat?msg=did%20any%20weather%20alerts%20in%20new%20york
    // 响应: {"response":"There are currently no weather alerts in New York. The report also mentions no significant wind conditions."}
    @GetMapping("chat")
    public Map<String, String> chat(@RequestParam String msg) {
        if (!StringUtils.hasText(msg)) {
            return new HashMap<>();
        }
        ChatClient.CallResponseSpec spec = chatClient.prompt().user(msg).call();
        String response = spec.content();
        assert response != null;
        return Map.of("response", response);
    }
}
