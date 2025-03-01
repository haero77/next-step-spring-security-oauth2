package nextstep.security.oauth2.authentication;

import nextstep.security.oauth2.provider.OAuth2Provider;

public record ClientRegistration(
        OAuth2Provider provider,
        String clientId,
        String clientSecret
) {

}
