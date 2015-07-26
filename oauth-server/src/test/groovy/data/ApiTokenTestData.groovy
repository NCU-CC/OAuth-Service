package data

import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken

trait ApiTokenTestData extends DomainTestData {

    ApiToken new_apiToken(){
        new ApiToken(
                client: getClients().findOne( 1 ),
                dateExpired: laterTime()
        )
    }

    ApiToken a_apiToken() {
        new ApiToken(
                id: 3,
                client: getClients().findOne( 3 ),
                token: "404928c32a31c3bb589c1a878b54c3fe",
                encryptedToken: "TOKEN3"
        )
    }

    ApiToken trusted_apiToken() {
        a_apiToken()
    }

    ApiToken untrusted_apiToken() {
        new ApiToken(
                id: 2,
                client: getClients().findOne( 2 ),
                token: "84545f3b5cadecbe465926049b0fbe66",
                encryptedToken: "TOKEN2"
        )
    }

}