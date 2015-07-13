package tw.edu.ncu.cc.oauth.server.model.userRestricted

import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.server.model.BasicSpecifications
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.model.user.User_

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class UserRestrictedSpecifications extends BasicSpecifications< UserRestricted > {

    static Specification< UserRestricted > userEquals( User user ) {
        return new Specification< UserRestricted >() {
            @Override
            public Predicate toPredicate( Root< UserRestricted > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( UserRestricted_.user ), user )
            }
        }
    }

    static Specification< UserRestricted > attributes( String user_name ) {
        return new Specification< UserRestricted >() {
            @Override
            public Predicate toPredicate( Root< UserRestricted > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {

                def predicates = [ ]

                if ( ! StringUtils.isEmpty( user_name ) ) {
                    predicates.add( cb.like( root.get( UserRestricted_.user ).get( User_.name ), "%${user_name}%" ) )
                }

                cb.and( predicates as Predicate[] )
            }
        }
    }

}
