package nextstep.security.oauth2;

public interface OAuth2Provider {

    String getProviderName();

    String getClientId();

    String getClientSecret();
}
