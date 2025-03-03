package nextstep.security.oauth2.core;

import org.springframework.util.Assert;

public record OAuth2AuthorizationCode(
        String codeValue
) {

    public OAuth2AuthorizationCode {
        Assert.hasText(codeValue, "codeValue cannot be empty");
    }
}
