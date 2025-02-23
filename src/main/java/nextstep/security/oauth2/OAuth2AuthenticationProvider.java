package nextstep.security.oauth2;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationProvider.class);

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

        // Step1: Exchange code for an access token
        AccessTokenResponse accessToken = exchangeCodeToAccessToken(token);

        // Step2: Query user info from OAuth2 Provider using via access token
        OAuth2UserInfo userInfo = getUserInfo(accessToken);

        // todo process after query user info from OAuth2 Provider: create member if not exists

        // todo create authenticated OAuth2AuthenticationToken
        throw new UnsupportedOperationException();
    }

    private AccessTokenResponse exchangeCodeToAccessToken(OAuth2AuthenticationToken token) {
        AccessTokenResponse accessToken = requestAccessToken(token);
        if (!accessToken.hasAccessToken()) {
            throw new AuthenticationException("Access token is empty");
        }

        logger.info("Successfully received OAuth2 access token: {}", accessToken);

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

        logger.info("Access token response: {}", responseEntity);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException("Failed to get access token");
        }

        return responseEntity.getBody();
    }

    private String generateGitHubAccessTokenRequestUrl() {
        return "%s/login/oauth/access_token".formatted(securityProperties.getGithub().domain());
    }

    private GitHubUserInfo getUserInfo(AccessTokenResponse accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.accessToken());
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<GitHubUserInfo> responseEntity = this.restTemplate.exchange(
                this.securityProperties.getGithub().apiDomain() + "/user", // GitHub email이 public이 아닐 경우 email이 null로 리턴
                GET,
                requestEntity,
                GitHubUserInfo.class
        );
        logger.info("User info response: {}", responseEntity);

        GitHubUserInfo userInfo = responseEntity.getBody();
        logger.info("User info: {}", userInfo);

        return userInfo;
    }
}
