package tw.edu.ncu.cc.oauth.resource.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute
import tw.edu.ncu.cc.oauth.data.v1.management.resource.TokenRequestMetaObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.resource.config.RemoteConfig

public class TokenConfirmServiceImpl implements TokenConfirmService {

    def RemoteConfig config

    private RestTemplate restTemplate

    public TokenConfirmServiceImpl( RemoteConfig config, RestTemplate restTemplate ) {
        this.config = config
        this.restTemplate = restTemplate
    }

    @Override
    public TokenObject readAccessToken( String accessToken, TokenRequestMetaObject metaObject ) {
        String targetUrl = config.serverPath + config.accessTokenPath
        getTokenWithType( targetUrl, accessToken, metaObject, TokenObject.class ).getBody()
    }

    @Override
    ApiTokenClientObject readApiToken( String apiToken, TokenRequestMetaObject metaObject ) {
        String targetUrl = config.serverPath + config.apiTokenPath
        getTokenWithType( targetUrl, apiToken, metaObject, ApiTokenClientObject.class ).getBody()
    }

    private <T> ResponseEntity<T> getTokenWithType( String url, String token, TokenRequestMetaObject metaObject, Class<T> responseType ) {
        restTemplate.exchange(
                url + "/" + token + "?ip={ip}&referer={referer}",
                HttpMethod.GET,
                headers(),
                responseType,
                metaObject.ip, metaObject.referer
        )
    }

    private HttpEntity headers() {
        HttpHeaders headers = new HttpHeaders()
        headers.add( RequestAttribute.API_TOKEN_HEADER, config.apiToken )
        new HttpEntity( headers )
    }

}
