package nextstep.security.oauth2;

import nextstep.security.authentication.Authentication;
import org.springframework.util.Assert;

import java.util.Set;

public class OAuth2AuthenticationToken implements Authentication {

    private final String principal;
    private final ClientRegistration clientRegistration;
    private final boolean authenticated;

    private OAuth2AuthenticationToken(String principal, ClientRegistration clientRegistration, boolean authenticated) {
        this.principal = principal;
        this.clientRegistration = clientRegistration;
        this.authenticated = authenticated;
    }

    public static OAuth2AuthenticationToken unauthenticated(ClientRegistration clientRegistration) {
        return new OAuth2AuthenticationToken(null, clientRegistration, false);
    }

    public static OAuth2AuthenticationToken authenticated(String principal) {
        return new OAuth2AuthenticationToken(principal, null, true);
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
        return null;
    }

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }
}
