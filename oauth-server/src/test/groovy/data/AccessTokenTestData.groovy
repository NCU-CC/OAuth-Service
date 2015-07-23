package data

import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken

trait AccessTokenTestData extends DomainTestData {

    AccessToken new_accessToken(){
        new AccessToken(
                client: getClients().findOne( 1 ),
                user: getUsers().findOne( 1 ),
                scope: [ getPermissions().findOne( 1 ) ],
                dateExpired: laterTime()
        )
    }

    AccessToken a_accessToken() {
        new AccessToken(
                id: 3,
                client: getClients().findOne( 3 ),
                user: getUsers().findOne( 3 ),
                scope: [ getPermissions().findOne( 1 ), getPermissions().findOne( 2 ) ],
                token: "Mzo6OlRPS0VO"
        )
    }

}