package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.apache.oltu.oauth2.as.request.OAuthTokenRequest
import org.apache.oltu.oauth2.common.error.OAuthError
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.exception.OAuthSystemException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import tw.edu.ncu.cc.oauth.server.helper.data.EditableRequest
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.oauth.TokenExchangeService

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static org.apache.oltu.oauth2.common.message.types.GrantType.AUTHORIZATION_CODE
import static org.apache.oltu.oauth2.common.message.types.GrantType.REFRESH_TOKEN

@Component
class OauthExchange extends BasicOperation {

    @Value( '${custom.oauth.accessToken.expire-seconds}' )
    def long accessTokenExpireSeconds

    @Resource( name = "RefreshTokenExchangeService" )
    def TokenExchangeService refreshTokenService

    @Resource( name = "AuthCodeExchangeService" )
    def TokenExchangeService authorizationCodeService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'request' )
    }

    @Override
    @Transactional( isolation = Isolation.SERIALIZABLE )
    protected handle( Map params, Map model ) {

        HttpServletRequest request = params.request as HttpServletRequest

        decideAndBuildMessage( buildOAuthTokenRequest( request ) )
    }

    private static OAuthTokenRequest buildOAuthTokenRequest( HttpServletRequest httpRequest ) throws OAuthProblemException, OAuthSystemException {
        HttpServletRequest stubHttpRequest = new EditableRequest( httpRequest ).setParameter( "redirect_uri", "stub" ); //OLTU BUG
        return new OAuthTokenRequest( stubHttpRequest );
    }

    private String decideAndBuildMessage( OAuthTokenRequest tokenRequest ) throws OAuthProblemException, OAuthSystemException {
        String grantType = tokenRequest.getGrantType()

        if( grantType ==  AUTHORIZATION_CODE as String  && authorizationCodeService != null ) {

            authorizationCodeService.buildResonseMessage( tokenRequest, accessTokenExpireSeconds )

        } else if( grantType ==  REFRESH_TOKEN as String && refreshTokenService != null ) {

            refreshTokenService.buildResonseMessage( tokenRequest, accessTokenExpireSeconds )

        } else {
            throw OAuthProblemException.error(
                    OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE
            )
        }
    }

}
