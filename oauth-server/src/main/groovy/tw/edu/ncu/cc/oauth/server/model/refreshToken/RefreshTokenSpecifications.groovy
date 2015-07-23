package tw.edu.ncu.cc.oauth.server.model.refreshToken

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.TokenSpecifications

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class RefreshTokenSpecifications extends TokenSpecifications< RefreshToken > {

    static Specification< RefreshToken > encryptedTokenEquals( String encryptedToken ) {
        return new Specification< RefreshToken >() {
            @Override
            public Predicate toPredicate( Root< RefreshToken > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( RefreshToken_.encryptedToken ), encryptedToken )
            }
        }
    }

}
