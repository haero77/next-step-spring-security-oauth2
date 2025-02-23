package nextstep.app.domain;

import java.util.Set;
import java.util.UUID;

public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;
    private final Set<String> roles;

    public Member(String email, String password, String name, String imageUrl, Set<String> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.roles = roles;
    }

    public static Member byOAuth2Joining(String email) {
        return new Member(
                email,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "",
                Set.of("USER")
        );
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
