package beinet.cn.github.oauth.feigns;

import beinet.cn.github.oauth.feigns.dto.GithubUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 获取github的用户等信息的接口服务
 * @author youbl
 * @since 2025/4/29 16:54
 */
@FeignClient(name = "github-api", url = "https://api.github.com")
public interface GithubApiFeign {
    /**
     * 获取用户信息
     * @param authorization access_token
     * @return 用户信息
     */
    @GetMapping(value = "user", headers = {"Accept=application/json", "Content-Type=application/json"})
    GithubUserDto getUserInfo(@RequestHeader String authorization);

    /*
    成功响应参考：
cost time(ms): 916 status:200 from GET https://api.github.com/user
Headers:
  access-control-allow-origin: *
  access-control-expose-headers: ETag, Link, Location, Retry-After, X-GitHub-OTP, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Used, X-RateLimit-Resource, X-RateLimit-Reset, X-OAuth-Scopes, X-Accepted-OAuth-Scopes, X-Poll-Interval, X-GitHub-Media-Type, X-GitHub-SSO, X-GitHub-Request-Id, Deprecation, Sunset
  cache-control: private, max-age=60, s-maxage=60
  content-length: 1250
  content-security-policy: default-src 'none'
  content-type: application/json; charset=utf-8
  date: Tue, 29 Apr 2025 10:00:16 GMT
  etag: "abc"
  github-authentication-token-expiration: 2025-04-29 18:00:09 UTC
  last-modified: Mon, 28 Apr 2025 09:48:55 GMT
  referrer-policy: origin-when-cross-origin, strict-origin-when-cross-origin
  server: github.com
  strict-transport-security: max-age=31536000; includeSubdomains; preload
  vary: Accept, Authorization, Cookie, X-GitHub-OTP,Accept-Encoding, Accept, X-Requested-With
  x-accepted-github-permissions: allows_permissionless_access=true
  x-accepted-oauth-scopes:
  x-content-type-options: nosniff
  x-frame-options: deny
  x-github-api-version-selected: 2022-11-28
  x-github-media-type: github.v3
  x-github-request-id: abc
  x-oauth-client-id: abc
  x-oauth-scopes:
  x-ratelimit-limit: 5000
  x-ratelimit-remaining: 4999
  x-ratelimit-reset: 1745924416
  x-ratelimit-resource: core
  x-ratelimit-used: 1
  x-xss-protection: 0

Body:
{"login":"youbl","id":1,"node_id":"abc","avatar_url":"https://avatars.githubusercontent.com/u/2508702?v=4","gravatar_id":"","url":"https://api.github.com/users/youbl","html_url":"https://github.com/youbl","followers_url":"https://api.github.com/users/youbl/followers","following_url":"https://api.github.com/users/youbl/following{/other_user}","gists_url":"https://api.github.com/users/youbl/gists{/gist_id}","starred_url":"https://api.github.com/users/youbl/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/youbl/subscriptions","organizations_url":"https://api.github.com/users/youbl/orgs","repos_url":"https://api.github.com/users/youbl/repos","events_url":"https://api.github.com/users/youbl/events{/privacy}","received_events_url":"https://api.github.com/users/youbl/received_events","type":"User","user_view_type":"public","site_admin":false,"name":"水边","company":"@baidu","blog":"http://beinet.cn","location":"Fuzhou","email":"youbl@126.com","hireable":true,"bio":"https://youbl.blog.csdn.net/","twitter_username":null,"notification_email":"youbl@126.com","public_repos":17,"public_gists":0,"followers":12,"following":0,"created_at":"2012-10-08T03:37:21Z","updated_at":"2025-04-28T09:48:55Z"}
     */
}
