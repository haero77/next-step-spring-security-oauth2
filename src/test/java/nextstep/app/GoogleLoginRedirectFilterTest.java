package nextstep.app;

import nextstep.app.testsupport.BaseIntegrationTestSupport;
import nextstep.security.oauth2.provider.SecurityOAuth2Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GoogleLoginRedirectFilterTest extends BaseIntegrationTestSupport {

    @Autowired
    SecurityOAuth2Properties oAuth2Properties;

    @Test
    void redirectTest() throws Exception {
        String requestUri = "/oauth2/authorization/google";
        String expectedRedirectUri = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + oAuth2Properties.getGoogle().clientId() +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email" +
                "&redirect_uri=http://localhost:8080/login/oauth2/code/google";

        ResultActions result = mockMvc.perform(get(requestUri))
                .andDo(print());

        result
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUri));
    }
}
