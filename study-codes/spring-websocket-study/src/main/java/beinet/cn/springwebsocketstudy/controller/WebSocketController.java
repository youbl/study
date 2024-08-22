package beinet.cn.springwebsocketstudy.controller;

import beinet.cn.springwebsocketstudy.dto.WebSocketMsg;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * 新类
 * @author youbl
 * @since 2024/8/22 11:11
 */
@Controller
public class WebSocketController {

    // MessageMapping 消息接收路由
    @MessageMapping("/wkmsg")
    // 响应结果，发到哪里
    @SendTo("/topic/mymsg")
    @SneakyThrows
    public WebSocketMsg sendMsg(@NonNull WebSocketMsg msg) {
        Thread.sleep(1000);
        String retMsg = "Hello " + msg.getName() + ", I recieved your msg: " + msg.getMsg();
        return new WebSocketMsg()
                .setName(msg.getName())
                .setType("HELLO")
                .setMsg(retMsg)
                .setTs(System.currentTimeMillis());
    }
}
