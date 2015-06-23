package tw.edu.ncu.cc.oauth.server.service.permission

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.repository.model.PermissionRepository

@Service
class PermissionServiceImpl implements PermissionService {

    @Autowired
    def PermissionRepository permissionRepository

    @Override
    @Cacheable( value="apiService", key="'PermissionID:' + #id" )
    Permission findById( int id ) {
        permissionRepository.findOne( id )
    }

    @Override
    @Cacheable( value="apiService", key="'PermissionName:' + #name" )
    Permission findByName( String name ) {
        permissionRepository.findByName( name )
    }

    @Override
    List< Permission > findAll() {
        permissionRepository.findAll()
    }

}
