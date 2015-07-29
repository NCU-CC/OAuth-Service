package tw.edu.ncu.cc.oauth.server.model.clientAccessLog

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.BasicSpecifications
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ClientAccessLogSpecifications extends BasicSpecifications< ClientAccessLog > {

    static Specification< ClientAccessLog > clientEquals( Client client ) {
        return new Specification< ClientAccessLog >() {
            @Override
            Predicate toPredicate( Root< ClientAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( ClientAccessLog_.client ), client )
            }
        }
    }

    static Specification< ClientAccessLog > applicationEquals( Client application ) {
        return new Specification<ClientAccessLog>() {
            @Override
            Predicate toPredicate( Root< ClientAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( ClientAccessLog_.application ), application )
            }
        }
    }

}
