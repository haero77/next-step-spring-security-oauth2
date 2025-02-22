package nextstep.security.authentication;

import java.util.Set;

public class OAuth2AuthenticationToken implements Authentication {

    private final String authorizationCode;
    private final boolean authenticated;

    private OAuth2AuthenticationToken(String authorizationCode, boolean authenticated) {
        this.authorizationCode = authorizationCode;
        this.authenticated = authenticated;
    }

    public static OAuth2AuthenticationToken unauthenticated(String authorizationCode) {
        return new OAuth2AuthenticationToken(authorizationCode, false);
    }

    public static OAuth2AuthenticationToken authenticated(OAuth2AuthenticationToken token) {
        return new OAuth2AuthenticationToken(token.authorizationCode, true);
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Set<String> getAuthorities() {
        return null;
    }
}
