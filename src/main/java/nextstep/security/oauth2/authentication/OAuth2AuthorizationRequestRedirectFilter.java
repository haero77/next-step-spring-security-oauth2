package nextstep.security.oauth2.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.oauth2.provider.OAuth2ClientProperties;
import nextstep.security.oauth2.provider.OAuth2ClientProperties.Provider;
import nextstep.security.oauth2.provider.OAuth2ClientProperties.Registration;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2AuthorizationRequestRedirectFilter extends GenericFilterBean {

    public static final String OAUTH2_LOGIN_REQUEST_URI_PREFIX = "/oauth2/authorization/";
    private final OAuth2ClientProperties oAuth2Properties;

    public OAuth2AuthorizationRequestRedirectFilter(OAuth2ClientProperties oAuth2Properties) {
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
        if (!matchesPattern(request)) {
            chain.doFilter(request, response);
            return;
        }

        String registrationId = extractRegistrationId(request);
        Registration registration = oAuth2Properties.getRegistrationById(registrationId);
        Provider provider = oAuth2Properties.getProvider().get(registration.provider());

        String redirectUrl = generateRedirectUrl(provider, registration);

        logger.info("Redirecting to %s login: %s".formatted(registration.provider(), redirectUrl));
        response.sendRedirect(redirectUrl);
    }

    private String generateRedirectUrl(Provider provider, Registration registration) {
        return UriComponentsBuilder.fromHttpUrl(provider.authorizationUri())
                .queryParam("client_id", registration.clientId())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", registration.scope()))
                .queryParam("redirect_uri", registration.redirectUri())
                .build()
                .toUriString();
    }

    private String extractRegistrationId(HttpServletRequest request) {
        return request.getRequestURI().substring(OAUTH2_LOGIN_REQUEST_URI_PREFIX.length());
    }

    private boolean matchesPattern(HttpServletRequest request) {
        return request.getRequestURI().startsWith(OAUTH2_LOGIN_REQUEST_URI_PREFIX);
    }
}
