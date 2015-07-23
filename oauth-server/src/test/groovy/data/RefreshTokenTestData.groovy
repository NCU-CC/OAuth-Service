package data

import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken

trait RefreshTokenTestData extends DomainTestData {

    RefreshToken a_refreshToken() {
        new RefreshToken(
                id: 3,
                client: getClients().findOne( 3 ),
                user: getUsers().findOne( 3 ),
                scope: [ getPermissions().findOne( 1 ), getPermissions().findOne( 3 ) ],
                token: "3008a3bf9b3cbc298303f731c350debe08fc04eb46cff3b48d00ff43574e2f50",
                encryptedToken: "TOKEN3"
        )
    }

}