package beinet.cn.frontstudy.oss;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.joda.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传Oss的类
 *
 * @author youbl
 * @date 2023/06/28 12:42
 */
@Slf4j
@RestController
public class OssController {
    private OSS client;

    // 下面4个参数，是上传到s3的必需配置
    /*
     注意：绝对不要把ak sk写在代码里，或写在配置里，泄露会导致oss数据泄露，被删除，被占用等不可预知的后果
     建议：
     1、安全性较低：加密后写入配置文件，代码里解密，参考： https://youbl.blog.csdn.net/article/details/122603550
     2、安全性较高：由运维在服务器上配置环境变量，程序中读取环境变量使用
    */
    private String accessKey = "I'm ak";
    private String secretKey = "I'm sk";
    private String region = "oss-cn-shenzhen";// 常用Region参考： https://help.aliyun.com/document_detail/140601.html
    private String endpoint = "https://" + region + ".aliyuncs.com";
    private String bucket = "my-bucket";

    @SneakyThrows
    public OssController() {
        this.client = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
    }

    /**
     * 生成一个预签名的url，给前端js上传
     * 参考官网文档： https://help.aliyun.com/document_detail/32016.html?
     *
     * @param ossFileName 上传到oss的文件相对路径
     * @return 签名后的url
     */
    @GetMapping("oss/sign")
    public String preUploadFile(@RequestParam String ossFileName, @RequestParam String contentType) {
        // token设置1小时后过期
        Date expiration = LocalDateTime.now().plusHours(1).toDate();

        // 设置请求头。
        Map<String, String> headers = new HashMap<String, String>();
        // 指定ContentType，注意：必须指定，这个header加入签名了，不指定时前端带Content-Type上传，会导致签名验证不通过
        headers.put(OSSHeaders.CONTENT_TYPE, contentType);

        // 设置用户自定义元信息。
        Map<String, String> userMetadata = new HashMap<String, String>();
        /*userMetadata.put("key1","value1");
        userMetadata.put("key2","value2");*/

        // 生成签名URL。
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, ossFileName, HttpMethod.PUT);
        // 设置过期时间。
        request.setExpiration(expiration);

        // 将请求头加入到request中。
        request.setHeaders(headers);
        // 添加用户自定义元信息。
        //request.setUserMetadata(userMetadata);

        URL url = client.generatePresignedUrl(request);
        log.info("{}", url);
        // java调用时，能正常上传
        // putObjectWithHttp(url.toString(), "d:/mine/index.txt", headers, userMetadata);
        return url.toString();
    }

    @SneakyThrows
    public static void putObjectWithHttp(String strSignUrl, String pathName, Map<String, String> headers, Map<String, String> userMetadata) {
        URL signedUrl = new URL(strSignUrl);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPut put = new HttpPut(signedUrl.toString());
            HttpEntity entity = new FileEntity(new File(pathName));
            put.setEntity(entity);
//            // 如果生成签名URL时设置了header参数，例如用户元数据，存储类型等，则调用签名URL上传文件时，也需要将这些参数发送至服务端。如果签名和发送至服务端的不一致，会报签名错误。
//            for(Map.Entry header: headers.entrySet()){
//                put.addHeader(header.getKey().toString(),header.getValue().toString());
//            }
//            for(Map.Entry meta: userMetadata.entrySet()){
//                // 如果使用userMeta，sdk内部会为userMeta拼接"x-oss-meta-"前缀。当您使用其他方式生成签名URL进行上传时，userMeta也需要拼接"x-oss-meta-"前缀。
//                put.addHeader("x-oss-meta-"+meta.getKey().toString(), meta.getValue().toString());
//            }

            httpClient = createClient();
            response = httpClient.execute(put);

            System.out.println("返回上传状态码：" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("使用网络库上传成功");
            }
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
            httpClient.close();
        }
    }

    @SneakyThrows
    private static CloseableHttpClient createClient() {
        // 创建不校验证书的SSLContext，避免错误：unable to find valid certification path to requested target
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        // 创建SSLConnectionSocketFactory，禁用主机名校验
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        // 设置fiddler代理，进行抓包测试
        HttpHost proxy = new HttpHost("127.0.0.1", 8888);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        return HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sslConnectionSocketFactory).build();
    }
}
