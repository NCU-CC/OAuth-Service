package tw.edu.ncu.cc.oauth.server.model.apiToken

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.TokenSpecifications

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ApiTokenSpecifications extends TokenSpecifications< ApiToken > {

    static Specification< ApiToken > encryptedTokenEquals( String encryptedToken ) {
        return new Specification< ApiToken >() {
            @Override
            public Predicate toPredicate( Root< ApiToken > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( ApiToken_.encryptedToken ), encryptedToken )
            }
        }
    }

}
