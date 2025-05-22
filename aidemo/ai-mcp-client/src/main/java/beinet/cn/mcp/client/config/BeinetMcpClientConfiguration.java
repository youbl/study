package beinet.cn.mcp.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Client初始化
 * @author youbl
 * @since 2025/4/21 13:40
 */
@Configuration
public class BeinetMcpClientConfiguration {

//    @Bean
//    ChatClient chatClient(OpenAiChatModel openAiChatModel, List<McpSyncClient> mcpSyncClients) {
//        var mcpToolProvider = new SyncMcpToolCallbackProvider(mcpSyncClients);
//        return ChatClient.builder(openAiChatModel).defaultTools(mcpToolProvider).build();
//    }

    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                          ToolCallbackProvider tools) {
        return chatClientBuilder
                .defaultToolCallbacks(tools)
                .build();
    }
}
