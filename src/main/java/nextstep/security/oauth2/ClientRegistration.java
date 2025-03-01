package nextstep.security.oauth2;

public record ClientRegistration(
        OAuth2Provider provider,
        String clientId,
        String clientSecret
) {

}
