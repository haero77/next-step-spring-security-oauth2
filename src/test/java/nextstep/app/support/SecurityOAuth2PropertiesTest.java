package nextstep.app.support;

import nextstep.app.testsupport.BaseIntegrationTestSupport;
import nextstep.security.oauth2.SecurityOAuth2Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SecurityOAuth2PropertiesTest extends BaseIntegrationTestSupport {

    @Autowired
    SecurityOAuth2Properties sut;

    @Test
    void load_credentials_from_profiles() {
        SecurityOAuth2Properties.GitHub github = sut.getGithub();
        assertAll(
                () -> assertThat(github.clientId()).isEqualTo("test-github-client-id"),
                () -> assertThat(github.clientSecret()).isEqualTo("test-github-client-secret")
        );

        SecurityOAuth2Properties.Google google = sut.getGoogle();
        assertAll(
                () -> assertThat(google.clientId()).isEqualTo("test-google-client-id"),
                () -> assertThat(google.clientSecret()).isEqualTo("test-google-client-secret")
        );
    }
}
