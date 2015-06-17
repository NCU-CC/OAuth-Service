package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdSecretObject
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenObject
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.client.ClientOperations
import tw.edu.ncu.cc.oauth.server.validator.client.ClientValidator

@RestController
@RequestMapping( value = "management/v1/clients" )
public class ClientController {

    @Autowired
    def ClientOperations clientOperations

    @Autowired
    def ConversionService conversionService

    @InitBinder
    public static void initBinder( WebDataBinder binder ) {
        binder.addValidators( new ClientValidator() );
    }

    /**
     * ·j´Mclients
     * <code>/clients?name={clientName}&id={clientId}&owner={portalId}&isDeleted={true or false}</code>
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    def search(@RequestParam(value = "id", required = false) String id,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "owner", required = false) String owner,
                                 @RequestParam(value = "isDeleted", defaultValue = "false") Boolean isDeleted) {

        def resource = clientOperations.search.process( id: id, name: name, owner: owner, isDeleted: isDeleted )

        conversionService.convert(
                resource as List< Client >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( Client.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( ClientObject.class ) )
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def create( @RequestBody @Validated final ClientObject clientObject, BindingResult bindingResult ) {

        def resource = clientOperations.create.process( bindingResult, [ clientObject: clientObject ] )

        conversionService.convert(
                resource as Client, ClientIdSecretObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.GET )
    def get( @PathVariable( "id" ) final String clientId ) {

        def resource = clientOperations.show.process( serialId: clientId )

        conversionService.convert(
                resource as Client, ClientIdSecretObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.PUT )
    def update( @PathVariable( "id" ) final String clientId,
                @RequestBody @Validated  final ClientObject clientObject, final BindingResult bindingResult ) {

        def resource = clientOperations.update.process( bindingResult, [ serialId: clientId, clientObject: clientObject ] )

        conversionService.convert(
                resource as Client, ClientIdSecretObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.DELETE )
    def delete( @PathVariable( "id" ) final String clientId ) {

        def resource = clientOperations.delete.process( serialId: clientId )

        conversionService.convert(
                resource as Client, ClientIdSecretObject.class
        )
    }

    @RequestMapping( value = "{id}/refresh_secret", method = RequestMethod.POST )
    def refresh( @PathVariable( "id" ) final String clientId ) {

        def resource = clientOperations.refreshSecret.process( serialId: clientId )

        conversionService.convert(
                resource as Client, ClientIdSecretObject.class
        )
    }

    @RequestMapping( value = "{id}/api_tokens", method = RequestMethod.GET )
    def getApiTokens( @PathVariable( "id" ) final String clientId ) {

        def resource = clientOperations.showApiTokens.process( serialId: clientId )

        conversionService.convert(
                resource as Set< ApiToken >,
                TypeDescriptor.collection( Set.class, TypeDescriptor.valueOf( ApiToken.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( ApiTokenObject.class ) )
        )
    }

}