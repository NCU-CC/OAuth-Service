package data

import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted

trait UserRestrictedTestData extends DomainTestData {

    UserRestricted a_userRestricted() {
        new UserRestricted(
                id: 1,
                user: getUsers().findOne( 2 ),
                reason: "reason1"
        )
    }

}