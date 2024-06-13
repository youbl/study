package beinet.cn.awss3demo.demos.web.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * 新类
 *
 * @author youbl
 * @since 2024/3/25 13:48
 */
@Data
@Accessors(chain = true)
public class StoreInfo {
    private String key;
    private Instant lastModified;
    private boolean dir;
    private long size;
    private String etag;
    private String url;
}
