package nextstep.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

public record AccessTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        String scope
) {

    public boolean hasAccessToken() {
        return StringUtils.hasText(accessToken);
    }
}
