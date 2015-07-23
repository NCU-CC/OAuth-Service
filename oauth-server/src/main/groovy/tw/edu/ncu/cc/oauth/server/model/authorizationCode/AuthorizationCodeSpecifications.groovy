package tw.edu.ncu.cc.oauth.server.model.authorizationCode

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.TokenSpecifications

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class AuthorizationCodeSpecifications extends TokenSpecifications< AuthorizationCode > {

    static Specification< AuthorizationCode > encryptedCodeEquals( String encryptedCode ) {
        return new Specification< AuthorizationCode >() {
            @Override
            public Predicate toPredicate( Root< AuthorizationCode > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( AuthorizationCode_.encryptedCode ), encryptedCode )
            }
        }
    }

}
