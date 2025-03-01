package nextstep.security.oauth2;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class OAuth2AuthenticationToken implements Authentication {

    private final String principal;
    private final ClientRegistration clientRegistration;
    private final OAuth2AuthorizationCode authorizationCode;
    private final boolean authenticated;

    private OAuth2AuthenticationToken(
            String principal,
            ClientRegistration clientRegistration,
            OAuth2AuthorizationCode authorizationCode,
            boolean authenticated
    ) {
        this.principal = principal;
        this.clientRegistration = clientRegistration;
        this.authorizationCode = authorizationCode;
        this.authenticated = authenticated;
    }

    public static OAuth2AuthenticationToken unauthenticated(ClientRegistration clientRegistration, OAuth2AuthorizationCode code) {
        return new OAuth2AuthenticationToken(null, clientRegistration, code, false);
    }

    public static OAuth2AuthenticationToken authenticated(String principal) {
        return new OAuth2AuthenticationToken(principal, null, null, true);
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Set<String> getAuthorities() {
        return null; // todo: fill authorities
    }

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }

    public OAuth2AuthorizationCode getAuthorizationCode() {
        return authorizationCode;
    }
}
