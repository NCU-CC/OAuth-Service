package tw.edu.ncu.cc.oauth.server.model.tokenAccessLog

import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class TokenAccessLog extends BasicEntity {

    @Column
    def String tokenType

    @Column
    def Integer tokenId

    @JoinColumn
    @ManyToOne( optional = false )
    def Client client

    @Column
    def String ip

    @Column
    def String referer

    @Column
    def String application

}
