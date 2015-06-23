package tw.edu.ncu.cc.oauth.server.model.authorizationCode

import tw.edu.ncu.cc.oauth.server.model.TokenEntity
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

import javax.persistence.*

@Entity
class AuthorizationCode extends TokenEntity {

    @Transient
    def String code

    @Column( unique = true, nullable = false )
    def String encryptedCode

    @ManyToMany
    @JoinTable(
        name = "authorization_code_scope",
        joinColumns = @JoinColumn( name = "authorization_code_id" ),
        inverseJoinColumns = @JoinColumn( name = "permission_id" )
    )
    def Set< Permission > scope

}