package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedObject
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted
import tw.edu.ncu.cc.oauth.server.operation.userBlackList.UserBlackListOperations
import tw.edu.ncu.cc.oauth.server.validator.userRestricted.UserRestrictedIdValidator

@RestController
@RequestMapping( value = "management/v1/blacklist/users" )
public class UserBlackListController {

    @Autowired
    def UserBlackListOperations userBlackListOperations

    @Autowired
    def ConversionService conversionService

    @InitBinder( 'restrictedObject' )
    public static void initBinder( WebDataBinder binder ) {
        binder.addValidators( new UserRestrictedIdValidator() );
    }

    @RequestMapping( method = RequestMethod.GET )
    def index( @RequestParam( value = "user_name", required = false ) String user_name,
               @PageableDefault( size = 20 ) Pageable pageable ) {

        def resource = userBlackListOperations.index.process(
                user_name: user_name,
                page: pageable
        )

        conversionService.convert(
                resource as List< UserRestricted >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( UserRestricted.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( UserRestrictedIdObject.class ) )
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def add( @RequestBody @Validated final UserRestrictedIdObject restrictedObject, final BindingResult bindingResult ) {

        def resource = userBlackListOperations.add.process( bindingResult, [ username: restrictedObject.user_name, reason: restrictedObject.reason ] )

        conversionService.convert(
                resource as UserRestricted, UserRestrictedIdObject.class
        )
    }

    @RequestMapping( value = "{username}", method = RequestMethod.GET )
    def show( @PathVariable( "username" ) final String username ) {

        def resource = userBlackListOperations.show.process( username: username )

        conversionService.convert(
                resource as UserRestricted, UserRestrictedIdObject.class
        )
    }

    @RequestMapping( value = "{username}", method = RequestMethod.PUT )
    def update( @PathVariable( "username" ) final String username, @RequestBody final UserRestrictedObject restrictedObject ) {

        def resource = userBlackListOperations.update.process( username: username, reason: restrictedObject.reason )

        conversionService.convert(
                resource as UserRestricted, UserRestrictedIdObject.class
        )
    }

    @RequestMapping( value = "{username}", method = RequestMethod.DELETE )
    def remove( @PathVariable( "username" ) final String username ) {

        def resource = userBlackListOperations.remove.process( username: username )

        conversionService.convert(
                resource as UserRestricted, UserRestrictedIdObject.class
        )
    }

}