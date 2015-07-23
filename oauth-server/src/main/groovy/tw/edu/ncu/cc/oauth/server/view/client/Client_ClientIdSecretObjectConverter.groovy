package tw.edu.ncu.cc.oauth.server.view.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdSecretObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class Client_ClientIdSecretObjectConverter implements Converter< Client, ClientIdSecretObject >{

    @Autowired
    def SecretService secretService

    @Override
    ClientIdSecretObject convert( Client source ) {
        ClientIdSecretObject secretIdClientObject = new ClientIdSecretObject()
        secretIdClientObject.id = source.serialId
        secretIdClientObject.secret = secretService.encryptQueryable( source.encryptedSecret )
        secretIdClientObject.name = source.name
        secretIdClientObject.description = source.description
        secretIdClientObject.owner = source.owner.name
        secretIdClientObject.url = source.url
        secretIdClientObject.callback = source.callback
        secretIdClientObject.deleted = source.deleted
        secretIdClientObject.last_updated = source.lastUpdated
        secretIdClientObject.date_created = source.dateCreated
        secretIdClientObject
    }

}
