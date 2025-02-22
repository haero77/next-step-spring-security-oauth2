package nextstep.app.support;

import nextstep.security.authentication.SecurityOAuth2Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class SecurityOAuth2PropertiesTest {

    @Autowired
    SecurityOAuth2Properties sut;

    @Test
    void load_github_credentials_from_profiles() {
        SecurityOAuth2Properties.GitHub github = sut.getGithub();

        assertAll(
                () -> assertThat(github.clientId()).isEqualTo("test-github-client-id"),
                () -> assertThat(github.clientSecret()).isEqualTo("test-github-client-secret")
        );
    }
}
