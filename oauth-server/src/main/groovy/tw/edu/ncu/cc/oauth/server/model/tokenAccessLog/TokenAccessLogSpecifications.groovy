package tw.edu.ncu.cc.oauth.server.model.tokenAccessLog

import org.springframework.data.jpa.domain.Specification
import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.BasicSpecifications
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class TokenAccessLogSpecifications extends BasicSpecifications<TokenAccessLog> {

    static Specification< TokenAccessLog > clientEquals( Client client ) {
        return new Specification<TokenAccessLog>() {
            @Override
            Predicate toPredicate( Root< TokenAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( TokenAccessLog_.client ), client )
            }
        }
    }

    static Specification< TokenAccessLog > ipMatches( String ip ) {
        return new Specification<TokenAccessLog>() {
            @Override
            Predicate toPredicate( Root< TokenAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( TokenAccessLog_.ip ), ip )
            }
        }
    }

    static Specification< TokenAccessLog > applicationEquals( String application ) {
        return new Specification<TokenAccessLog>() {
            @Override
            Predicate toPredicate( Root< TokenAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( TokenAccessLog_.application ), application )
            }
        }
    }

    static Specification< TokenAccessLog > tokenTypeEquals( String tokenType ) {
        return new Specification<TokenAccessLog>() {
            @Override
            Predicate toPredicate( Root< TokenAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( TokenAccessLog_.tokenType ), tokenType )
            }
        }
    }

    static Specification< TokenAccessLog > tokenIdEquals( Integer tokenId ) {
        return new Specification<TokenAccessLog>() {
            @Override
            Predicate toPredicate( Root< TokenAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                return cb.equal( root.get( TokenAccessLog_.tokenId ), tokenId )
            }
        }
    }

    static Specification< TokenAccessLog > dateBetween( Date startDate, Date endDate ) {
        return new Specification<TokenAccessLog>() {
            @Override
            Predicate toPredicate( Root< TokenAccessLog > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {
                def predicates = [
                        cb.greaterThan( root.get( BasicEntity_.dateCreated ), startDate ),
                        cb.lessThan( root.get( BasicEntity_.dateCreated ), endDate )
                ]
                cb.and( predicates as Predicate[] )
            }
        }
    }

}
