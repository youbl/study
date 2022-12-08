package beinet.cn.frontstudy.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * 新类
 *
 * @author youbl
 * @date 2022/12/8 11:42
 */
@Slf4j
@RestController
public class S3Controller {
    // 下面4个参数，是上传到s3的必需配置
    private AmazonS3 client;
    private String accessKey = "我是ak";
    private String secretKey = "我是sk";
    private String region = "cn-northwest-1";// 对应endpoint参考： https://docs.amazonaws.cn/en_us/aws/latest/userguide/endpoints-Beijing.html
    private String bucket = "我是bucket";


    public S3Controller() {
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocol(Protocol.HTTPS);
        config.disableSocketProxy();

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.client = AmazonS3ClientBuilder
                .standard()
                .withClientConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region) // Regions.CN_NORTH_1.getName()
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .enablePathStyleAccess()
                .build();
    }

    // 调用方法 http://localhost:8801/s3/file?filePath=d:/xxx.jpg
    @GetMapping("s3/file")
    public String uploadFile(@RequestParam String filePath) {
        return uploadFileToS3(filePath, "abc/defFile.xxx", null);
    }

    // 调用方法 http://localhost:8801/s3/str?str=abcd%E6%88%91%E5%9C%A8%E6%98%AFfsldkl
    @GetMapping("s3/str")
    public String uploadStr(@RequestParam String str) {
        return uploadStrToS3(str, "abc/ghiStr.txt", null);
    }

    /**
     * 上传本地文件到aws s3
     *
     * @param filepath    本地文件路径
     * @param s3FileName  s3上的相对路径
     * @param contentType 内容
     * @return s3地址
     */
    public String uploadFileToS3(String filepath, String s3FileName, String contentType) {
        if (!StringUtils.hasLength(filepath)) {
            throw new RuntimeException("参数为空");
        }
        File file = new File(filepath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在:" + filepath);
        }
        try {
            try (InputStream input = new FileInputStream(file)) {
                return uploadStreamToS3(input, s3FileName, file.length(), contentType);
            }
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    /**
     * 上传字符串到aws s3
     *
     * @param fileContent s3的文件内容字符串
     * @param s3FileName  s3上的相对路径
     * @param contentType 内容
     * @return s3地址
     */
    public String uploadStrToS3(String fileContent, String s3FileName, String contentType) {
        try {
            if (!StringUtils.hasLength(fileContent)) {
                throw new RuntimeException("上传内容不能为空");
            }
            byte[] arrBytes = fileContent.getBytes();
            try (InputStream input = new ByteArrayInputStream(arrBytes)) {
                return uploadStreamToS3(input, s3FileName, arrBytes.length, contentType);
            }
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }


    public String uploadStreamToS3(InputStream stream, String s3FileName, long contentLength, String contentType) {
//        if (!StringUtils.hasLength(contentType)) {
//            contentType = "application/octet-stream";
//        }

        ObjectMetadata metadata = new ObjectMetadata();
        if (!StringUtils.hasLength(contentType)) {
            metadata.setContentType(contentType);
        }
        metadata.setContentLength(contentLength);
        PutObjectRequest request = new PutObjectRequest(bucket, s3FileName, stream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicReadWrite);
        client.putObject(request);
        URL url = client.getUrl(bucket, s3FileName);
        return url.toString();
    }

}
