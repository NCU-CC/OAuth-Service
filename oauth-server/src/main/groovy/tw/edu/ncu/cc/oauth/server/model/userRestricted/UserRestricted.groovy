package tw.edu.ncu.cc.oauth.server.model.userRestricted

import tw.edu.ncu.cc.oauth.server.model.BasicEntity
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class UserRestricted extends BasicEntity {

    @JoinColumn
    @OneToOne( optional = false )
    def User user

    @Column
    def String reason = ""

}
