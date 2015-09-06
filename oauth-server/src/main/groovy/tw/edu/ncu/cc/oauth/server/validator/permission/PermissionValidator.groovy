package tw.edu.ncu.cc.oauth.server.validator.permission

import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import tw.edu.ncu.cc.oauth.data.v1.management.permission.PermissionObject

public class PermissionValidator implements Validator {

    @Override
    public boolean supports( Class< ? > clazz ) {
        return PermissionObject.class.equals( clazz )
    }

    @Override
    public void validate( Object target, Errors errors ) {
        ValidationUtils.rejectIfEmpty( errors, "name", "name.necessary", "name is necessary" )
    }

}
