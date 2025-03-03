package nextstep.security.oauth2.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuth2ClientProperties {

    private final Map<String, Provider> provider = new HashMap<>();
    private final Map<String, Registration> registration = new HashMap<>();

    public OAuth2ClientProperties() {
    }

    public Map<String, Provider> getProvider() {
        return this.provider;
    }

    public Map<String, Registration> getRegistration() {
        return this.registration;
    }

    public Registration getRegistrationById(String registrationId) {
        Registration registration = this.registration.get(registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Cannot find Registration for registrationId=%s".formatted(registrationId));
        }
        return registration;
    }

    public record Registration(
            String provider,
            String clientId,
            String clientSecret,
            Set<String> scope,
            String redirectUri
    ) {

    }

    public record Provider(
            String authorizationUri,
            String tokenUri,
            String userInfoUri,
            String userNameAttribute
    ) {

    }
}
