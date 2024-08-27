package beinet.cn.awss3demo.demos.configs;

import lombok.Data;

/**
 * 新类
 * @author youbl
 * @since 2024/8/27 19:52
 */
@Data
public class S3Property {
    private Boolean enabled;

    private String accessKey;

    private String accessSecret;

    private String bucket;

    private String endpoint;

    private String region;

    private String downUrl;
}
