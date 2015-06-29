package tw.edu.ncu.cc.oauth.server.service.clientRestricted

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestrictedSpecifications
import tw.edu.ncu.cc.oauth.server.repository.model.ClientRestrcitedRepository
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.security.SecretService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Service
class ClientRestrictedServiceImpl implements ClientRestrictedService {

    @Autowired
    def UserService userService

    @Autowired
    def SecretService secretService

    @Autowired
    def ClientService clientService

    @Autowired
    def ClientRestrcitedRepository clientRestrcitedRepository

    @Override
    ClientRestricted create( ClientRestricted clientRestricted ) {
        clientService.revokeTokens( clientRestricted.client )
        clientRestrcitedRepository.save( clientRestricted )
    }

    @Override
    ClientRestricted update( ClientRestricted clientRestricted ) {
        clientRestrcitedRepository.save( clientRestricted )
    }

    @Override
    ClientRestricted delete( ClientRestricted clientRestricted ) {
        clientRestrcitedRepository.delete( clientRestricted.id )
        clientRestricted
    }

    @Override
    List< ClientRestricted > findAll( ClientIdObject clientObject, Pageable pageable ) {
        clientRestrcitedRepository.findAll(
                ClientRestrictedSpecifications.attributes(
                        StringUtils.isEmpty( clientObject.id ) ? null : secretService.decodeHashId( clientObject.id ) as String,
                        clientObject.name,
                        StringUtils.isEmpty( clientObject.owner ) ? null : userService.findByName( clientObject.owner ),
                        clientObject.deleted
                ),
                pageable
        ).content
    }

    @Override
    ClientRestricted findByClient( Client client ) {
        clientRestrcitedRepository.findOne(
                ClientRestrictedSpecifications.clientEquals( client )
        )
    }

    @Override
    boolean isClientRestricted( Client client ) {
        findByClient( client ) != null
    }

}