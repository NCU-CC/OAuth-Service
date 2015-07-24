package tw.edu.ncu.cc.oauth.server.model.refreshToken

import tw.edu.ncu.cc.oauth.server.model.UserTokenEntity
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

import javax.persistence.*

@Entity
class RefreshToken extends UserTokenEntity {

    @Transient
    def String token

    @Column( unique = true, nullable = false )
    def String encryptedToken

    @JoinColumn( name = "access_token_id" )
    @OneToOne( optional = false )
    def AccessToken accessToken

    @ManyToMany
    @JoinTable(
        name = "refresh_token_scope",
        joinColumns = @JoinColumn( name = "refresh_token_id" ),
        inverseJoinColumns = @JoinColumn( name = "permission_id" )
    )
    def Set< Permission > scope

}
