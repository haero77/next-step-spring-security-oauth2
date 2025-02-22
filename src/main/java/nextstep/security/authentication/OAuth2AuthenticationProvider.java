package nextstep.security.authentication;

import nextstep.security.oauth2.AccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.POST;

public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final SecurityOAuth2Properties securityProperties;

    public OAuth2AuthenticationProvider(SecurityOAuth2Properties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        // Exchange code for an access token: Request POST https://github.com/login/oauth/access_token
        AccessTokenResponse accessToken = exchangeCodeToAccessToken(token);

        // todo request user info using access token

        // todo process after query user info from OAuth2 Provider: create member if not exists

        // todo create OAuth2AuthenticationToken
        throw new UnsupportedOperationException();
    }

    private AccessTokenResponse exchangeCodeToAccessToken(OAuth2AuthenticationToken token) {
        AccessTokenResponse accessToken = requestAccessToken(token);
        if (!accessToken.hasAccessToken()) {
            throw new AuthenticationException("Access token is empty");
        }
        return accessToken;
    }

    private AccessTokenResponse requestAccessToken(OAuth2AuthenticationToken token) {
        OAuth2AuthenticationToken.ClientRegistration client = token.getClientRegistration();

        URI accessTokenRequestUrl = UriComponentsBuilder.fromHttpUrl(generateGitHubAccessTokenRequestUrl())
                .queryParam("client_id", client.clientId())
                .queryParam("client_secret", client.clientSecret())
                .queryParam("code", client.authorizationCode())
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AccessTokenResponse> responseEntity = this.restTemplate.exchange(
                accessTokenRequestUrl,
                POST,
                requestEntity,
                AccessTokenResponse.class
        );

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Failed to get access token");
        }

        return responseEntity.getBody();
    }

    private String generateGitHubAccessTokenRequestUrl() {
        return "%s/login/oauth/access_token".formatted(securityProperties.getGithub().domain());
    }
}
