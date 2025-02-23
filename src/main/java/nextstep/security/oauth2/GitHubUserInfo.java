package nextstep.security.oauth2;

import java.io.Serializable;

public class GitHubUserInfo implements OAuth2UserInfo, Serializable {

    private String login;
    private Long id;
    private String email;

    public GitHubUserInfo() {
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    public String getLogin() {
        return login;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GitHubUserInfo{" +
                "login='" + login + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
