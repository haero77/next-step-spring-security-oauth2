package nextstep.security.oauth2.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.oauth2.provider.SecurityOAuth2Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class GitHubLoginRedirectFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLoginRedirectFilter.class);

    private final RequestMatcher matcher = new MvcRequestMatcher(HttpMethod.GET, "/oauth2/authorization/github");
    private final SecurityOAuth2Properties oAuth2Properties;

    public GitHubLoginRedirectFilter(SecurityOAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (matcher.matches((HttpServletRequest) servletRequest)) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);

            String githubLoginUrl = UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                    .queryParam("client_id", oAuth2Properties.getGithub().clientId())
                    .queryParam("response_type", "code")
                    .queryParam("scope", "read:user")
                    .queryParam("redirect_uri", "http://localhost:8080/login/oauth2/code/github")
                    .build()
                    .toUriString();

            logger.info("Redirecting to GitHub login: {}", githubLoginUrl);
            response.sendRedirect(githubLoginUrl);
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
    }
}
