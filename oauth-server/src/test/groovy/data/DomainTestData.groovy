package data

import org.springframework.beans.factory.annotation.Autowired
import tw.edu.ncu.cc.oauth.server.repository.model.*

trait DomainTestData {

    @Autowired
    def AccessTokenRepository AccessTokens

    @Autowired
    def ApiTokenRepository ApiTokens

    @Autowired
    def AuthorizationCodeRepository AuthorizationCodes

    @Autowired
    def RefreshTokenRepository RefreshTokens

    @Autowired
    def ClientRepository Clients

    @Autowired
    def PermissionRepository Permissions

    @Autowired
    def UserRepository Users

    @Autowired
    def RoleRepository Roles

    public static Date laterTime() {
        return new Date( System.currentTimeMillis() + 1000000 )
    }

}