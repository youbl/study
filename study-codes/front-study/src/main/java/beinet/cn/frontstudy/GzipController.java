package beinet.cn.frontstudy;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

@RestController
public class GzipController {
    @PostMapping("gzip")
    public String gzip(@RequestBody byte[] bytes) throws IOException {
        String str = unGzip(bytes, "UTF-8");
        return str;
    }

    static String unGzip(byte[] bytes, String encoding) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             GZIPInputStream gis = new GZIPInputStream(in)) {
            byte[] buffer = new byte[256];
            int n;
            while ((n = gis.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        }
    }

}
