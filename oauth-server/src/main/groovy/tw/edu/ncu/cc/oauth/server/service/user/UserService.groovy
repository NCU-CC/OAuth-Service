package tw.edu.ncu.cc.oauth.server.service.user

import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.Attribute

interface UserService {
    User create( User user )
    User update( User user )
    User findByName( String name )
    User findByName( String name, Attribute...attributes )

    List< User > findAllByNameLike( String userName )
    List< User > findAllByNameLike( String userName, Attribute...attributes )
}