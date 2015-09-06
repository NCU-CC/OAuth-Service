package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService

@Transactional
class PermissionServiceImplTest extends SpringSpecification {

    @Autowired
    private PermissionService permissionService

    def "it can read all permissions"() {
        expect:
            permissionService.findAll().size() == 3
    }

    def "it can read permission by id"() {
        given:
            def permission = a_permission()
        expect:
            permissionService.findById( permission.id ).name == permission.name
    }

    def "it can read permission by name"() {
        given:
            def permission = a_permission()
        expect:
            permissionService.findByName( permission.name ).id == permission.id
    }

    def "it can create permission"() {
        given:
            def permission = new Permission( name: "TESTNAME" )
        when:
            permissionService.create( permission )
        then:
            permissionService.findByName( permission.name ) != null
    }

    def "it can delete permission"() {
        given:
            def permission = a_permission()
        when:
            permissionService.delete( permission )
        then:
            permissionService.findByName( permission.name ) == null
    }

}
