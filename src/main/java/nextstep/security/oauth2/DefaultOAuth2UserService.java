package nextstep.security.oauth2;

import nextstep.app.security.CustomOAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class DefaultOAuth2UserService implements OAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultOAuth2UserService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final SecurityOAuth2Properties securityProperties;

    public DefaultOAuth2UserService(SecurityOAuth2Properties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        GitHubUserInfo userInfo = getUserInfo(userRequest.accessToken());
        return new CustomOAuth2User(userInfo.getId().toString());
    }

    private GitHubUserInfo getUserInfo(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.value());
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
