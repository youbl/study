package beinet.cn.github.oauth.config.log;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static feign.Util.decodeOrDefault;
import static feign.form.util.CharsetUtil.UTF_8;

/**
 * <a href="https://blog.csdn.net/youbl/article/details/109047987">...</a>
 *
 * @author youbl
 * @date 2023/3/6 9:45
 */
@Slf4j
public class MyFeignLogger extends Logger {
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        byte[] arrBody = request.body();
        String body = arrBody == null ? "" : new String(arrBody);
        log.info("[log request started]\n{} {}\nbody: {}\n{}",
                request.httpMethod(),
                request.url(),
                body,
                CombineHeaders(request.headers()));
    }

    @Override
    protected Response logAndRebufferResponse(String configKey,
                                              Level logLevel,
                                              Response response,
                                              long elapsedTime) {
        int status = response.status();

        String content = "";
        if (response.body() != null && !(status == 204 || status == 205)) {
            byte[] bodyData;
            try {
                bodyData = Util.toByteArray(response.body().asInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (bodyData.length > 0) {
                content = decodeOrDefault(bodyData, UTF_8, "Binary data");
            }
            response = response.toBuilder().body(bodyData).build();
        }

        log.info("[log request ended]\ncost time(ms): {} status:{} from {} {}\n{}\nBody:\n{}",
                elapsedTime,
                status,
                response.request().httpMethod(),
                response.request().url(),
                CombineHeaders(response.headers()),
                content);

        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        //log.info(String.format(methodTag(configKey) + format, args));
    }

    private static String CombineHeaders(Map<String, Collection<String>> headers) {
        StringBuilder sb = new StringBuilder();
        if (headers != null && !headers.isEmpty()) {
            sb.append("Headers:\r\n");
            for (Map.Entry<String, Collection<String>> ob : headers.entrySet()) {
                for (String val : ob.getValue()) {
                    sb.append("  ").append(ob.getKey()).append(": ").append(val).append("\r\n");
                }
            }
        }
        return sb.toString();
    }
}
