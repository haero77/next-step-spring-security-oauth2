package nextstep.security.oauth2.provider;

import nextstep.security.authentication.AuthenticationException;
import nextstep.security.oauth2.client.registration.ClientRegistration;
import nextstep.security.oauth2.core.OAuth2AccessToken;
import nextstep.security.oauth2.core.user.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static nextstep.security.oauth2.provider.Oauth2Constants.GOOGLE;

public class GoogleClient implements OAuth2ProviderClient {

    public static final GoogleClient INSTANCE = new GoogleClient();
    private static final Logger logger = LoggerFactory.getLogger(GoogleClient.class);

    private final RestOperations restOperations = new RestTemplate();

    private GoogleClient() {
    }

    @Override
    public String getProviderName() {
        return GOOGLE;
    }

    @Override
    public OAuth2User fetchUser(ClientRegistration registration, OAuth2AccessToken accessToken) {
        ClientRegistration.ProviderDetails provider = registration.providerDetails();

        RequestEntity<Void> request = RequestEntity
                .get(URI.create(provider.userInfoUri()))
                .headers(headers -> headers.setBearerAuth(accessToken.value()))
                .build();
        logger.info("request: {}", request);

        ResponseEntity<Map<String, Object>> response = this.restOperations.exchange(
                request,
                new ParameterizedTypeReference<>() {}
        );
        logger.info("response: {}", response);

        Map<String, Object> userInfo = Objects.requireNonNullElseGet(response.getBody(), () -> {
            throw new AuthenticationException("Failed to get user info");
        });

        String usernameAttributeName = provider.userNameAttribute();
        Object username = Objects.requireNonNullElseGet(userInfo.get(usernameAttributeName), () -> {
            throw new AuthenticationException("Failed to get user identifier(=%s)".formatted(usernameAttributeName));
        });

        return username::toString;
    }
}
