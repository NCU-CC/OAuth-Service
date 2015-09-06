package tw.edu.ncu.cc.oauth.server.view.permission

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.permission.PermissionObject
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

@Component
class Permission_PermissionObjectConverter implements Converter< Permission, PermissionObject > {

    @Override
    PermissionObject convert( Permission source ) {
        PermissionObject permissionObject = new PermissionObject()
        permissionObject.name = source.name
        permissionObject
    }

}
