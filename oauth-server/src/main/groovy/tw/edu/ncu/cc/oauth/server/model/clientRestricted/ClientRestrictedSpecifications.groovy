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

    static Specification< ClientRestricted > attributes( String client_id, String client_name, User client_owner, Boolean client_deleted ) {
        return new Specification< ClientRestricted >() {
            @Override
            public Predicate toPredicate( Root< ClientRestricted > root, CriteriaQuery<?> query, CriteriaBuilder cb ) {

                def predicates = [ ]

                if ( ! StringUtils.isEmpty( client_id ) ) {
                    predicates.add( cb.equal( root.get( ClientRestricted_.client ).get( Client_.id ), client_id as Integer ) )
                }

                if ( ! StringUtils.isEmpty( client_name ) ) {
                    predicates.add( cb.like( root.get( ClientRestricted_.client ).get( Client_.name ), "%${ client_name }%" ) )
                }

                if ( client_owner != null ) {
                    predicates.add( cb.equal( root.get( ClientRestricted_.client ).get( Client_.owner ), client_owner ) )
                }

                if( client_deleted != null ) {
                    predicates.add( cb.equal( root.get( ClientRestricted_.client ).get( Client_.deleted ), client_deleted ) )
                }

                cb.and( predicates as Predicate[] )
            }
        }
    }

}
