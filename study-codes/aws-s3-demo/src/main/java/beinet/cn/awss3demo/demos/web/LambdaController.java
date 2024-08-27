package beinet.cn.awss3demo.demos.web;

import beinet.cn.awss3demo.demos.configs.AwsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调用aws lambda的demo，
 * 参考文档： <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-lambda.html#invoke-function">...</a>
 *
 * @author youbl
 * @since 2024/8/27 20:35
 */
@RestController
@RequiredArgsConstructor
public class LambdaController {
    private final AwsConfig awsConfig;

    @GetMapping("lambda")
    public String callLambda(@RequestParam(required = false) String functionName,
                             @RequestParam(required = false) String para) {
        functionName = StringUtils.hasLength(functionName) ? functionName : "myFirstFunc";
        String json = StringUtils.hasLength(para) ? para : "{\"length\":66,\"width\":77}";
        LambdaHelper helper = new LambdaHelper(awsConfig.getLambda());
        return helper.call(functionName, json);
    }
}
