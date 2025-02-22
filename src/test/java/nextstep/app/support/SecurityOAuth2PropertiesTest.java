package nextstep.app.support;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityOAuth2PropertiesTest {

    @Autowired
    SecurityOAuth2Properties sut;

    @Test
    void load_github_credentials_from_profiles() {
        // given
        String githubClientId = sut.getGithub().getClientId();
        assertThat(githubClientId).isEqualTo("test-github-client-id");
    }
}
