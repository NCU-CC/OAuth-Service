package tw.edu.ncu.cc.oauth.server.model.apiToken

import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.*

@Entity
class ApiToken extends BasicEntity {

    @Column( unique = true, nullable = false )
    def String encryptedToken

    @Transient
    def String token

    @JoinColumn
    @ManyToOne( optional = false )
    def Client client

    @Temporal( value = TemporalType.TIMESTAMP )
    def Date dateExpired

    def revoke() {
        dateExpired = new Date( System.currentTimeMillis() )
    }

}
