package nextstep.security.oauth2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.authentication.*;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class OAuth2LoginAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginAuthenticationFilter.class);

    private final RequestMatcher matcher = new MvcRequestMatcher(GET, "/login/oauth2/code/github");

    private final AuthenticationManager authenticationManager;
    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityOAuth2Properties oAuth2Properties;

    public OAuth2LoginAuthenticationFilter(SecurityOAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
        this.authenticationManager = new ProviderManager(
                List.of(new OAuth2AuthenticationProvider(oAuth2Properties))
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
        if (!matcher.matches(request)) {
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
            Authentication authResult = attemptAuthentication(authorizationCode);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            response.sendRedirect("/"); // 인증 완료 후 리다이렉트
        } catch (AuthenticationException e) {
            logger.info("Authentication failed: {}", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }

    private Authentication attemptAuthentication(String authorizationCode) {
        Authentication authRequest = OAuth2AuthenticationToken.unauthenticated(
                new ClientRegistration(
                        oAuth2Properties.getGithub().clientId(),
                        oAuth2Properties.getGithub().clientSecret(),
                        authorizationCode
                )
        );

        return this.authenticationManager.authenticate(authRequest);
    }
}
