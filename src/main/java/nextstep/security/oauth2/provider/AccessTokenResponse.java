package nextstep.security.oauth2.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

record AccessTokenResponse(
        @JsonProperty("access_token") String accessToken
) {

    AccessTokenResponse {
        Assert.hasText(accessToken, "accessToken cannot be empty");
    }
}
