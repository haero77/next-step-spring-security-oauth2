package nextstep.security.authentication;

public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // todo OAuth2 액세스 토큰 발급 로직 구현
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
