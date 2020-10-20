package beinet.cn.demologfeign.feign;

import feign.Client;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class MyClient extends Client.Default {
    public MyClient(SSLSocketFactory socketFactory, HostnameVerifier hostnameVerifier) {
        super(socketFactory, hostnameVerifier);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        StringBuilder sb = new StringBuilder("[log started]\r\n");
        sb.append(request.httpMethod()).append(" ").append(request.url()).append("\r\n");
        CombineHeaders(sb, request.headers()); // 请求Header
        CombineBody(sb, request.body());

        long costTime = -1;
        Exception exception = null;
        BufferingFeignClientResponse response = null;
        long begin = System.currentTimeMillis();
        try {
            response = new BufferingFeignClientResponse(super.execute(request, options));
            costTime = (System.currentTimeMillis() - begin);
        } catch (Exception exp) {
            costTime = (System.currentTimeMillis() - begin);
            exception = exp;
            throw exp;
        } finally {
            sb.append("\r\nResponse cost time(ms)： ").append(String.valueOf(costTime));
            if (response != null)
                sb.append("  status: ").append(response.status());
            sb.append("\r\n");
            if (response != null) {
                CombineHeaders(sb, response.headers()); // 响应Header
                sb.append("Body:\r\n").append(response.body()).append("\r\n");
            }
            if (exception != null) {
                sb.append("Exception:\r\n  ").append(exception.getMessage()).append("\r\n");
            }
            sb.append("\r\n[log ended]");
            log.debug(sb.toString());
        }

        Response ret = response.getResponse().toBuilder()
                .body(response.getBody(),
                        response.getResponse().body().length()).build();
        response.close();

        return ret;
    }

    private static void CombineHeaders(StringBuilder sb, Map<String, Collection<String>> headers) {
        if (headers != null && !headers.isEmpty()) {
            sb.append("Headers:\r\n");
            for (Map.Entry<String, Collection<String>> ob : headers.entrySet()) {
                for (String val : ob.getValue()) {
                    sb.append("  ").append(ob.getKey()).append(": ").append(val).append("\r\n");
                }
            }
        }
    }

    private static void CombineBody(StringBuilder sb, byte[] body) {
        if (body == null || body.length <= 0)
            return;
        sb.append("Body:\r\n").append(new String(body)).append("\r\n");
    }


    static final class BufferingFeignClientResponse implements Closeable {
        private Response response;
        private byte[] body;

        private BufferingFeignClientResponse(Response response) {
            this.response = response;
        }

        private Response getResponse() {
            return this.response;
        }

        private int status() {
            return this.response.status();
        }

        private Map<String, Collection<String>> headers() {
            return this.response.headers();
        }

        private String body() throws IOException {
            StringBuilder sb = new StringBuilder();
            try (InputStreamReader reader = new InputStreamReader(getBody())) {
                char[] tmp = new char[1024];
                int len;
                while ((len = reader.read(tmp, 0, tmp.length)) != -1) {
                    sb.append(new String(tmp, 0, len));
                }
            }
            return sb.toString();
        }

        private InputStream getBody() throws IOException {
            if (this.body == null) {
                this.body = StreamUtils.copyToByteArray(this.response.body().asInputStream());
            }
            return new ByteArrayInputStream(this.body);
        }

        @Override
        public void close() {
            this.response.close();
        }
    }
}
