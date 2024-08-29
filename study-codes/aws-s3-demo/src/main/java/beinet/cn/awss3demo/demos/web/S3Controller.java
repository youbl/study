package beinet.cn.awss3demo.demos.web;

import beinet.cn.awss3demo.demos.configs.AwsConfig;
import beinet.cn.awss3demo.demos.configs.S3Property;
import beinet.cn.awss3demo.demos.dto.ResponseData;
import beinet.cn.awss3demo.demos.web.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * S3操作辅助类
 */
@RestController
@RequestMapping("/s3")
@Slf4j
@RequiredArgsConstructor
public class S3Controller {
    private final AwsConfig awsConfig;

    /**
     * 把指定文件，上传到s3的某个目录下
     * @param file 文件
     * @param dir s3目录
     * @param bucket s3桶
     * @return 上传结果url
     */
    @PostMapping("fileToDir")
    public ResponseData uploadFile(@RequestBody MultipartFile file,
                                   @RequestParam String dir,
                                   @RequestParam String bucket) {
        if (file == null) {
            return ResponseData.fail(500, "未提交文件");
        }

        try {
            if (!StringUtils.hasLength(dir)) {
                dir = "/";
            } else if (!dir.endsWith("/")) {
                dir += "/";
            }

            String s3FileName = StringUtils.hasLength(file.getOriginalFilename()) ? file.getOriginalFilename() : "noName";
            String s3FullName = dir + s3FileName;
            S3Helper s3Helper = new S3Helper(awsConfig.getS3());
            String path = s3Helper.uploadFileToBucket(file, s3FullName, false, bucket);

            //OpLogService.reportLog(SubType.UPLOAD_FILE, Map.of("path", path, "dir", dir), loginInfo.getAccount());

            return ResponseData.ok(path);
        } catch (Exception e) {
            log.error("上传失败", e);
            return ResponseData.fail(500, e.getMessage());
        }
    }

    /**
     * 枚举指定桶下的文件
     * @param prefix 文件的前缀，通常是目录名
     * @param bucket s3的桶
     * @return String数组
     */
    @GetMapping("listFiles")
    public ResponseData getFileList(@RequestParam(required = false) String prefix,
                                    @RequestParam(required = false) String bucket) {
        S3Helper s3Helper = new S3Helper(awsConfig.getS3());
        return ResponseData.ok(s3Helper.listFiles(prefix, bucket));
    }

    /**
     * 枚举所有桶名
     * @return String数组
     */
    @GetMapping("listBuckets")
    public ResponseData getBucketList() {
        S3Helper s3Helper = new S3Helper(awsConfig.getS3());
        return ResponseData.ok(s3Helper.listBucket());
    }

    /**
     * 删除指定文件
     * @param key 文件路径
     * @return 无
     */
    @PostMapping("deleteFile")
    public ResponseData deleteFile(@RequestParam String key,
                                   @RequestParam String bucket) {
        S3Property property = awsConfig.getS3();
        S3Helper s3Helper = new S3Helper(property);
        s3Helper.deleteFile(key, bucket);
        // 删除日志
        //OpLogService.reportLog(SubType.DELETE_FILE, Map.of("path", key, "bucket", property.getBucket()), loginInfo.getAccount());
        return ResponseData.ok(null);
    }

    /**
     * 返回指定url的头信息
     * @param url url
     * @return 头信息
     */
    @GetMapping("testUrl")
    public ResponseData<Map<String, List<String>>> getUrlHeaders(@RequestParam String url) {
        Assert.notNull(url, "url不能为空");
        Assert.isTrue(url.startsWith("http://") || url.startsWith("https://"), "只支持http协议");

        return ResponseData.ok(HttpUtils.getHeaders(url));
    }
}
