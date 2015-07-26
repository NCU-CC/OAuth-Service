package tw.edu.ncu.cc.oauth.server.model.client

import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.server.model.BasicSpecifications
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ClientSpecifications extends BasicSpecifications< Client > {

    static Specification< Client > undeleted() {
        return new Specification< Client >() {
            @Override
            public Predicate toPredicate( Root< Client > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( Client_.deleted ), false )
            }
        }
    }

    static Specification< Client > trusted() {
        return new Specification< Client >() {
            @Override
            public Predicate toPredicate( Root< Client > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( Client_.trusted ), true )
            }
        }
    }

    static Specification< Client > serialIdEquals( String serialId ) {
        return new Specification< Client >() {
            @Override
            public Predicate toPredicate( Root< Client > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
                return cb.equal( root.get( Client_.serialId ), serialId )
            }
        }
    }

    static Specification< Client > attributes( String id, String name, User owner, Boolean deleted, Boolean trusted = false ) {
        return new Specification< Client >() {
            @Override
            public Predicate toPredicate( Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb ) {

                def predicates = [ ]

                if ( !StringUtils.isEmpty( id ) ) {
                    predicates.add( cb.equal( root.get( Client_.serialId ), id ) )
                }

                if ( !StringUtils.isEmpty( name ) ) {
                    predicates.add( cb.like( root.get( Client_.name ), "%${ name }%" ) )
                }

                if ( owner != null ) {
                    predicates.add( cb.equal( root.get( Client_.owner ), owner ) )
                }

                if( deleted != null ) {
                    predicates.add( cb.equal( root.get( Client_.deleted ), deleted ) )
                }

                if( deleted != null ) {
                    predicates.add( cb.equal( root.get( Client_.trusted ), trusted ) )
                }

                cb.and( predicates as Predicate[] )
            }
        }
    }

}
