package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import tw.edu.ncu.cc.oauth.server.model.client.Client

public interface ClientRepository extends JpaRepository< Client, Integer >, JpaSpecificationExecutor< Client > {

    @Modifying
    @Query( "update AccessToken t set t.dateExpired = CURRENT_TIMESTAMP() where t.client = ?1" )
    void revokeAccessTokens( Client client )

    @Modifying
    @Query( "update AuthorizationCode t set t.dateExpired = CURRENT_TIMESTAMP() where t.client = ?1" )
    void revokeAuthorizationCodes( Client client )

    @Modifying
    @Query( "update ApiToken t set t.dateExpired = CURRENT_TIMESTAMP() where t.client = ?1" )
    void revokeApiTokens( Client client )

    @Modifying
    @Query( "update RefreshToken t set t.dateExpired = CURRENT_TIMESTAMP() where t.client = ?1" )
    void revokeRefreshTokens( Client client )

}
