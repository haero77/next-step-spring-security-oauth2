package nextstep.security.oauth2;

@FunctionalInterface
public interface OAuth2UserService {

    OAuth2User loadUser(GitHubUserInfo gitHubUserInfo);
}
