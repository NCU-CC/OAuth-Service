package tw.edu.ncu.cc.oauth.server.model

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

abstract class TokenSpecifications< T extends TokenEntity > extends BasicSpecifications< T > {

    static Specification< T > unexpired() {
        new Specification< T >() {
            @Override
            Predicate toPredicate( Root< T > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.greaterThan( root.get( TokenEntity_.dateExpired ), new Date() )
            }
        }
    }

    static Specification< T > clientEquals( Client client ) {
        new Specification< T >() {
            @Override
            Predicate toPredicate( Root< T > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( TokenEntity_.client ), client )
            }
        }
    }

}
