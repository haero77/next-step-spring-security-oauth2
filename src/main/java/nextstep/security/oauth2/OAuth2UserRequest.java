package nextstep.security.oauth2;

public record OAuth2UserRequest(
        ClientRegistration registration,
        OAuth2AccessToken accessToken
) {

}
