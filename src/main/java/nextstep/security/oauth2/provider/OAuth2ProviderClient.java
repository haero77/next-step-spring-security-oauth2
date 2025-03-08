package nextstep.security.oauth2.provider;

import nextstep.security.oauth2.client.registration.ClientRegistration;
import nextstep.security.oauth2.core.OAuth2AccessToken;
import nextstep.security.oauth2.core.user.OAuth2User;

public interface OAuth2ProviderClient {

    String getProviderName();

    OAuth2AccessToken fetchAccessToken(ClientRegistration registration, String code1);

    OAuth2User fetchUser(ClientRegistration registration, OAuth2AccessToken accessToken);
}
