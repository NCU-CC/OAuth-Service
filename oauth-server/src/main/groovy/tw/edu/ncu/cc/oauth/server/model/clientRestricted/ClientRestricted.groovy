package tw.edu.ncu.cc.oauth.server.model.clientRestricted

import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class ClientRestricted extends BasicEntity {

    @JoinColumn
    @OneToOne( optional = false )
    def Client client

    @Column
    def String reason = ""

}
