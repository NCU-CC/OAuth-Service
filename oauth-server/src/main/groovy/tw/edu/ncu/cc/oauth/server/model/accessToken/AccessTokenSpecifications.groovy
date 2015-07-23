package tw.edu.ncu.cc.oauth.server.model.accessToken

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.UserTokenSpecifications

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class AccessTokenSpecifications extends UserTokenSpecifications< AccessToken > {

    static Specification< AccessToken > encryptedTokenEquals( String encryptedToken ) {
        return new Specification< AccessToken >() {
            @Override
            public Predicate toPredicate( Root< AccessToken > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( AccessToken_.encryptedToken ), encryptedToken )
            }
        }
    }

}
