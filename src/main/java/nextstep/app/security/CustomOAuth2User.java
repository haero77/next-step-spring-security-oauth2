package nextstep.app.security;

import nextstep.security.oauth2.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final String id;

    public CustomOAuth2User(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return id;
    }
}
