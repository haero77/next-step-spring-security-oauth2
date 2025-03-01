package nextstep.security.oauth2;

import nextstep.security.authentication.AuthenticationException;
import org.springframework.lang.NonNull;

import java.util.Map;

public class OAuth2ProviderClientFactory {

    public static OAuth2ProviderClientFactory INSTANCE = new OAuth2ProviderClientFactory();

    private final Map<String, OAuth2ProviderClient> clients;

    private OAuth2ProviderClientFactory() {
        this.clients = Map.of(
                GoogleClient.INSTANCE.getProviderName(), GoogleClient.INSTANCE,
                GitHubClient.INSTANCE.getProviderName(), GitHubClient.INSTANCE
        );
    }

    @NonNull
    public OAuth2ProviderClient getProviderClient(String providerName) {
        OAuth2ProviderClient client = this.clients.get(providerName);
        if (client == null) {
            throw new AuthenticationException("Unsupported OAuth2Provider: %s".formatted(providerName));
        }
        return client;
    }
}
