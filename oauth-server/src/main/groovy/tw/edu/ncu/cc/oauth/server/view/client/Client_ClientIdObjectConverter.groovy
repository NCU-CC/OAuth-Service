package tw.edu.ncu.cc.oauth.server.view.client

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client

@Component
class Client_ClientIdObjectConverter implements Converter< Client, ClientIdObject >{

    @Override
    ClientIdObject convert( Client source ) {
        ClientIdObject idClientObject = new ClientIdObject()
        idClientObject.id = source.serialId
        idClientObject.name = source.name
        idClientObject.description = source.description
        idClientObject.owner = source.owner.name
        idClientObject.url = source.url
        idClientObject.callback = source.callback
        idClientObject.deleted = source.deleted
        idClientObject.trusted = source.trusted
        idClientObject.last_updated = source.lastUpdated
        idClientObject.date_created = source.dateCreated
        idClientObject
    }

}
