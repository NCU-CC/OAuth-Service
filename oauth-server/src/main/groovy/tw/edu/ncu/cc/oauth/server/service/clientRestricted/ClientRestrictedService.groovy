package tw.edu.ncu.cc.oauth.server.service.clientRestricted

import org.springframework.data.domain.Pageable
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted


interface ClientRestrictedService {

    ClientRestricted create( ClientRestricted clientRestricted )
    ClientRestricted update( ClientRestricted clientRestricted )
    ClientRestricted delete( ClientRestricted clientRestricted )

    List< ClientRestricted > findAll( ClientIdObject clientObject, Pageable pageable )
    ClientRestricted findByClient( Client client )

    boolean isClientRestricted( Client client )

}