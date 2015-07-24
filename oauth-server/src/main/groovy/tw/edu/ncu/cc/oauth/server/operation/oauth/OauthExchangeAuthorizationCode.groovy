package tw.edu.ncu.cc.oauth.server.operation.oauth

import groovy.time.TimeCategory
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest
import org.apache.oltu.oauth2.common.error.OAuthError
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode_
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.accessToken.AccessTokenService
import tw.edu.ncu.cc.oauth.server.service.authorizationCode.AuthorizationCodeService
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService
import tw.edu.ncu.cc.oauth.server.service.log.LogService
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService

import javax.servlet.http.HttpServletResponse

@Component
class OauthExchangeAuthorizationCode extends BasicOperation {

    @Autowired
    def LogService logService

    @Autowired
    def ClientService clientService

    @Autowired
    def AccessTokenService accessTokenService

    @Autowired
    def RefreshTokenService refreshTokenService

    @Autowired
    def AuthorizationCodeService authCodeService

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
        Integer expireSeconds = params.expireSeconds as Integer

        transaction.executeSerializable {

            validateOauthRequest( request )

            AccessToken accessToken   = prepareAccessToken( request, expireSeconds )
            RefreshToken refreshToken = prepareRefreshToken( accessToken )

            buildResponseMessage( accessToken.getToken(), refreshToken.getToken(), expireSeconds )
        }

    }

    private void validateOauthRequest( OAuthTokenRequest request ) {

        String clientID     = request.getClientId()
        String clientSecret = request.getClientSecret()
        String authCode     = request.getCode()

        logService.info(
                "EXCHANGE AUTHCODE",
                "CLIENT:" + clientID
        )

        def client = clientService.findUndeletedBySerialId( clientID )

        if ( ! clientService.isCredentialValid( clientID, clientSecret ) || clientRestrictedService.isClientRestricted( client ) ) {
            throw OAuthProblemException.error(
                    OAuthError.TokenResponse.INVALID_CLIENT, "INVALID CLIENT"
            )
        }

        if( ! authCodeService.isUnexpiredCodeMatchesClientId( authCode, clientID ) ) {
            throw OAuthProblemException.error(
                    OAuthError.TokenResponse.INVALID_GRANT, "INVALID AUTH CODE"
            )
        }
    }

    private AccessToken prepareAccessToken( OAuthTokenRequest request, Integer expireSeconds ) {
        accessTokenService.createByAuthorizationCode(
                new AccessToken(
                        dateExpired: dicideAccessTokenExpireDate( expireSeconds )
                ),
                authCodeService.findUnexpiredByCode( request.getCode(), AuthorizationCode_.scope )
        )
    }

    private static Date dicideAccessTokenExpireDate( Integer expireSeconds ) {
        use( TimeCategory ) {
            new Date() + expireSeconds.seconds
        }
    }

    private RefreshToken prepareRefreshToken( AccessToken accessToken ) {
        refreshTokenService.createByAccessToken(
                new RefreshToken(
                        dateExpired: dicideRefreshTokenExpireDate()
                ),
                accessToken
        )
    }

    private static Date dicideRefreshTokenExpireDate() {
        return null
    }

    private static String buildResponseMessage( String accessToken, String refreshToken, long expireSeconds ) {
        return org.apache.oltu.oauth2.as.response.OAuthASResponse
                .tokenResponse( HttpServletResponse.SC_OK )
                .setAccessToken( accessToken )
                .setRefreshToken( refreshToken )
                .setTokenType( "Bearer" )
                .setExpiresIn( expireSeconds as String )
                .buildJSONMessage()
                .getBody()
    }


}
