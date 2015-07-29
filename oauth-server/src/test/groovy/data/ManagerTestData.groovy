package data

import tw.edu.ncu.cc.oauth.server.model.user.User


trait ManagerTestData extends DomainTestData {

    User get_manager( int id ) {
        User user = getUsers().findOne( id )
        user.roles.find { it.name == 'admin' } == null ? null : user
    }

    User a_manager() {
        new User(
                name: "ADMIN1",
                roles: [ getRoles().findOne( 1 ) ]
        )
    }

    User a_common_user() {
        new User(
                name: "ADMIN2"
        )
    }

}