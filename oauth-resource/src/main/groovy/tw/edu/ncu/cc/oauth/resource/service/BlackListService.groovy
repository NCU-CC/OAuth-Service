package tw.edu.ncu.cc.oauth.resource.service

import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientRestrictedIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedIdObject


interface BlackListService {

    public ClientRestrictedIdObject addClient( ClientRestrictedIdObject clientRestrictedIdObject )
    public UserRestrictedIdObject   addUser( UserRestrictedIdObject userRestrictedIdObject )

}