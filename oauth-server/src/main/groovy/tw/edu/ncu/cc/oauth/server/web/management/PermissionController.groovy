package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.permission.PermissionObject
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.operation.permission.PermissionOperations
import tw.edu.ncu.cc.oauth.server.validator.permission.PermissionValidator

@RestController
@RequestMapping( value = "management/v1/permissions" )
public class PermissionController {

    @Autowired
    def PermissionOperations permissionOperations

    @Autowired
    def ConversionService conversionService

    @InitBinder
    public static void initBinder( WebDataBinder binder ) {
        binder.addValidators( new PermissionValidator() );
    }

    @RequestMapping( method = RequestMethod.GET )
    def search() {

        def resource = permissionOperations.index.process()

        conversionService.convert(
                resource as List< Permission >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( Permission.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( PermissionObject.class ) )
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def create( @RequestBody @Validated final PermissionObject permissionObject, BindingResult bindingResult ) {

        def resource = permissionOperations.create.process( bindingResult, [ permissionObject: permissionObject ] )

        conversionService.convert(
                resource as Permission, PermissionObject.class
        )
    }

    @RequestMapping( value = "{name}", method = RequestMethod.GET )
    def show( @PathVariable( "name" ) final String name ) {

        def resource = permissionOperations.show.process( name: name )

        conversionService.convert(
                resource as Permission, PermissionObject.class
        )
    }

    @RequestMapping( value = "{name}", method = RequestMethod.DELETE )
    def destroy( @PathVariable( "name" ) final String name ) {

        def resource = permissionOperations.delete.process( name: name )

        conversionService.convert(
                resource as Permission, PermissionObject.class
        )
    }

}