package tw.edu.ncu.cc.oauth.server.view.clientRestricted

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientRestrictedIdObject
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class ClientRestrcited_ClientRestrictedIdObjectConverter implements Converter< ClientRestricted, ClientRestrictedIdObject >{

    @Autowired
    def SecretService secretService

    @Override
    ClientRestrictedIdObject convert( ClientRestricted source ) {
        ClientRestrictedIdObject restrictedIdObject = new ClientRestrictedIdObject()
        restrictedIdObject.client_id = source.client.serialId
        restrictedIdObject.reason = source.reason
        restrictedIdObject.last_updated = source.lastUpdated
        restrictedIdObject.date_created = source.dateCreated
        restrictedIdObject
    }

}
