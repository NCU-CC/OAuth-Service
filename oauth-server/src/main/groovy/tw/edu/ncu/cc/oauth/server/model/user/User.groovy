package tw.edu.ncu.cc.oauth.server.model.user

import org.hibernate.annotations.Where
import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.role.Role

import javax.persistence.*

@Entity
class User extends BasicEntity {

    @Column( unique = true, nullable = false )
    def String name

    @OneToMany( mappedBy = "owner", cascade = [ CascadeType.ALL ] )
    @Where( clause = "deleted = 'false'" )
    @OrderBy( "date_created desc" )
    def Set< Client > clients = new HashSet<>()

    @OneToMany( mappedBy = "user", cascade = [ CascadeType.MERGE ] )
    @Where( clause = "date_expired > CURRENT_TIMESTAMP()" )
    def Set< AccessToken > accessTokens = new HashSet<>()

    @OneToMany( mappedBy = "user", cascade = [ CascadeType.MERGE ] )
    @Where( clause = "date_expired > CURRENT_TIMESTAMP()" )
    def Set< AuthorizationCode > codes = new HashSet<>()

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn( name = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "role_id" )
    )
    def Set< Role > roles = new HashSet<>()

}
