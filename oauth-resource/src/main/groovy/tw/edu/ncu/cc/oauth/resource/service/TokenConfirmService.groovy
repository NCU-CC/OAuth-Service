package tw.edu.ncu.cc.oauth.resource.service

import tw.edu.ncu.cc.oauth.data.v1.management.resource.TokenRequestMetaObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject

public interface TokenConfirmService {
    public TokenObject readAccessToken( String accessToken, TokenRequestMetaObject metaObject );
    public ApiTokenClientObject readApiToken( String apiToken, TokenRequestMetaObject metaObject );
}
