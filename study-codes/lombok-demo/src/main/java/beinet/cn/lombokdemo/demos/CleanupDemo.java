package beinet.cn.lombokdemo.demos;

import lombok.Cleanup;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
public class CleanupDemo {

    @SneakyThrows
    public static void test(HttpServletResponse response) {
        @Cleanup InputStream inputStream = new ByteArrayInputStream("I'm a string".getBytes());
        @Cleanup OutputStream outputStream = response.getOutputStream();
        inputStream.transferTo(outputStream);
    }

    @SneakyThrows
    public static void test2(HttpServletResponse response) {
        @Cleanup OutputStream outputStream = response.getOutputStream();
        @Cleanup("doClear") var tmp = new CleanClass();// 默认要求注解的变量有close方法，可以修改
        outputStream.write(tmp.now.getBytes());
    }

    // 定义一个带资源清理的类
    public static class CleanClass {
        public String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        public void doClear() {
            now = null;
        }
    }
}
