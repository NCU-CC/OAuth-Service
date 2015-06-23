package tw.edu.ncu.cc.oauth.server.validator.user

import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserObject

public class UserValidator implements Validator {

    @Override
    public boolean supports( Class< ? > clazz ) {
        return UserObject.class.equals( clazz )
    }

    @Override
    public void validate( Object target, Errors errors ) {
        ValidationUtils.rejectIfEmpty( errors, "name", "name.necessary", "name is necessary" )
    }

}
