package beinet.cn.mcp.client.config;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MCP Client初始化
 * @author youbl
 * @since 2025/4/21 13:40
 */
@Configuration
public class BeinetMcpClientConfiguration {

    @Bean
    ChatClient chatClient(OpenAiChatModel openAiChatModel, List<McpSyncClient> mcpSyncClients) {
        var mcpToolProvider = new SyncMcpToolCallbackProvider(mcpSyncClients);
        return ChatClient.builder(openAiChatModel).defaultTools(mcpToolProvider).build();
    }
}
