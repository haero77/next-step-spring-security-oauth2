package nextstep.security.oauth2.authentication;

import org.springframework.util.Assert;

public record OAuth2AccessToken(
        String value
) {

    public OAuth2AccessToken {
        Assert.hasText(value, "token value cannot be empty");
    }
}
