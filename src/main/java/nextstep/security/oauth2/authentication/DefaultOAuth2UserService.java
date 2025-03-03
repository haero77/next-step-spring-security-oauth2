package nextstep.security.oauth2.authentication;

import nextstep.security.oauth2.provider.OAuth2ProviderClient;
import nextstep.security.oauth2.provider.OAuth2ProviderClientFactory;

public class DefaultOAuth2UserService implements OAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2ProviderClient client = getProviderClient(userRequest);
        return client.fetchUser(userRequest.registration(), userRequest.accessToken());
    }

    private OAuth2ProviderClient getProviderClient(OAuth2UserRequest userRequest) {
        String providerName = userRequest.registration().registrationId();
        return OAuth2ProviderClientFactory.INSTANCE.getProviderClient(providerName);
    }
}
