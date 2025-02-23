package nextstep.security.oauth2;

public class CustomOAuth2User implements OAuth2User {

    private final String username;

    public CustomOAuth2User(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
