package nextstep.app;

import nextstep.security.authentication.SecurityOAuth2Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GitHubLoginRedirectFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SecurityOAuth2Properties oAuth2Properties;

    @Test
    void redirectTest() throws Exception {
        String requestUri = "/oauth2/authorization/github";
        String expectedRedirectUri = "https://github.com/login/oauth/authorize" +
                "?client_id=" + oAuth2Properties.getGithub().clientId() +
                "&response_type=code" +
                "&scope=read:user" +
                "&redirect_uri=http://localhost:8080/login/oauth2/code/github";

        ResultActions result = mockMvc.perform(get(requestUri))
                .andDo(print());

        result
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUri));
    }
}
