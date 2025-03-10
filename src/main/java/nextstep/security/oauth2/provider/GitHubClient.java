package nextstep.security.oauth2.provider;

import nextstep.security.authentication.AuthenticationException;
import nextstep.security.oauth2.authentication.ClientRegistration;
import nextstep.security.oauth2.authentication.OAuth2AccessToken;
import nextstep.security.oauth2.authentication.OAuth2AuthorizationCode;
import nextstep.security.oauth2.authentication.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static nextstep.security.oauth2.provider.Oauth2Constants.GITHUB;

public class GitHubClient implements OAuth2ProviderClient {

    public static final GitHubClient INSTANCE = new GitHubClient();

    private static final Logger logger = LoggerFactory.getLogger(GitHubClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private GitHubClient() {
    }

    @Override
    public String getProviderName() {
        return GITHUB;
    }

    @Override
    public OAuth2AccessToken fetchAccessToken(ClientRegistration registration, OAuth2AuthorizationCode code) {
        ResponseEntity<AccessTokenResponse> responseEntity = callAccessTokenApi(registration, code);

        AccessTokenResponse tokenResponse = Objects.requireNonNullElseGet(responseEntity.getBody(), () -> {
            throw new AuthenticationException("Failed to get access token");
        });

        return new OAuth2AccessToken(tokenResponse.accessToken());
    }

    @Override
    public OAuth2User fetchUser(ClientRegistration registration, OAuth2AccessToken accessToken) {
        OAuth2Provider provider = registration.provider();

        RequestEntity<?> request = RequestEntity
                .get(URI.create(provider.getUserInfoUri()))
                .headers(headers -> headers.setBearerAuth(accessToken.value()))
                .build();
        logger.info("request: {}", request);

        ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
                request,
                new ParameterizedTypeReference<>() {}
        );
        logger.info("response: {}", response);

        Map<String, Object> userInfo = Objects.requireNonNullElseGet(response.getBody(), () -> {
            throw new AuthenticationException("Failed to get user info");
        });

        String usernameAttributeName = provider.getUsernameAttributeName();
        Object username = Objects.requireNonNullElseGet(userInfo.get(usernameAttributeName), () -> {
            throw new AuthenticationException("Failed to get user identifier(=%s)".formatted(usernameAttributeName));
        });

        return username::toString;
    }

    private ResponseEntity<AccessTokenResponse> callAccessTokenApi(ClientRegistration registration, OAuth2AuthorizationCode code) {
        OAuth2Provider provider = registration.provider();

        URI accessTokenUrl = UriComponentsBuilder.fromHttpUrl(provider.getAccessTokenUri())
                .queryParam("client_id", registration.clientId())
                .queryParam("client_secret", registration.clientSecret())
                .queryParam("code", code.codeValue())
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        return this.restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.POST,
                requestEntity,
                AccessTokenResponse.class
        );
    }
}
