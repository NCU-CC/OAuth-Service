package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.apache.oltu.oauth2.as.request.OAuthTokenRequest
import org.apache.oltu.oauth2.common.error.OAuthError
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.helper.StringHelper
import tw.edu.ncu.cc.oauth.server.helper.TimeBuilder
import tw.edu.ncu.cc.oauth.server.helper.data.TimeUnit
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.accessToken.AccessTokenService
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService
import tw.edu.ncu.cc.oauth.server.service.log.LogService
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService

import javax.servlet.http.HttpServletResponse

@Component
class OauthExchangeRefreshToken extends BasicOperation {

    @Autowired
    def LogService logService

    @Autowired
    def ClientService clientService

    @Autowired
    def RefreshTokenService refreshTokenService

    @Autowired
    def AccessTokenService accessTokenService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'request' )
        validator.required().notNull( 'expireSeconds' )
    }

    @Override
    protected handle( Map params, Map model ) {
        OAuthTokenRequest request = params.request as OAuthTokenRequest
        long expireSeconds = params.expireSeconds as long

        transaction.executeSerializable {

            validateOauthRequest( request )

            AccessToken accessToken = prepareAccessToken( request, expireSeconds )

            buildResponseMessage( accessToken.getToken(), expireSeconds )
        }

    }

    private void validateOauthRequest( OAuthTokenRequest request ) {

        String clientID     = request.getClientId()
        String clientSecret = request.getClientSecret()
        String refreshToken = request.getRefreshToken()

        logService.info(
                "EXCHANGE REFRESHTOKEN",
                "CLIENT:" + clientID,
                "REFRESHTOKEN[10]:" + StringHelper.first( refreshToken, 10 )
        )

        def client = clientService.findUndeletedBySerialId( clientID )

        if ( ! clientService.isCredentialValid( clientID, clientSecret ) || clientRestrictedService.isClientRestricted( client ) ) {
            throw OAuthProblemException.error(
                    OAuthError.TokenResponse.INVALID_CLIENT, "INVALID CLIENT"
            )
        }

        if( ! refreshTokenService.isUnexpiredTokenMatchesClientId( refreshToken , clientID ) ) {
            throw OAuthProblemException.error(
                    OAuthError.TokenResponse.INVALID_GRANT, "INVALID REFRESH TOKEN"
            )
        }
    }

    private AccessToken prepareAccessToken( OAuthTokenRequest request, long expireSeconds ) {
        accessTokenService.createByRefreshToken(
                new AccessToken(
                        dateExpired: dicideExpireDate( expireSeconds )
                ),
                refreshTokenService.findUnexpiredByToken( request.getRefreshToken(), RefreshToken_.scope )
        )
    }

    private static Date dicideExpireDate( long expireSeconds ) {
        if( expireSeconds <= 0 ) {
            return TimeBuilder.now().after( 30, TimeUnit.DAY ).buildDate()
        } else {
            return TimeBuilder.now().after( expireSeconds, TimeUnit.SECOND ).buildDate()
        }
    }

    private static String buildResponseMessage( String token, long expireSeconds ) {
        return org.apache.oltu.oauth2.as.response.OAuthASResponse
                .tokenResponse( HttpServletResponse.SC_OK )
                .setAccessToken(  token )
                .setTokenType( "Bearer" )
                .setExpiresIn( expireSeconds as String )
                .buildJSONMessage()
                .getBody()
    }

}
