package beinet.cn.java11newspecdemo;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 演示java9新增的HttpClient操作类
 *
 * @author youbl
 * @since 2023/6/7 17:05
 */
@RestController
public class HttpClientController {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @GetMapping(value = "HttpClientAsync", produces = "text/plain")
    public CompletableFuture<String> HttpClientAsyncTest() {
        return getContentAsync("https://www.baidu.com/");
    }

    @GetMapping(value = "HttpClient", produces = "text/plain")
    public String HttpClientSyncTest() {
        String content = getContentSync("https://www.baidu.com/");
        return content;
    }


    @SneakyThrows
    private CompletableFuture<String> getContentAsync(String url) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::getResponseTxt);
//                .thenAccept(str -> {
//            System.out.println(str);
//        });
    }

    @SneakyThrows
    private String getContentSync(String url) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return getResponseTxt(response);
    }

    private String getResponseTxt(HttpResponse<String> response) {
        var sb = new StringBuilder();
        sb.append("Response Status:")
                .append(response.statusCode())
                .append("\r\nResponse Headers:\r\n");
        for (Map.Entry entry : response.headers().map().entrySet()) {
            sb.append("  ")
                    .append(entry.getKey())
                    .append(":")
                    .append(String.join(",", (List<String>) entry.getValue())).append("\r\n");
        }
        sb.append("\r\nResponse Body:\r\n")
                .append(response.body());
        return sb.toString();
    }
}
