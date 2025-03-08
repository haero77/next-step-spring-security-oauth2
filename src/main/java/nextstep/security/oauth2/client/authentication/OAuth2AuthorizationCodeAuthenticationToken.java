package nextstep.security.oauth2.client.authentication;

import nextstep.security.authentication.Authentication;
import nextstep.security.oauth2.client.registration.ClientRegistration;
import nextstep.security.oauth2.core.OAuth2AccessToken;
import nextstep.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;

import java.util.Set;

public class OAuth2AuthorizationCodeAuthenticationToken implements Authentication {

    private final OAuth2AccessToken accessToken;
    private final ClientRegistration clientRegistration;
    private final OAuth2AuthorizationExchange authorizationExchange;
    private final boolean authenticated;

    public OAuth2AuthorizationCodeAuthenticationToken(
            OAuth2AccessToken accessToken,
            ClientRegistration clientRegistration,
            OAuth2AuthorizationExchange authorizationExchange,
            boolean authenticated
    ) {
        this.accessToken = accessToken;
        this.clientRegistration = clientRegistration;
        this.authorizationExchange = authorizationExchange;
        this.authenticated = authenticated;
    }

    public static OAuth2AuthorizationCodeAuthenticationToken unauthenticated(
            ClientRegistration clientRegistration,
            OAuth2AuthorizationExchange authorizationExchange
    ) {
        return new OAuth2AuthorizationCodeAuthenticationToken(null, clientRegistration, authorizationExchange, false);
    }

    @Override
    public Set<String> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public OAuth2AccessToken getAccessToken() {
        return this.accessToken;
    }
}
