package tw.edu.ncu.cc.oauth.server.service.apiToken

import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken

import javax.persistence.metamodel.Attribute

interface ApiTokenService {

    ApiToken create( ApiToken apiToken )
    ApiToken refreshToken( ApiToken apiToken )
    ApiToken revoke( ApiToken apiToken )
    ApiToken refreshLastUsedTime( ApiToken apiToken )
    ApiToken findUnexpiredByToken( String token, Attribute...attributes )
    ApiToken findUnexpiredByToken( String token )
    ApiToken findUnexpiredById( String id, Attribute...attributes )
    ApiToken findUnexpiredById( String id )

}