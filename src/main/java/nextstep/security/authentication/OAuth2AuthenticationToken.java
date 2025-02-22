package nextstep.security.authentication;

import org.springframework.util.Assert;

import java.util.Set;

public class OAuth2AuthenticationToken implements Authentication {

    private final ClientRegistration clientRegistration;
    private final boolean authenticated;

    private OAuth2AuthenticationToken(ClientRegistration clientRegistration, boolean authenticated) {
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        this.clientRegistration = clientRegistration;
        this.authenticated = authenticated;
    }

    public static OAuth2AuthenticationToken unauthenticated(ClientRegistration clientRegistration) {
        return new OAuth2AuthenticationToken(clientRegistration, false);
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

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }

    public record ClientRegistration(
            String clientId,
            String clientSecret,
            String authorizationCode
    ) {

    }
}
