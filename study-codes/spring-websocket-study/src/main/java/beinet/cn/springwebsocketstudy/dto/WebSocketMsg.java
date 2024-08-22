package beinet.cn.springwebsocketstudy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 自定义的WebSocket消息格式类
 * @author youbl
 * @since 2024/8/22 11:08
 */
@Data
@Accessors(chain = true)
public class WebSocketMsg {
    private String type;
    private String name;
    private String msg;
    private Long ts;
}
