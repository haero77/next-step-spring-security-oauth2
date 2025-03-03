package nextstep.security.oauth2.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.oauth2.provider.OAuth2ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

import static nextstep.security.oauth2.provider.Oauth2Constants.LOGIN_CALL_BACK_URI_PREFIX;

public class OAuth2LoginAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final OAuth2ClientProperties oAuth2Properties;

    public OAuth2LoginAuthenticationFilter(OAuth2ClientProperties oAuth2Properties, OAuth2UserService userService) {
        this.oAuth2Properties = oAuth2Properties;
        this.authenticationManager = new ProviderManager(
                List.of(new OAuth2AuthenticationProvider(userService))
        );
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
    ) throws IOException, ServletException {
        if (!matchesPattern(request)) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationCode = request.getParameter("code");
        if (!StringUtils.hasText(authorizationCode)) {
            logger.error("Authorization code is missing");
            response.sendError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            return;
        }

        try {
            Authentication authResult = attemptAuthentication(request, new OAuth2AuthorizationCode(authorizationCode));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            response.sendRedirect("/"); // 인증 완료 후 리다이렉트
        } catch (AuthenticationException e) {
            logger.info("Authentication failed: {}", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } finally {
            SecurityContext context = SecurityContextHolder.getContext();
            securityContextRepository.saveContext(context, request, response);
            SecurityContextHolder.clearContext();
        }
    }

    private boolean matchesPattern(HttpServletRequest request) {
        return request.getRequestURI().startsWith(LOGIN_CALL_BACK_URI_PREFIX);
    }

    private Authentication attemptAuthentication(HttpServletRequest request, OAuth2AuthorizationCode code) {
        Authentication authRequest = generateUnAuthenticatedToken(request, code);
        return this.authenticationManager.authenticate(authRequest);
    }

    private OAuth2AuthenticationToken generateUnAuthenticatedToken(
            HttpServletRequest request,
            OAuth2AuthorizationCode code
    ) {
        String providerName = getProvider(request);
        OAuth2ClientProperties.Registration registration = oAuth2Properties.getRegistration().get(providerName);
        if (registration == null) {
            throw new AuthenticationException("Invalid registration: " + providerName);
        }

        OAuth2ClientProperties.Provider provider = oAuth2Properties.getProvider().get(providerName);
        if (provider == null) {
            throw new AuthenticationException("Invalid provider: " + providerName);
        }

        return OAuth2AuthenticationToken.unauthenticated(ClientRegistration.of(registration, provider), code);
    }

    private String getProvider(HttpServletRequest request) {
        String provider = request.getRequestURI().substring(LOGIN_CALL_BACK_URI_PREFIX.length());
        if (!StringUtils.hasText(provider)) {
            throw new AuthenticationException("Cannot extract provider from request URI");
        }
        return provider;
    }
}
