package nextstep.security.oauth2.authentication;

@FunctionalInterface
public interface OAuth2UserService {

    OAuth2User loadUser(OAuth2UserRequest userRequest);
}
