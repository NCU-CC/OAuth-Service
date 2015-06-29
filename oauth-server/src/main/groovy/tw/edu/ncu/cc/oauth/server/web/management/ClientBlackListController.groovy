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
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientRestrictedIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientRestrictedObject
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.operation.clientBlackList.ClientBlackListOperations
import tw.edu.ncu.cc.oauth.server.validator.clientRestricted.ClientRestrictedIdValidator

@RestController
@RequestMapping( value = "management/v1/blacklist/clients" )
public class ClientBlackListController {

    @Autowired
    def ClientBlackListOperations clientBlackListOperations

    @Autowired
    def ConversionService conversionService

    @InitBinder( 'restrictedObject' )
    public static void initBinder( WebDataBinder binder ) {
        binder.addValidators( new ClientRestrictedIdValidator() );

    }

    @RequestMapping( method = RequestMethod.GET )
    def index( @RequestParam( value = "id", required = false ) String id,
               @RequestParam( value = "name", required = false ) String name,
               @RequestParam( value = "owner", required = false ) String owner,
               @RequestParam( value = "deleted", defaultValue = "false" ) Boolean deleted,
               @PageableDefault( size = 20 ) Pageable pageable ) {

        def resource = clientBlackListOperations.index.process(
                clientIdObject: new ClientIdObject(
                        id: id,
                        name: name,
                        owner: owner,
                        deleted: deleted
                ),
                page: pageable
        )

        conversionService.convert(
                resource as List< ClientRestricted >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( ClientRestricted.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( ClientRestrictedIdObject.class ) )
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def add( @RequestBody @Validated final ClientRestrictedIdObject restrictedObject, final BindingResult bindingResult ) {

        def resource = clientBlackListOperations.add.process( bindingResult, [ serialId: restrictedObject.client_id, reason: restrictedObject.reason ] )

        conversionService.convert(
                resource as ClientRestricted, ClientRestrictedIdObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.GET )
    def show( @PathVariable( "id" ) final String clientId ) {

        def resource = clientBlackListOperations.show.process( serialId: clientId )

        conversionService.convert(
                resource as ClientRestricted, ClientRestrictedIdObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.PUT )
    def update( @PathVariable( "id" ) final String clientId, @RequestBody final ClientRestrictedObject restrictedObject ) {

        def resource = clientBlackListOperations.update.process( serialId: clientId, reason: restrictedObject.reason )

        conversionService.convert(
                resource as ClientRestricted, ClientRestrictedIdObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.DELETE )
    def remove( @PathVariable( "id" ) final String clientId ) {

        def resource = clientBlackListOperations.remove.process( serialId: clientId )

        conversionService.convert(
                resource as ClientRestricted, ClientRestrictedIdObject.class
        )
    }

}