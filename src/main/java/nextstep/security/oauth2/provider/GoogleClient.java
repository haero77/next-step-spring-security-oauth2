package nextstep.security.oauth2.provider;

import nextstep.security.authentication.AuthenticationException;
import nextstep.security.oauth2.authentication.ClientRegistration;
import nextstep.security.oauth2.authentication.OAuth2AccessToken;
import nextstep.security.oauth2.authentication.OAuth2AuthorizationCode;
import nextstep.security.oauth2.authentication.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public OAuth2AccessToken fetchAccessToken(ClientRegistration registration, OAuth2AuthorizationCode code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code.codeValue());
        params.add("client_id", registration.clientId());
        params.add("client_secret", registration.clientSecret());
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
        params.add("grant_type", "authorization_code");

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(URI.create(registration.providerDetails().tokenUri()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params);
        logger.info("request: {}", request);

        ResponseEntity<AccessTokenResponse> response = restOperations.exchange(request, AccessTokenResponse.class);
        logger.info("response: {}", response);

        AccessTokenResponse tokenResponse = Objects.requireNonNullElseGet(response.getBody(), () -> {
            throw new AuthenticationException("Failed to get access token");
        });

        return new OAuth2AccessToken(tokenResponse.accessToken());
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
