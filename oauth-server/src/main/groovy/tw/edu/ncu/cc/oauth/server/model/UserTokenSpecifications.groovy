package tw.edu.ncu.cc.oauth.server.model

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

abstract class UserTokenSpecifications< T extends UserTokenEntity > extends TokenSpecifications< T > {

    static Specification< T > userEquals( User user ) {
        return new Specification< T >() {
            @Override
            public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( UserTokenEntity_.user ), user )
            }
        }
    }

}
