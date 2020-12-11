package beinet.cn.demogitlaboperation;

import beinet.cn.demogitlaboperation.feign.GitlabClient;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

@SpringBootTest
class DemoGitlabOperationApplicationTests {
    @Autowired
    GitlabClient gitlabClient;

    @Value("${gitlab.host:}")
    private String host;

    @Value("${gitlab.token:}")
    private String token;

    @Test
    void contextLoads() throws UnsupportedEncodingException {
        String projectName = "base-service/ops";
        String project = URLEncoder.encode(projectName, "UTF-8");
        String source = "release/prod";
        String target = "master";
        String title = "aaa&bbb";

        // 检查是否有差异，没有就退出
        // 空结果如下：
        //  {"commit":null,"commits":[],"diffs":[],"compare_timeout":false,"compare_same_ref":false}
        // 非空结果如下：
        // {"commit":{"id":"967a00ea4980108ffaa7e757ce915335f4462939","short_id":"967a00ea","title":"合并分支 'master' 到 'release/prod'","created_at":"2020-12-11T07:31:35.000+00:00","parent_ids":["a94367ee48951c6db31bb9335f901f02b6ffdd4a","3e111964f7a80289377c4315817f279482ff90d4"],"message":"合并分支 'master' 到 'release/prod'\n\ntestMerge\n\n查看合并请求 base-service/ops!6","author_name":"张三","author_email":"beinet@beinet.cn","authored_date":"2020-12-11T07:31:35.000+00:00","committer_name":"张三","committer_email":"beinet@beinet.cn","committed_date":"2020-12-11T07:31:35.000+00:00"},"commits":[{"id":"a94367ee48951c6db31bb9335f901f02b6ffdd4a","short_id":"a94367ee","title":"test","created_at":"2020-09-07T16:39:21.000+08:00","parent_ids":["8b11be46dd269d5540868a61f5e8ba051305b54f"],"message":"test\n","author_name":"youbl","author_email":"youbl@126.com","authored_date":"2020-09-07T16:39:21.000+08:00","committer_name":"youbl","committer_email":"youbl@126.com","committed_date":"2020-09-07T16:39:21.000+08:00"},{"id":"967a00ea4980108ffaa7e757ce915335f4462939","short_id":"967a00ea","title":"合并分支 'master' 到 'release/prod'","created_at":"2020-12-11T07:31:35.000+00:00","parent_ids":["a94367ee48951c6db31bb9335f901f02b6ffdd4a","3e111964f7a80289377c4315817f279482ff90d4"],"message":"合并分支 'master' 到 'release/prod'\n\ntestMerge\n\n查看合并请求 base-service/ops!6","author_name":"张三","author_email":"beinet@beinet.cn","authored_date":"2020-12-11T07:31:35.000+00:00","committer_name":"张三","committer_email":"beinet@beinet.cn","committed_date":"2020-12-11T07:31:35.000+00:00"}],"diffs":[{"old_path":"Readme.adoc","new_path":"Readme.adoc","a_mode":"100644","b_mode":"100644","new_file":false,"renamed_file":false,"deleted_file":false,"diff":"--- a/Readme.adoc\n+++ b/Readme.adoc\n@@ -18,4 +18,4 @@ ops 发布的流程\n \n == hostname\n \n-如果 nodejs 读取到本地为 Web ，则替换为 Nodejs\n\\ No newline at end of file\n+如果 nodejs 读取到本地为 Web ，则替换为 Nodejs.\n\\ No newline at end of file\n"}],"compare_timeout":false,"compare_same_ref":false}
        Map<String, Object> compareResult = gitlabClient.compareBranch(token, project, source, target);
        if (isCompareEmpty(compareResult)) {
            System.out.println("2分支不存在差异，无需合并");
            return;
        }

        // 有差异，调接口创建Merge Request
        int mergeRequestId = 0;
        try {
            // 成功结果
            // {"id":2317,"iid":8,"project_id":386,"title":"aaa&bbb","description":null,"state":"opened","created_at":"2020-12-11T08:03:58.890Z","updated_at":"2020-12-11T08:03:58.890Z","target_branch":"master","source_branch":"release/prod","upvotes":0,"downvotes":0,"author":{"id":4,"name":"张三","username":"zs","state":"active","avatar_url":"https://code.beinet.cn/uploads/-/system/user/avatar/4/avatar.png","web_url":"https://code.beinet.cn/sz"},"assignee":null,"source_project_id":386,"target_project_id":386,"labels":[],"work_in_progress":false,"milestone":null,"merge_when_pipeline_succeeds":false,"merge_status":"unchecked","sha":"967a00ea4980108ffaa7e757ce915335f4462939","merge_commit_sha":null,"user_notes_count":0,"discussion_locked":null,"should_remove_source_branch":null,"force_remove_source_branch":null,"web_url":"https://code.beinet.cn/base-service/ops/merge_requests/8","time_stats":{"time_estimate":0,"total_time_spent":0,"human_time_estimate":null,"human_total_time_spent":null},"subscribed":true,"changes_count":"1"}
            Map<String, Object> createResult = gitlabClient.createMergeRequest(token, project, source, target, title);

            String strIid = String.valueOf(createResult.get("iid"));
            String mergeUrl = host + "/" + projectName + "/merge_requests/" + strIid;
            System.out.println("Merge Request的地址： " + mergeUrl);

            mergeRequestId = Integer.parseInt(strIid);
        } catch (FeignException exp) {
            if (exp.status() == 409) { // 这2个分支的合并请求已经存在
                String errMsg = exp.contentUTF8();
                System.out.println(errMsg);
                return;
            }

            String errMsg = exp.contentUTF8();
            System.out.println(exp.status() + errMsg);
            return;
        }

        // 创建成功后，直接合并该Merge Request
        try {
            // 成功结果
            // {"id":2319,"iid":10,"project_id":386,"title":"aaa&bbb","description":null,"state":"merged","created_at":"2020-12-11T09:00:16.261Z","updated_at":"2020-12-11T09:00:18.997Z","target_branch":"master","source_branch":"release/prod","upvotes":0,"downvotes":0,"author":{"id":4,"name":"张三","username":"zs","state":"active","avatar_url":"https://code.beinet.cn/uploads/-/system/user/avatar/4/avatar.png","web_url":"https://code.beinet.cn/zs"},"assignee":null,"source_project_id":386,"target_project_id":386,"labels":[],"work_in_progress":false,"milestone":null,"merge_when_pipeline_succeeds":false,"merge_status":"can_be_merged","sha":"967a00ea4980108ffaa7e757ce915335f4462939","merge_commit_sha":"629337ebdded15ffb4d05567d8eb37e311e1c588","user_notes_count":0,"discussion_locked":null,"should_remove_source_branch":null,"force_remove_source_branch":null,"web_url":"https://code.beinet.cn/base-service/ops/merge_requests/10","time_stats":{"time_estimate":0,"total_time_spent":0,"human_time_estimate":null,"human_total_time_spent":null},"subscribed":true,"changes_count":"1"}
            Map<String, Object> acceptResult = gitlabClient.acceptMergeRequest(token, project, mergeRequestId);
            String state = String.valueOf(acceptResult.get("state"));
            if (state != null && state.equals("merged")) {
                System.out.println("合并成功");
            } else {
                System.out.println("合并失败：" + state);
            }
        } catch (FeignException exp) {
            switch (exp.status()) {
                case 405: // 有冲突无法合并
                    break;
                case 406:// 已经合并或被关闭
                    break;
                case 409:// sha不匹配？？
                    break;
                case 401:// 没合并权限
                    break;
            }
            String errMsg = exp.contentUTF8();
            System.out.println(exp.status() + errMsg);

            return;
        }
    }

    boolean isCompareEmpty(Map<String, Object> compareResult) {
        Object commits = compareResult.get("commits");
        if (commits == null) {
            throw new RuntimeException("gitlab返回结果有误");
        }
        ArrayList arr = (ArrayList) commits;
        return arr.isEmpty();
    }

    /*
     200 – OK ： This means that the GET , PUT, or DELETE request was successful. When you request a resource, it will be returned in JSON format.
     201 – Created : This means that the POST request was successful. This status code is only returned when you try to create a new resource. The resource will also be returned to you.
     400 – Bad Request : This mean you have missed a required attribute for this request. For example, the title for a merge request was not given.
     401 – Unauthorized ： This means that you are not authenticated. If you don’t send a secret token with your request or send an invalid token, this status is returned.
     403 – Forbidden : This means that you are authenticated but don’t have the required permissions to perform thte given request. This can happen, for example , if you try to delete a project but you’re not an owner of the project.
     404 – Not Found : This means that the resource you’re trying to fetch dose not exist. For example, you try to request an issue by its ID, but that issue could not be found.
     405 – Method not allowed ： This means that GitLab does not support the request you try to perform.
     409 – Conflict : This means that a conflicting resource alreadly exists; for example , you try to create a project with the same name as a project that was already created.
     500 – Server error : This means that something went wrong with your request. This was a server-side issue, so you don’t have to alter your message. You might find a bug in Gitlab.
     */
}
