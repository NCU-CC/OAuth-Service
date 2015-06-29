package tw.edu.ncu.cc.oauth.server.validator.clientRestricted

import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientRestrictedIdObject

public class ClientRestrictedIdValidator implements Validator {

    @Override
    public boolean supports( Class< ? > clazz ) {
        return ClientRestrictedIdObject.class.equals( clazz )
    }

    @Override
    public void validate( Object target, Errors errors ) {
        ValidationUtils.rejectIfEmpty( errors, "client_id", "client_id.necessary", "client_id is necessary" )
    }

}
