package tw.edu.ncu.cc.oauth.server.model.clientRestricted

import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.server.model.BasicSpecifications
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.client.Client_
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ClientRestrictedSpecifications extends BasicSpecifications< ClientRestricted > {

    static Specification< ClientRestricted > clientEquals( Client client ) {
        return new Specification< ClientRestricted >() {
            @Override
            public Predicate toPredicate( Root< ClientRestricted > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( ClientRestricted_.client ), client )
            }
        }
    }

    static Specification< ClientRestricted > attributes( String id, String name, User owner, Boolean deleted ) {
        return new Specification< ClientRestricted >() {
            @Override
            public Predicate toPredicate( Root< ClientRestricted > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {

                def predicates = [ ]

                if ( ! StringUtils.isEmpty( id ) ) {
                    predicates.add( cb.equal( root.get( ClientRestricted_.client ).get( Client_.id ), id as Integer ) )
                }

                if ( ! StringUtils.isEmpty( name ) ) {
                    predicates.add( cb.like( root.get( ClientRestricted_.client ).get( Client_.name ), "%${ name }%" ) )
                }

                if ( owner != null ) {
                    predicates.add( cb.equal( root.get( ClientRestricted_.client ).get( Client_.owner ), owner ) )
                }

                if( deleted != null ) {
                    predicates.add( cb.equal( root.get( ClientRestricted_.client ).get( Client_.deleted ), deleted ) )
                }

                cb.and( predicates as Predicate[] )
            }
        }
    }

}
