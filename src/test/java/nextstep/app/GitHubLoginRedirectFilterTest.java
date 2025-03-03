package nextstep.app;

import nextstep.app.testsupport.BaseIntegrationTestSupport;
import nextstep.security.oauth2.provider.OAuth2ClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GitHubLoginRedirectFilterTest extends BaseIntegrationTestSupport {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OAuth2ClientProperties oAuth2Properties;

    @Test
    void redirectTest() throws Exception {
        String requestUri = "/oauth2/authorization/github";
        String expectedRedirectUri = "https://github.com/login/oauth/authorize" +
                "?client_id=" + oAuth2Properties.getRegistration().get("github").clientId() +
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
