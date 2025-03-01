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
            String domain,
            String apiDomain,
            String clientId,
            String clientSecret
    ) {

        public GitHub {
            Assert.hasText(domain, "domain cannot be empty");
            Assert.hasText(apiDomain, "apiDomain cannot be empty");
            Assert.hasText(clientId, "clientId cannot be empty");
            Assert.hasText(clientSecret, "clientSecret cannot be empty");
        }
    }

    public record Google(
            String domain,
            String apiDomain,
            String clientId,
            String clientSecret
    ) {

        public Google {
            Assert.hasText(domain, "domain cannot be empty");
            Assert.hasText(apiDomain, "apiDomain cannot be empty");
            Assert.hasText(clientId, "clientId cannot be empty");
            Assert.hasText(clientSecret, "clientSecret cannot be empty");
        }
    }
}
