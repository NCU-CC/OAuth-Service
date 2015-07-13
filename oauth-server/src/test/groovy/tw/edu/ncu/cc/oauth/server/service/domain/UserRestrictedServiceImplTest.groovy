package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserIdObject
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted
import tw.edu.ncu.cc.oauth.server.repository.model.UserRestrictedRepository
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService
import tw.edu.ncu.cc.oauth.server.service.user.UserService
import tw.edu.ncu.cc.oauth.server.service.userRestricted.UserRestrictedService

class UserRestrictedServiceImplTest extends SpringSpecification {

    @Autowired
    def UserService userService

    @Autowired
    def UserRestrictedRepository userRestrictedRepository

    @Autowired
    def UserRestrictedService userRestrictedService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Transactional
    def "it can restrict an user"() {
        given:
            def user = unrestricted_user()
        when:
            userRestrictedService.create(
                new UserRestricted(
                        user: user,
                        reason: 'none'
                )
            )
        then:
            userRestrictedService.findByUser( user ) != null
        and:
            user.clients.each {
                assert clientRestrictedService.isClientRestricted( it )
            }
    }

    @Transactional
    def "it can update user restrict information"() {
        given:
            def userRestricted = a_userRestricted()
        and:
            def originReason = userRestricted.reason
        when:
            userRestricted.reason = originReason + "++"
        and:
            userRestrictedService.update( userRestricted )
        then:
            userRestrictedRepository.findOne( userRestricted.id ).reason != originReason
    }

    @Transactional
    def "it can unrestrict an user"() {
        given:
            def userRestricted = a_userRestricted()
        when:
            userRestrictedService.delete( userRestricted )
        then:
            userRestrictedRepository.findOne( userRestricted.id ) == null
    }

    @Transactional
    def "it can find all restricted users by some attributes"() {
        when:
            def results = userRestrictedService.findAll( new UserIdObject(), new PageRequest( 0, 3 ) )
        then:
            results.size() != 0
    }

    @Transactional
    def "it can check if an user is restricted"() {
        expect:
            ! userRestrictedService.isUserRestricted( unrestricted_user() )
            userRestrictedService.isUserRestricted( restricted_user() )
    }

}
