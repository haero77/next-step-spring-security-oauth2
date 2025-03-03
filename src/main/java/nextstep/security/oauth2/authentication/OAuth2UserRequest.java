package nextstep.security.oauth2.authentication;

public record OAuth2UserRequest(
        ClientRegistration registration,
        OAuth2AccessToken accessToken
) {

}
