package tw.edu.ncu.cc.oauth.server.service.permission

import tw.edu.ncu.cc.oauth.server.model.permission.Permission

interface PermissionService {

    Permission create( Permission permission )
    Permission delete( Permission permission )
    Permission findById( int id )
    Permission findByName( String name )
    List< Permission > findAll()

}