package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdSecretObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenClientObject
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.user.UserOperations
import tw.edu.ncu.cc.oauth.server.validator.user.UserValidator

@RestController
@RequestMapping( value = "management/v1/users" )
public class UserController {

    @Autowired
    def UserOperations userOperations

    @Autowired
    def ConversionService conversionService

    @InitBinder
    public static void initBinder( WebDataBinder binder ) {
        binder.addValidators( new UserValidator() )
    }

    /**
     * 部份搜尋使用者帳號
     * /users?name={userName}*
     * @return
     */
    @RequestMapping( method = RequestMethod.GET )
    def search( @RequestParam( value = "name", required = true ) String username ) {

        def resource = userOperations.search.process( username: username )

        conversionService.convert(
                resource as List< User >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( User.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( UserIdObject.class ) )
        )
    }

    @RequestMapping( value = "{userName}", method = RequestMethod.GET )
    def getUser( @PathVariable( "userName" ) final String username ) {

        def resource = userOperations.show.process( username: username )

        conversionService.convert(
                resource as User, UserIdObject.class
        )
    }

    @RequestMapping( value = "{userName}/authorized_tokens", method = RequestMethod.GET )
    def showAuthorizedTokens( @PathVariable( "userName" ) final String username ) {

        def resource = userOperations.showAuthorizedTokens.process( username: username )

        conversionService.convert(
                resource as List< RefreshToken >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( RefreshToken.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( TokenClientObject.class ) )
        )
    }

    @RequestMapping( value = "{userName}/clients", method = RequestMethod.GET )
    def getOwnedClients( @PathVariable( "userName" ) final String username ) {

        def resource = userOperations.showOwnedClients.process( username: username )

        conversionService.convert(
                resource as Set< Client >,
                TypeDescriptor.collection( Set.class, TypeDescriptor.valueOf( Client.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( ClientIdSecretObject.class ) )
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def createIfNotExist( @Validated @RequestBody final UserObject userObject, BindingResult bindingResult ) {

        def resource = userOperations.createIfNotExist.process( bindingResult, [ userObject: userObject ] )

        conversionService.convert(
                resource as User, UserObject.class
        )
    }

}