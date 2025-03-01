package nextstep.security.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "security.oauth2")
public class SecurityOAuth2Properties {

    private final GitHub github;
    private final Google google;

    public SecurityOAuth2Properties(GitHub github, Google google) {
        Assert.notNull(github, "github must not be null");
        Assert.notNull(google, "google must not be null");

        this.github = github;
        this.google = google;
    }

    @NonNull
    public GitHub getGithub() {
        return github;
    }

    public Google getGoogle() {
        return this.google;
    }

    public record GitHub(
            String clientId,
            String clientSecret,
            String tokenUri,
            String userInfoUri
    ) implements OAuth2Provider {

        public GitHub {
            Assert.hasText(clientId, "clientId cannot be empty");
            Assert.hasText(clientSecret, "clientSecret cannot be empty");
            Assert.hasText(tokenUri, "tokenUri cannot be empty");
            Assert.hasText(userInfoUri, "userInfoUri cannot be empty");
        }

        @Override
        public String getProviderName() {
            return Oauth2Constants.GITHUB;
        }

        @Override
        public String getAccessTokenUri() {
            return tokenUri();
        }

        @Override
        public String getUserInfoUri() {
            return userInfoUri();
        }
    }

    public record Google(
            String clientId,
            String clientSecret,
            String tokenUri,
            String userInfoUri
    ) implements OAuth2Provider {

        public Google {
            Assert.hasText(clientId, "clientId cannot be empty");
            Assert.hasText(clientSecret, "clientSecret cannot be empty");
            Assert.hasText(tokenUri, "tokenUri cannot be empty");
            Assert.hasText(userInfoUri, "userInfoUri cannot be empty");
        }

        @Override
        public String getProviderName() {
            return Oauth2Constants.GOOGLE;
        }

        @Override
        public String getAccessTokenUri() {
            return tokenUri();
        }

        @Override
        public String getUserInfoUri() {
            return userInfoUri();
        }
    }
}
