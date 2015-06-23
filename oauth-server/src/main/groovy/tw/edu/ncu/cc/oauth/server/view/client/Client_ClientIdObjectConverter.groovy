package tw.edu.ncu.cc.oauth.server.view.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class Client_ClientIdObjectConverter implements Converter< Client, ClientIdObject >{

    @Autowired
    def SecretService secretService

    @Override
    ClientIdObject convert( Client source ) {
        ClientIdObject idClientObject = new ClientIdObject()
        idClientObject.id = secretService.encodeHashId( source.id )
        idClientObject.name = source.name
        idClientObject.description = source.description
        idClientObject.owner = source.owner.name
        idClientObject.url = source.url
        idClientObject.callback = source.callback
        idClientObject.deleted = source.deleted
        return idClientObject
    }

}
