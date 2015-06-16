package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

public interface PermissionRepository extends JpaRepository< Permission, Integer > {

    Permission findByName( String name )

}
