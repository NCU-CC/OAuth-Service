package tw.edu.ncu.cc.oauth.server.validator.manager

import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import tw.edu.ncu.cc.oauth.data.v1.management.user.ManagerCreateObject

class ManagerCreateValidator implements Validator {

    @Override
    public boolean supports( Class< ? > clazz ) {
        return ManagerCreateObject.class.equals( clazz )
    }

    @Override
    public void validate( Object target, Errors errors ) {
        ValidationUtils.rejectIfEmpty( errors, "id", "id.necessary", "id is necessary" )
//        ValidationUtils.rejectIfEmpty( errors, "name", "name.necessary", "name is necessary" )
    }

}
