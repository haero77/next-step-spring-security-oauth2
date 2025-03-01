package nextstep.security.oauth2;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.AuthenticationProvider;

public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private final OAuth2UserService userService;

    public OAuth2AuthenticationProvider(OAuth2UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2AccessToken accessToken = exchangeCodeToAccessToken(token);

        OAuth2User oAuth2User = this.userService.loadUser(new OAuth2UserRequest(
                token.getClientRegistration(),
                accessToken
        ));

        return OAuth2AuthenticationToken.authenticated(oAuth2User.getName());
    }

    private OAuth2AccessToken exchangeCodeToAccessToken(OAuth2AuthenticationToken token) {
        OAuth2ProviderClient providerClient = getProviderClient(token);
        return providerClient.fetchAccessToken(token.getClientRegistration(), token.getAuthorizationCode());
    }

    private OAuth2ProviderClient getProviderClient(OAuth2AuthenticationToken token) {
        OAuth2Provider provider = token.getClientRegistration().provider();
        return OAuth2ProviderClientFactory.INSTANCE.getProviderClient(provider.getProviderName());
    }
}
