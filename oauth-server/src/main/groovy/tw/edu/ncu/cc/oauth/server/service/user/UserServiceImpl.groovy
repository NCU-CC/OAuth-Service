package tw.edu.ncu.cc.oauth.server.service.user

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.model.user.UserSpecifications
import tw.edu.ncu.cc.oauth.server.repository.model.UserRepository

import javax.persistence.metamodel.Attribute

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class UserServiceImpl implements UserService {

    @Autowired
    def UserRepository userRepository

    @Override
    User findByName( String name, Attribute... attributes = [] ) {
        userRepository.findOne(
                where( UserSpecifications.nameEquals( name ) )
                        .and( UserSpecifications.include( attributes ) )
        )
    }

    @Override
    User create( User user ) {
        userRepository.save( user )
    }

    @Override
    User update( User user ) {
        userRepository.save( user )
    }

    @Override
    List< User > findAllByNameLike( String userName, Attribute... attributes = [] ) {
        userRepository.findAll(
                where( UserSpecifications.nameLike( userName ) )
                        .and( UserSpecifications.include( attributes ) )
        )
    }

}
