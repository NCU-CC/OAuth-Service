package data

import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted

trait ClientRestrictedTestData extends DomainTestData {

    ClientRestricted a_clientRestricted() {
        new ClientRestricted(
                id: 1,
                client: getClients().findOne( 2 ),
                reason: "reason1"
        )
    }

}