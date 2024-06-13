package beinet.cn.awss3demo.demos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2024/6/12 17:57
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String home() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }

    @GetMapping("length")
    public long getLength() {
        String url = "https://get.xxx.com/client/prod/win/x64/2.15.0/xxx_x64_2.15.0.exe";

        var helper = new S3Helper(createS3Property());
        return helper.getContentLength(getKeyFromS3Url(url));
    }


    @GetMapping("url")
    public String getUrl() {
        String url = "https://get.xxx.com/prod/errorLog/7eadf87a10ceac78a5a6a7f0ca1ad4f1/20240612/17181938631840.2295927702941727_logs.zip";

        var helper = new S3Helper(createS3Property());
        return helper.getPreSignedDownloadUrl(getKeyFromS3Url(url));
    }

    private S3Helper.S3Property createS3Property() {
        S3Helper.S3Property prop = new S3Helper.S3Property();
        prop.setBucket("xxx");
        prop.setDownUrl("https://get.xxx.com");
        prop.setEnabled(true);
        prop.setEndpoint("s3.ap-southeast-1.amazonaws.com");
        prop.setRegion("ap-southeast-1");
        return prop;
    }

    private String getKeyFromS3Url(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return url;
        }
        int idx = url.indexOf("/", 8);
        if (idx > 0)
            return url.substring(idx + 1);
        return url;
    }
}
