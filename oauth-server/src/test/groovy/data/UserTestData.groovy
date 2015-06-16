package data

import tw.edu.ncu.cc.oauth.server.model.user.User


trait UserTestData extends DomainTestData {

    User get_user( int id ) {
        getUsers().findOne( id )
    }

}