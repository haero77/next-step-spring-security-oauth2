package nextstep.security.oauth2.client.registration;

public record ClientRegistration(
        String registrationId,
        String clientId,
        String clientSecret,
        String redirectUri,
        ProviderDetails providerDetails
) {

    public record ProviderDetails(
            String authorizationUri,
            String tokenUri,
            String userInfoUri,
            String userNameAttribute
    ) {

    }
}
