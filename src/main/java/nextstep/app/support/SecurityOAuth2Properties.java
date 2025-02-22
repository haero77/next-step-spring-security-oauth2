package nextstep.app.support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "security.oauth2")
public class SecurityOAuth2Properties {

    private final GitHub github;

    public SecurityOAuth2Properties(GitHub github) {
        Assert.notNull(github, "github must not be null");
        this.github = github;
    }

    @NonNull
    public GitHub getGithub() {
        return github;
    }

    public static final class GitHub {

        private final String clientId;

        public GitHub(String clientId) {
            Assert.hasText(clientId, "clientId must have text");
            this.clientId = clientId;
        }

        public String getClientId() {
            return clientId;
        }
    }
}
