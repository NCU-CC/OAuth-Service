package tw.edu.ncu.cc.oauth.server.model

import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MappedSuperclass

@MappedSuperclass
class UserTokenEntity extends TokenEntity {

    @JoinColumn
    @ManyToOne( optional = false )
    def User user

}
