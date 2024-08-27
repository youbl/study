package beinet.cn.awss3demo.demos.web;

import beinet.cn.awss3demo.demos.configs.LambdaProperty;
import beinet.cn.awss3demo.demos.web.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

/**
 * Description:
 * aws lambda操作辅助类
 *
 * @author : youbl
 * @create: 2021/9/26 20:01
 */
@Slf4j
public class LambdaHelper {
    private final String delimiter = "/";
    private final LambdaProperty property;
    private LambdaClient client;

    /**
     * 构造函数
     *
     * @param property ak sk等信息
     */
    public LambdaHelper(LambdaProperty property) {
        this.property = property;
        this.afterPropertiesSet();
    }

    private void afterPropertiesSet() {
        try {
            if (StringUtils.hasLength(property.getAccessKey())) {
                log.info("初始化lambda:{}", this.client);
                AwsCredentials credentials = AwsBasicCredentials.create(property.getAccessKey(), property.getAccessSecret());
                AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
                this.client = LambdaClient.builder()
                        .credentialsProvider(credentialsProvider)
                        .region(Region.of(property.getRegion()))
                        .build();
            }
        } catch (Exception e) {
            log.error("Lambda 初始化失败：{}.服务:{}", property, SpringUtil.getApplicationName(), e);
        }
    }

    /**
     * 调用lambda函数。
     * 注意：ak/sk需要配置有如下lambda的执行权限，
     * {
     *     "Version": "2012-10-17",
     *     "Statement": [
     *         {
     *             "Sid": "Stmt1464440182000",
     *             "Effect": "Allow",
     *             "Action": [
     *                 "lambda:InvokeAsync",
     *                 "lambda:InvokeFunction"
     *             ],
     *             "Resource": [
     *                 "*"
     *             ]
     *         }
     *     ]
     * }
     *
     * @param functionName lambda函数名
     * @param jsonPara lambda函数的入参
     * @return 函数结果
     */
    public String call(String functionName, String jsonPara) {
        SdkBytes payload = SdkBytes.fromUtf8String(jsonPara);

        //Setup an InvokeRequest
        InvokeRequest request = InvokeRequest.builder()
                .functionName(functionName)
                .payload(payload)
                .build();

        InvokeResponse res = client.invoke(request);
        return res.payload().asUtf8String();
    }
}
