package nextstep.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8089)
class OAuth2LoginAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupMockServer() throws Exception {
        stubForAccessToken();
//        stubForUser();
    }

    @Test
    void redirectAndRequestGithubAccessToken() throws Exception {
        String requestUri = "/login/oauth2/code/github?code=mock_code";

        ResultActions result = mockMvc.perform(get(requestUri))
                .andDo(print());

        result
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andExpect(request -> {
                    HttpSession session = request.getRequest().getSession();
                    assert session != null;
                    SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                    assertThat(context).isNotNull();
                    assertThat(context.getAuthentication()).isNotNull();
                    assertThat(context.getAuthentication().isAuthenticated()).isTrue();
//                    assertThat(context.getAuthentication().getPrincipal()).isEqualTo("a@a.com");
                });
    }

    private void stubForAccessToken() throws JsonProcessingException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", "mock_access_token");
        responseBody.put("token_type", "bearer");
        String jsonResponse = new ObjectMapper().writeValueAsString(responseBody);

        stubFor(post(urlPathEqualTo("/login/oauth/access_token"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withBody(jsonResponse)));
    }

//    private void stubForUser() throws JsonProcessingException {
//        Map<String, String> userProfile = new HashMap<>();
//        userProfile.put("email", "a@a.com");
//        userProfile.put("name", "a");
//        userProfile.put("avatar_url", "");
//        String profileJsonResponse = new ObjectMapper().writeValueAsString(userProfile);
//
//        stubFor(get(urlEqualTo("/user"))
//                .willReturn(aResponse()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
//                        .withBody(profileJsonResponse)));
//    }
}
