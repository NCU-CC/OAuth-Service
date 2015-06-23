package tw.edu.ncu.cc.oauth.server.service.role

import tw.edu.ncu.cc.oauth.server.model.role.Role


interface RoleService {

    Role findByName( String name )

}