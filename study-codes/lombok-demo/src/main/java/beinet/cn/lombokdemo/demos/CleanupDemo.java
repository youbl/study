package beinet.cn.lombokdemo.demos;

import lombok.Cleanup;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
}
