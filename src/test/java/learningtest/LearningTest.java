package learningtest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class LearningTest {

    @ParameterizedTest
    @CsvSource({
            "/login/oauth2/code/github, github",
            "/login/oauth2/code/google, google",
    })
    void provider_extract_test(String requestUri, String expectedProvider) {
        // when
        String actualProvider = requestUri.substring(("/login/oauth2/code/").length());

        // then
        assertThat(actualProvider).isEqualTo(expectedProvider);
    }
}
