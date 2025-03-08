package nextstep.security.oauth2.client.authentication;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.oauth2.client.userinfo.OAuth2UserRequest;
import nextstep.security.oauth2.client.userinfo.OAuth2UserService;
import nextstep.security.oauth2.core.OAuth2AccessToken;
import nextstep.security.oauth2.core.user.OAuth2User;
import nextstep.security.oauth2.provider.OAuth2ProviderClient;
import nextstep.security.oauth2.provider.OAuth2ProviderClientFactory;

public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private final OAuth2UserService userService;

    public OAuth2AuthenticationProvider(OAuth2UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2LoginAuthenticationToken token = (OAuth2LoginAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = exchangeCodeToAccessToken(token);

        OAuth2User oAuth2User = this.userService.loadUser(new OAuth2UserRequest(
                token.getClientRegistration(),
                accessToken
        ));

        return OAuth2LoginAuthenticationToken.authenticated(oAuth2User);
    }

    private OAuth2AccessToken exchangeCodeToAccessToken(OAuth2LoginAuthenticationToken token) {
        OAuth2ProviderClient providerClient = getProviderClient(token);
        return providerClient.fetchAccessToken(
                token.getClientRegistration(),
                token.getAuthorizationExchange().authorizationResponse().code()
        );
    }

    private OAuth2ProviderClient getProviderClient(OAuth2LoginAuthenticationToken token) {
        String providerName = token.getClientRegistration().registrationId();
        return OAuth2ProviderClientFactory.INSTANCE.getProviderClient(providerName);
    }
}
