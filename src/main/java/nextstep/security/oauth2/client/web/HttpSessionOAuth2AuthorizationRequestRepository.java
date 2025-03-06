package nextstep.security.oauth2.client.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class HttpSessionOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository {

    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME =
            HttpSessionOAuth2AuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";

    private final String sessionAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;

    @Override
    @Nullable
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return (OAuth2AuthorizationRequest) session.getAttribute(this.sessionAttributeName);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        request.getSession().setAttribute(this.sessionAttributeName, authorizationRequest);
    }

    @Override
    @Nullable
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        if (authorizationRequest == null) {
            return null;
        }

        request.getSession().removeAttribute(this.sessionAttributeName);
        return authorizationRequest;
    }
}
