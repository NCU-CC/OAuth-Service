package tw.edu.ncu.cc.oauth.server.model.apiToken

import tw.edu.ncu.cc.oauth.server.model.TokenEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Transient

@Entity
class ApiToken extends TokenEntity {

    @Column( unique = true, nullable = false )
    def String encryptedToken

    @Transient
    def String token

}
