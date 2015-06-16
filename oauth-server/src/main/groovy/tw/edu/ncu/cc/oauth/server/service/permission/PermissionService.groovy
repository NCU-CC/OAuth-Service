package tw.edu.ncu.cc.oauth.server.service.permission

import tw.edu.ncu.cc.oauth.server.model.permission.Permission

interface PermissionService {
    Permission findById( int id )
    Permission findByName( String name )
    List< Permission > findAll()
}