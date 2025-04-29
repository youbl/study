package beinet.cn.github.oauth.feigns.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 * @author youbl
 * @since 2025/4/29 18:04
 */
@Data
@Accessors(chain = true)
public class GithubUserDto {
    private String login;
    private Long id;
    private String node_id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String followers_url;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String events_url;
    private String received_events_url;
    private String type;
    private String user_view_type;
    private Boolean site_admin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private Boolean hireable;
    private String bio;
    private String twitter_username;
    private String notification_email;
    private Integer public_repos;
    private Integer public_gists;
    private Integer followers;
    private Integer following;
    private String created_at;
    private String updated_at;
}
