package tw.edu.ncu.cc.oauth.server.model.client

import org.hibernate.annotations.Where
import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.*

@Entity
class Client extends BasicEntity {

    @Column( nullable = false, unique = true )
    def String serialId

    @Column( nullable = false )
    def String encryptedSecret

    @Transient
    def String secret

    @Column( nullable = false )
    def String name

    @Column
    def String description = ""

    @Column
    def String url = ""

    @Column( nullable = false )
    def String callback

    @Column
    def Boolean deleted = false

    @JoinColumn
    @ManyToOne( optional = false )
    def User owner

    @OneToMany( mappedBy = "client", cascade = [ CascadeType.PERSIST, CascadeType.MERGE ] )
    @Where( clause = "date_expired > CURRENT_TIMESTAMP()" )
    def Set< ApiToken > apiTokens = new HashSet<>()

    @OneToMany( mappedBy = "client", cascade = [ CascadeType.PERSIST, CascadeType.MERGE ] )
    @Where( clause = "date_expired > CURRENT_TIMESTAMP()" )
    def Set< RefreshToken > refreshTokens = new HashSet<>()

    @OneToMany( mappedBy = "client", cascade = [ CascadeType.PERSIST, CascadeType.MERGE ] )
    @Where( clause = "date_expired > CURRENT_TIMESTAMP()" )
    def Set< AccessToken > accessTokens = new HashSet<>()

    @OneToMany( mappedBy = "client", cascade = [ CascadeType.PERSIST, CascadeType.MERGE ] )
    @Where( clause = "date_expired > CURRENT_TIMESTAMP()" )
    def Set< AuthorizationCode > codes = new HashSet<>()

}
