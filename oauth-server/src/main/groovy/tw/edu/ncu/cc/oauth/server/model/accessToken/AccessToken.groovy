package tw.edu.ncu.cc.oauth.server.model.accessToken

import tw.edu.ncu.cc.oauth.server.model.UserTokenEntity
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

import javax.persistence.*

@Entity
public class AccessToken extends UserTokenEntity {

    @Transient
    def String token

    @Column( unique = true, nullable = false )
    def String encryptedToken

    @ManyToMany
    @JoinTable(
        name = "access_token_scope",
        joinColumns = @JoinColumn( name = "access_token_id" ),
        inverseJoinColumns = @JoinColumn( name = "permission_id" )
    )
    def Set< Permission > scope

}
