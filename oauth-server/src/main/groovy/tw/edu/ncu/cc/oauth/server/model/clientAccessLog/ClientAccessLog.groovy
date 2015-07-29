package tw.edu.ncu.cc.oauth.server.model.clientAccessLog

import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class ClientAccessLog extends BasicEntity {

    @JoinColumn
    @ManyToOne( optional = false )
    def Client client

    @JoinColumn
    @ManyToOne( optional = false )
    def Client application

    @Column
    def Integer accessTimesPerMonth = 0

}
