package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.service.user.UserService

class UserServiceImplTest extends SpringSpecification {

    @Autowired
    private UserService userService

    @Transactional
    def "it can create user"() {
        when:
            userService.create( new User( name: "newuser" ) )
        then:
            userService.findByName( "newuser" ) != null
    }

    @Transactional
    def "it can update user"() {
        given:
            User user = get_user( 1 )
        when:
            user.name = "ADMINTEST"
        and:
            userService.update( user )
        then:
            userService.findByName( "ADMINTEST" ) != null
    }

    @Transactional
    def "it can find user by partial username"() {
        when:
            def username = "fakeusername"
            userService.create( new User(
                    name: username
            ) )
            def partialUsername = username.substring( username.length() - 1 )
        then:
            userService.findAllByNameLike( partialUsername ) != null
    }
}
