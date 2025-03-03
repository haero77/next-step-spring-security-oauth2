package nextstep.security.oauth2.authentication;

import nextstep.security.oauth2.provider.OAuth2ClientProperties;

public record ClientRegistration(
        String registrationId,
        String clientId,
        String clientSecret,
        ProviderDetails providerDetails
) {

    public static ClientRegistration of(
            OAuth2ClientProperties.Registration registration,
            OAuth2ClientProperties.Provider provider
    ) {
        return new ClientRegistration(
                registration.provider(),
                registration.clientId(),
                registration.clientSecret(),
                ProviderDetails.from(provider)
        );
    }

    public record ProviderDetails(
            String authorizationUri,
            String tokenUri,
            String userInfoUri,
            String userNameAttribute
    ) {

        public static ProviderDetails from(OAuth2ClientProperties.Provider provider) {
            return new ProviderDetails(
                    provider.authorizationUri(),
                    provider.tokenUri(),
                    provider.userInfoUri(),
                    provider.userNameAttribute()
            );
        }
    }
}
