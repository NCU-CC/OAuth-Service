package tw.edu.ncu.cc.oauth.server.model

import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.*

@MappedSuperclass
class TokenEntity extends BasicEntity {

    @JoinColumn
    @ManyToOne( optional = false )
    def Client client

    @Temporal( value = TemporalType.TIMESTAMP )
    def Date dateExpired

    @Temporal( value = TemporalType.TIMESTAMP )
    def Date lastUsed

    def revoke() {
        dateExpired = new Date()
    }

    def refreshLastUsedTime() {
        lastUsed = new Date()
    }

}
