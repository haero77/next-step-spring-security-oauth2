package nextstep.security.oauth2.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "security.oauth2")
public class OAuth2ClientProperties {

    private final GitHub github;
    private final Google google;

    public OAuth2ClientProperties(GitHub github, Google google) {
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
            String userInfoUri,
            String usernameAttributeName
    ) implements OAuth2Provider {

        private static final String USERNAME_ATTRIBUTE_NAME = "id";

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

        @Override
        public String getUsernameAttributeName() {
            return USERNAME_ATTRIBUTE_NAME;
        }
    }

    public record Google(
            String clientId,
            String clientSecret,
            String tokenUri,
            String userInfoUri
    ) implements OAuth2Provider {

        private static final String USERNAME_ATTRIBUTE_NAME = "id";

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

        @Override
        public String getUsernameAttributeName() {
            return USERNAME_ATTRIBUTE_NAME;
        }
    }
}
