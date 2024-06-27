package beinet.cn.awss3demo.demos.web;

import beinet.cn.awss3demo.demos.web.utils.SpringUtil;
import beinet.cn.awss3demo.demos.web.utils.dto.StoreInfo;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * aws s3操作辅助类
 *
 * @author : youbl
 * @create: 2021/9/26 20:01
 */
@Slf4j
public class S3Helper {
    private final String delimiter = "/";
    private final S3Property property;
    private S3Client client;

    private S3Presigner presigner;

    /**
     * 构造函数
     *
     * @param property ak sk等信息
     */
    public S3Helper(S3Property property) {
        this.property = property;
        this.afterPropertiesSet();
    }

    private void afterPropertiesSet() {
        try {
            if (StringUtils.hasLength(property.getAccessKey())) {
                log.info("有ak/sk初始化s3{}", this.client);
                AwsCredentials credentials = AwsBasicCredentials.create(property.getAccessKey(), property.getAccessSecret());
                AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
                this.client = S3Client.builder().credentialsProvider(credentialsProvider).region(Region.of(property.getRegion())).build();
                this.presigner = S3Presigner.builder()
                        .credentialsProvider(credentialsProvider)
                        .region(Region.of(property.getRegion()))
                        .build();
            } else {
                // aws支持对机器配置角色，这样在这台机器上自动拥有相应权限，不需要生成ak/sk
                log.info("无ak/sk初始化s3:{}", this.client);
                this.client = S3Client.builder().region(Region.of(property.getRegion())).build();
                // 用于生成签名url上传下载
                this.presigner = S3Presigner.builder()
                        .region(Region.of(property.getRegion()))
                        .build();
            }
        } catch (Exception e) {
            log.error("S3 初始化失败：{}.服务:{}", property, SpringUtil.getApplicationName(), e);
        }
    }

    /**
     * 指定的s3文件是否存在
     *
     * @param s3FileName 文件相对路径
     * @return true false
     */
    public Boolean exitFile(String s3FileName) {
        boolean exitFile = false;
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(this.property.getBucket())
                    .key(s3FileName)
                    .build();
            client.headObject(headObjectRequest);
            exitFile = true;
        } catch (S3Exception e) {
            if (e.statusCode() != HttpStatus.NOT_FOUND.value() &&
                    e.statusCode() != HttpStatus.UNAUTHORIZED.value()) {
                log.warn("s3文件验证报错:{}", s3FileName, e);
            }
        } catch (Exception e) {
            log.warn("文件验证报错:{}", s3FileName, e);
        }
        return exitFile;
    }

    /**
     * 删除指定的文件
     *
     * @param s3FileName 文件相对路径
     */
    public void deleteFile(String s3FileName) {
        try {
            DeleteObjectResponse deleteObjectResponse = client.deleteObject(DeleteObjectRequest.builder().bucket(property.getBucket()).key(s3FileName).build());
            // 检查删除是否成功
            if (deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
                log.info("文件删除成功:{}", s3FileName);
            }
            log.info("S3文件：{},删除结果：{}", s3FileName, deleteObjectResponse.sdkHttpResponse());
        } catch (Exception e) {
            log.error("删除文件失败:{},", s3FileName, e);
        }
    }

    /**
     * 上传文件到s3
     *
     * @param file           上传文件对象
     * @param directoryName  相对目录, 等于random表示随机文件名
     * @param fixedDirectory 相对目录前面的固定相对目录
     * @return 上传后文件的url
     */
    public String uploadFile(MultipartFile file, String directoryName, String fixedDirectory) {
        return uploadFile(file, directoryName, fixedDirectory, false);
    }

    /**
     * 上传文件到s3
     *
     * @param file           上传文件对象
     * @param directoryName  相对目录, 等于random表示随机文件名
     * @param fixedDirectory 相对目录前面的固定相对目录
     * @param overwrite      存在时是否覆盖
     * @return 上传后文件的url
     */
    @SneakyThrows
    public String uploadFile(MultipartFile file, String directoryName, String fixedDirectory, boolean overwrite) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            fileName = UUID.randomUUID().toString();
        } else if ("random".equals(directoryName)) {
            String ext = "";
            int idx = fileName.lastIndexOf(".");
            if (idx != -1 && idx < fileName.length() - 1) {
                ext = fileName.substring(idx);
            }
            fileName = UUID.randomUUID() + ext;
        }
        String s3FileName = SpringUtil.getActiveProfile() + "/" + directoryName + "/" + fileName;
        if (StringUtils.hasLength(fixedDirectory)) {
            s3FileName = fixedDirectory + "/" + s3FileName;
        }
        return uploadFile(file, s3FileName, overwrite);
    }

    /**
     * 上传文件到s3
     *
     * @param file       上传文件对象
     * @param s3FileName 上传到的相对路径
     * @param overwrite  存在时是否覆盖
     * @return 上传后文件的url
     */
    @SneakyThrows
    public String uploadFile(MultipartFile file, String s3FileName, boolean overwrite) {
        if (!StringUtils.hasLength(s3FileName)) {
            s3FileName = "tmp/" + file.getOriginalFilename();
        } else if (s3FileName.startsWith("https://")) {
            s3FileName = s3FileName.substring(s3FileName.indexOf("/", 9) + 1);
        }

        byte[] arrBytes = file.getBytes();
        if (!overwrite && exitFile(s3FileName)) {
            URL url = new URL(getUrl(s3FileName));
            throw new RuntimeException("文件已存在: " + url);
        }
        String contentType = getContentType(file);
//        System.out.println(contentType);
//
//        if (StringUtils.hasLength(contentType))
//            return contentType;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(property.getBucket())
                .key(s3FileName) // 设置在存储桶中的对象键（文件名）/文件路径
                .contentType(contentType)
                .build();
        client.putObject(putObjectRequest, RequestBody.fromBytes(arrBytes));
        URL url = new URL(getUrl(s3FileName));
        log.info("文件名：{},上传到:{}", s3FileName, url);
        return url.toString();
    }

    /**
     * 生成原始的aws s3下载url
     *
     * @param bucket   s3桶名
     * @param fileName 相对路径
     * @return url
     */
    public String getOriS3Url(String bucket, String fileName) {
        return "https://" + bucket + ".s3.amazonaws.com/" + fileName;
    }

    /**
     * 把指定的文件内容，写入s3文件，s3文件存在时会覆盖
     *
     * @param fileContent 文件内容
     * @param s3FileName  s3文件路径
     * @param contentType 文件类型
     * @return 上传结果url
     */
    public String uploadFile(String fileContent, String s3FileName, String contentType) {
        if (!StringUtils.hasLength(fileContent)) {
            throw new RuntimeException("上传内容不能为空");
        }
        if (!StringUtils.hasLength(contentType)) {
            contentType = "application/octet-stream";
        }
        byte[] arrBytes = fileContent.getBytes();
        try {
            if (exitFile(s3FileName)) {
                deleteFile(s3FileName);
            }
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(property.getBucket())
                    .key(s3FileName) // 设置在存储桶中的对象键（文件名）/文件路径
                    .contentType(contentType)
                    .build();
            client.putObject(putObjectRequest, RequestBody.fromBytes(arrBytes));
            URL url = new URL(getUrl(s3FileName));
            log.info("文件名：{},上传到:{}", s3FileName, url.toString());
            return url.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取下载的预签名地址
     *
     * @param s3FileName 文件的相对路径名（不含bucket）
     * @return 预签名地址
     */
    public String getPreSignedDownloadUrl(String s3FileName) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(this.property.getBucket())
                .key(s3FileName)
                .build();
        // 生成预签名 的下载URL
        GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1L))
                .getObjectRequest(objectRequest)
                .build();
        PresignedGetObjectRequest preSignedRequest = presigner.presignGetObject(preSignRequest);
        return preSignedRequest.url().toString();
    }

    /**
     * 返回s3文件大小
     *
     * @param s3FileName s3 文件的相对路径名（不含bucket）
     * @return 大小
     */
    public long getContentLength(String s3FileName) {
        int idx = s3FileName.indexOf('?');
        if (idx > 0) {
            s3FileName = s3FileName.substring(0, idx);
        }
        HeadObjectRequest headObjectRequest =
                HeadObjectRequest.builder()
                        .bucket(this.property.getBucket())
                        .key(s3FileName)
                        .build();
        //try {
        HeadObjectResponse headObjectResponse =
                client.headObject(headObjectRequest);
        Long contentLength = headObjectResponse.contentLength();
        return contentLength == null ? -1 : contentLength;
        //} catch (Exception exp) {
        //    return -2;
        //}
    }

    private String getContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (StringUtils.hasLength(contentType))
            return contentType;
        // S3的SDK默认就是用这个 ContentType
        return "application/octet-stream";
    }

    /**
     * 枚举当前bucket下的文件
     *
     * @param prefix 文件所属的子目录或文件前缀
     * @param bucket 存储桶
     * @return 文件列表
     */
    public List<StoreInfo> listFiles(String prefix, String bucket) {
        if (!StringUtils.hasLength(bucket))
            bucket = property.getBucket();

        if (!StringUtils.hasLength(prefix)) {
            prefix = "";
        } else {
            if (!prefix.endsWith(delimiter)) {
                prefix += delimiter;
            }
            if (prefix.startsWith(delimiter)) {
                prefix = prefix.substring(1);
            }
        }

        ListObjectsV2Request.Builder builder = ListObjectsV2Request.builder()
                .bucket(bucket)
                .delimiter(delimiter)  // 只返回一级子目录和文件，不返回二级子目录和文件
                .prefix(prefix)
                .maxKeys(1000);  // 最多返回多少文件
        ListObjectsV2Request request = builder.build();

        ListObjectsV2Response response = client.listObjectsV2(request);
        List<StoreInfo> ret = new ArrayList<>();
        // 这个是子目录列表
        for (CommonPrefix dir : response.commonPrefixes()) {
            if (dir.prefix().equals("/"))
                continue;
            StoreInfo info = new StoreInfo()
                    .setKey(dir.prefix())
                    .setDir(true)
                    .setUrl(getUrl(dir.prefix()));
            ret.add(info);
        }
        // 这个是文件列表
        for (S3Object object : response.contents()) {
            StoreInfo info = new StoreInfo().setKey(object.key())
                    .setLastModified(object.lastModified())
                    .setDir(false)
                    .setSize(object.size())
                    .setEtag(object.eTag())
                    .setUrl(getUrl(object.key()));
            ret.add(info);
        }
        return ret;
    }

    /**
     * 列出当前ak下所有桶名
     *
     * @return 桶列表
     */
    public List<String> listBucket() {
        ListBucketsResponse response = client.listBuckets();
        List<Bucket> buckets = response.buckets();
        return buckets.stream().map(Bucket::name).collect(Collectors.toList());
    }

    /**
     * 返回指定文件的下载地址
     *
     * @param key 指定文件，相对路径
     * @return url
     */
    public String getUrl(String key) {
        if (StringUtils.hasLength(property.getDownUrl())) {
            return property.getDownUrl() + "/" + key;
        } else {
            return getOriS3Url(property.getBucket(), key);
        }
    }

    @Data
    public static class S3Property {
        private Boolean enabled;

        private String accessKey;

        private String accessSecret;

        private String bucket;

        private String endpoint;

        private String region;

        private String downUrl;
    }
}
