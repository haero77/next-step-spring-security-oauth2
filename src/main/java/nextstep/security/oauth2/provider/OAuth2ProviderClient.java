package nextstep.security.oauth2.provider;

import nextstep.security.oauth2.authentication.ClientRegistration;
import nextstep.security.oauth2.authentication.OAuth2AccessToken;
import nextstep.security.oauth2.authentication.OAuth2AuthorizationCode;
import nextstep.security.oauth2.authentication.OAuth2User;

public interface OAuth2ProviderClient {

    String getProviderName();

    OAuth2AccessToken fetchAccessToken(ClientRegistration registration, OAuth2AuthorizationCode code);

    OAuth2User fetchUser(ClientRegistration registration, OAuth2AccessToken accessToken);
}
