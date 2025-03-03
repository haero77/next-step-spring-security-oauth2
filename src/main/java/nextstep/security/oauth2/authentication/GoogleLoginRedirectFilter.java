package nextstep.security.oauth2.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.oauth2.provider.OAuth2ClientProperties;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class GoogleLoginRedirectFilter extends GenericFilterBean {

    private final RequestMatcher matcher = new MvcRequestMatcher(HttpMethod.GET, "/oauth2/authorization/google"); // fixme: BASE URI + /google로 리팩토링

    private final OAuth2ClientProperties oAuth2Properties;

    public GoogleLoginRedirectFilter(OAuth2ClientProperties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        if (!matcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        String googleLoginUrl = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", oAuth2Properties.getGoogle().clientId())
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email")
                .queryParam("redirect_uri", "http://localhost:8080/login/oauth2/code/google")
                .build()
                .toUriString();

        logger.info("Redirecting to Google login: %s".formatted(googleLoginUrl));
        response.sendRedirect(googleLoginUrl);
    }
}
