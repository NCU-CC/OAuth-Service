package data

import tw.edu.ncu.cc.oauth.server.model.user.User


trait ManagerTestData extends DomainTestData {

    User get_manager( int id ) {
        User user = getUsers().findOne( id )
        user.roles.find { it.name == 'admin' } == null ? null : user
    }

}