package nextstep.security.oauth2;

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

    public record GitHub(
            String domain,
            String clientId,
            String clientSecret
    ) {

        public GitHub {
            Assert.hasText(domain, "domain must have text");
            Assert.hasText(clientId, "clientId must have text");
            Assert.hasText(clientSecret, "clientSecret must have text");
        }
    }
}
