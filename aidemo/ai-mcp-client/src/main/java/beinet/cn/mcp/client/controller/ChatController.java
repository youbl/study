package beinet.cn.mcp.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("chat")
    public Map<String, String> chat(@RequestParam String msg) {
        String response = chatClient.prompt().user(msg).call().content();
        assert response != null;
        return Map.of("response", response);
    }
}
