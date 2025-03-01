package nextstep.security.oauth2.provider;

public interface OAuth2Provider {

    String getProviderName();

    String getAccessTokenUri();

    String getUserInfoUri();

    String getUsernameAttributeName();
}
