package nextstep.security.oauth2;

public class DefaultOAuth2UserService implements OAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2ProviderClient client = getProviderClient(userRequest);
        return client.fetchUser(userRequest.registration(), userRequest.accessToken());
    }

    private OAuth2ProviderClient getProviderClient(OAuth2UserRequest userRequest) {
        OAuth2Provider provider = userRequest.registration().provider();
        return OAuth2ProviderClientFactory.INSTANCE.getProviderClient(provider.getProviderName());
    }
}
