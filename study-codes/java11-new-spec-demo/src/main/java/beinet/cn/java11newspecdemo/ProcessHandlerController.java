package beinet.cn.java11newspecdemo;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.time.Instant;

/**
 * 演示java9新增的ProcessHandle信息操作类
 *
 * @author youbl
 * @since 2023/6/7 17:05
 */
@RestController
public class ProcessHandlerController {

    @GetMapping(value = "ProcessHandler", produces = "text/plain")
    @SneakyThrows
    public String ProcessHandlerTest() {
        var process = new ProcessBuilder("cmd", "/c", "dir /w").start();
        var output = new String(process.getInputStream().readAllBytes(), Charset.forName("GBK"));//StandardCharsets.UTF_8);

        var sb = new StringBuilder();
        // Java9增加的方法
        var handle = process.toHandle();
        sb.append(handle.pid());

        var info = handle.info();
        sb.append("\nStartTime: ")
                .append(info.startInstant().orElse(Instant.now()))
                .append("\r\nArguments: ")
                .append(String.join(",", info.arguments().orElse(new String[0])))
                .append("\r\n\r\n输出内容: ")
                .append(output);

        return sb.toString();
    }

    // Java9中， InputStream新增的transferTo方法，可以直接把输入流转发到输出流，而无需自行处理
    @GetMapping(value = "ProcessHandler2", produces = "text/plain")
    @SneakyThrows
    public void ProcessHandlerTest2(HttpServletResponse response) {
        var process = new ProcessBuilder("cmd", "/c", "dir /w").start();
        process.getInputStream().transferTo(response.getOutputStream());
    }
}
