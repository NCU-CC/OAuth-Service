package tw.edu.ncu.cc.oauth.server.service.userRestricted

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestrictedSpecifications
import tw.edu.ncu.cc.oauth.server.repository.model.UserRestrictedRepository
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

@Service
class UserRestrictedServiceImpl implements UserRestrictedService {

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Autowired
    def UserRestrictedRepository userRestrictedRepository

    @Override
    UserRestricted create( UserRestricted userRestricted ) {
        for( Client client : userRestricted.user.clients ) {
            if( ! clientRestrictedService.isClientRestricted( client ) ) {
                ClientRestricted clientRestricted = new ClientRestricted()
                clientRestricted.client = client
                clientRestricted.reason = userRestricted.reason
                clientRestrictedService.create( clientRestricted )
            }
        }
        userRestrictedRepository.save( userRestricted )
    }

    @Override
    UserRestricted update( UserRestricted userRestricted ) {
        userRestrictedRepository.save( userRestricted )
    }

    @Override
    UserRestricted delete( UserRestricted userRestricted ) {
        userRestrictedRepository.delete( userRestricted.id )
        userRestricted
    }

    @Override
    List< UserRestricted > findAll( UserIdObject userObject, Pageable pageable ) {
        userRestrictedRepository.findAll(
                UserRestrictedSpecifications.attributes( userObject.name ),
                pageable
        ).content
    }

    @Override
    UserRestricted findByUser( User user ) {
        userRestrictedRepository.findOne(
                UserRestrictedSpecifications.userEquals( user )
        )
    }

    @Override
    boolean isUserRestricted( User user ) {
        findByUser( user ) != null
    }

}
