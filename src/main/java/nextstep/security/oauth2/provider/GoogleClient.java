package nextstep.security.oauth2.provider;

import nextstep.security.oauth2.authentication.ClientRegistration;
import nextstep.security.oauth2.authentication.OAuth2AccessToken;
import nextstep.security.oauth2.authentication.OAuth2AuthorizationCode;
import nextstep.security.oauth2.authentication.OAuth2User;
import org.springframework.web.client.RestTemplate;

import static nextstep.security.oauth2.provider.Oauth2Constants.GOOGLE;

public class GoogleClient implements OAuth2ProviderClient {

    public static final GoogleClient INSTANCE = new GoogleClient();
    private final RestTemplate restTemplate = new RestTemplate();

    private GoogleClient() {
    }

    @Override
    public String getProviderName() {
        return GOOGLE;
    }

    @Override
    public OAuth2AccessToken fetchAccessToken(ClientRegistration registration, OAuth2AuthorizationCode code) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2User fetchUser(ClientRegistration registration, OAuth2AccessToken accessToken) {
        throw new UnsupportedOperationException();
    }
}
