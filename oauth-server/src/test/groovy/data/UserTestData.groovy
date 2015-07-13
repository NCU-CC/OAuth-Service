package data

import tw.edu.ncu.cc.oauth.server.model.user.User


trait UserTestData extends DomainTestData {

    User get_user( int id ) {
        getUsers().findOne( id )
    }

    User unrestricted_user() {
        get_user( 1 )
    }

    User restricted_user() {
        get_user( 2 )
    }

}