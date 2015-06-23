package tw.edu.ncu.cc.oauth.server.service.client

import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.metamodel.Attribute

interface ClientService {

    Client create( Client client )
    Client update( Client client )
    Client delete( Client client )
    Client refreshSecret( Client client )
    Client findUndeletedBySerialId( String id )
    Client findUndeletedBySerialId( String id, Attribute... attributes )
    boolean isCredentialValid( String serialId, String secret )

    List< Client > findAllByDataObject( ClientIdObject dto )
    List< Client > findAllByDataObject( ClientIdObject dto, Attribute... attributes )
}