package nextstep.security.oauth2;

public record ClientRegistration(
        String clientId,
        String clientSecret,
        String authorizationCode
) {

}
