package tw.edu.ncu.cc.oauth.server.validator.manager

import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import tw.edu.ncu.cc.oauth.data.v1.management.user.ManagerObject

class ManagerValidator implements Validator {

    @Override
    public boolean supports( Class< ? > clazz ) {
        return ManagerObject.class.equals( clazz )
    }

    @Override
    public void validate( Object target, Errors errors ) {
        ValidationUtils.rejectIfEmpty( errors, "id", "id.necessary", "id is necessary" )
    }

}
