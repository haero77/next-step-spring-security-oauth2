package nextstep.security.oauth2;

public interface OAuth2ProviderClient {

    String getProviderName();

    OAuth2AccessToken fetchAccessToken(ClientRegistration registration, OAuth2AuthorizationCode code);

    OAuth2User fetchUser(ClientRegistration registration, OAuth2AccessToken accessToken);
}
