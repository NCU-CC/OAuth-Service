package tw.edu.ncu.cc.oauth.server.view.accessToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenClientObject
import tw.edu.ncu.cc.oauth.server.helper.ScopeHelper
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.view.client.Client_ClientIdObjectConverter

@Component
class AccessToken_TokenClientObjectConverter implements Converter< AccessToken, TokenClientObject >{

    @Autowired
    def Client_ClientIdObjectConverter client_idClientObjectConverter

    @Override
    TokenClientObject convert( AccessToken source ) {
        TokenClientObject clientTokenObject = new TokenClientObject()
        clientTokenObject.id = source.id
        clientTokenObject.user = source.user.name
        clientTokenObject.client = convertFrom( source.client )
        clientTokenObject.scope = ScopeHelper.toStringArray( source.scope )
        clientTokenObject.last_updated = source.lastUpdated
        clientTokenObject.last_used = source.lastUsed
        clientTokenObject.date_created = source.dateCreated
        clientTokenObject
    }

    private ClientIdObject convertFrom( Client client ) {
        client == null ? null : client_idClientObjectConverter.convert( client )
    }

}

