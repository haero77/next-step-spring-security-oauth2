package nextstep.security.oauth2;

public interface OAuth2Provider {

    String getProviderName();

    String getAccessTokenUri();

    String getUserInfoUri();

    String getUsernameAttributeName();
}
