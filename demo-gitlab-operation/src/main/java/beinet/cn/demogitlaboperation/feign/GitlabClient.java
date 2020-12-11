package beinet.cn.demogitlaboperation.feign;

import feign.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * Gitlab API操作类
 * url = "${gitlab.host}" 表示读取yml里的配置
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/11 9:30
 */
@FeignClient(name = "gitlabClient", url = "${gitlab.host}", configuration = GitlabClient.FeignClientConfig.class)
public interface GitlabClient {
    /*
     * 1、由于gitlab的api要求【https://docs.gitlab.com/ee/api/merge_requests.html#list-merge-requests】
     * 必须使用转义的项目名，如不允许使用 base-service/ops，必须使用 base-service%2Fops
     * 而使用GetMapping的PathVariable注解，不会对 / 转义，但是会把 % 转义，所以没法使用，只能用 RequestLine注解；
     * 2、默认的feign，使用GetMapping注解, 必须在FeignClient加 feign.Contract.Default，才能用RequestLine注解，
     * 否则会报错： Method createMergeRequest not annotated with HTTP method type (ex. GET, POST)
     * <p>*/


    /**
     * https://docs.gitlab.com/ee/api/repositories.html
     * 注意：跟createMergeRequest不同，url参数里的from是目标，to是源
     *
     * @param token        登录用
     * @param projectName  项目
     * @param sourceBranch sha或分支
     * @param targetBranch sha或分支
     * @return 结果
     */
    @RequestLine(value = "GET api/v4/projects/{projectName}/repository/compare?from={b1}&to={b2}",
            decodeSlash = false)
    @Headers({"Private-Token: {token}"})
    Map<String, Object> compareBranch(@Param("token") String token,
                                      @Param(value = "projectName") String projectName,
                                      @Param(value = "b2") String sourceBranch,
                                      @Param(value = "b1") String targetBranch);

    /**
     * https://docs.gitlab.com/ee/api/merge_requests.html
     * <p>
     * curl --header "PRIVATE-TOKEN: xxx" --data "source_branch=master&target_branch=release/prod&title=testMerge"
     * "https://code.beinet.cn/api/v4/projects/base-service%2Fops/merge_requests"
     * 创建merge request
     *
     * @param token        登录用
     * @param projectName  项目
     * @param sourceBranch sha或分支
     * @param targetBranch sha或分支
     * @param title        mr的标题
     * @return 结果
     */
    @RequestLine(value = "POST api/v4/projects/{projectName}/merge_requests?source_branch={b1}&target_branch={b2}&title={title}",
            decodeSlash = false)
    @Headers({"Private-Token: {token}"})
    Map<String, Object> createMergeRequest(@Param("token") String token,
                                           @Param(value = "projectName") String projectName,
                                           @Param(value = "b1") String sourceBranch,
                                           @Param(value = "b2") String targetBranch,
                                           @Param(value = "title") String title);

    class FeignClientConfig {
        @Bean
        public Contract feignContract() {
            return new feign.Contract.Default();
        }

        @Bean
        public Logger.Level logger() {
            return Logger.Level.FULL;
        }
    }
}
