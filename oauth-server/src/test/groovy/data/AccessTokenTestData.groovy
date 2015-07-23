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
                token: "3008a3bf9b3cbc298303f731c350debe08fc04eb46cff3b48d00ff43574e2f50",
                encryptedToken: "TOKEN3"
        )
    }

}