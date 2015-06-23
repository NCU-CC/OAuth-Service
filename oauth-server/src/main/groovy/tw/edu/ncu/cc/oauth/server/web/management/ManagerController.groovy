package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.data.domain.Pageable
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.user.ManagerCreateObject
import tw.edu.ncu.cc.oauth.data.v1.management.user.ManagerObject
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.manager.ManagerOperations
import tw.edu.ncu.cc.oauth.server.validator.manager.ManagerCreateValidator

@RestController
@RequestMapping( value = "management/v1/managers" )
public class ManagerController {

    @Autowired
    def ManagerOperations managerOperations

    @Autowired
    def ConversionService conversionService

    @InitBinder
    public static void initBinder( WebDataBinder binder ) {
        binder.addValidators( new ManagerCreateValidator() );
    }

    @RequestMapping(  method = RequestMethod.GET )
    def index( Pageable pageable ) {

        def resource = managerOperations.index.process( page: pageable )

        conversionService.convert(
                resource as List< User >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( User.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( ManagerObject.class ) )
        )
    }

    @RequestMapping( value = "{userid}", method = RequestMethod.GET )
    def read( @PathVariable( "userid" ) String username ) {

        def resource = managerOperations.show.process( username: username )

        conversionService.convert(
                resource as User , ManagerObject.class
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def create( @Validated @RequestBody final ManagerCreateObject managerObject, BindingResult bindingResult ) {

        def resource = managerOperations.create.process( bindingResult, [ managerObject: managerObject ] )

        conversionService.convert(
                resource as User , ManagerObject.class
        )
    }

    @RequestMapping( value = "{userid}", method = RequestMethod.DELETE )
    def delete(  @PathVariable( "userid" ) String username ) {

        def resource = managerOperations.delete.process( username: username )

        conversionService.convert(
                resource as User , ManagerObject.class
        )
    }

}