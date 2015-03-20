package tw.edu.ncu.cc.oauth.server.domain

import grails.persistence.Entity
import tw.edu.ncu.cc.oauth.server.domain.concern.Auditable
import tw.edu.ncu.cc.oauth.server.domain.concern.Lazyable

@Entity
class Permission implements Auditable, Lazyable {

    String name

    static hasMany = [
        accessTokens: AccessToken,
        authorizationCodes: AuthorizationCode,
        refreshTokens: RefreshToken
    ]

    static belongsTo = [
        AccessToken, AuthorizationCode, RefreshToken
    ]

    static attrFetchModes =  [
        'accessTokens'       : 'eager',
        'authorizationCodes' : 'eager',
        'refreshTokens'      : 'eager'
    ]

}
