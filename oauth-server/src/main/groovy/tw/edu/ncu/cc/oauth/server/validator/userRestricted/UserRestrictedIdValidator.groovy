package tw.edu.ncu.cc.oauth.server.validator.userRestricted

import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedIdObject

public class UserRestrictedIdValidator implements Validator {

    @Override
    public boolean supports( Class< ? > clazz ) {
        return UserRestrictedIdObject.class.equals( clazz )
    }

    @Override
    public void validate( Object target, Errors errors ) {
        ValidationUtils.rejectIfEmpty( errors, "user_name", "user_name.necessary", "user_name is necessary" )
    }

}
