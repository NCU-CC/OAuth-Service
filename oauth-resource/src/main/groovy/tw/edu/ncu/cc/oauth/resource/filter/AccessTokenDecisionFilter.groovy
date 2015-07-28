package tw.edu.ncu.cc.oauth.resource.filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.HttpClientErrorException
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.resource.component.TokenMetaDecider
import tw.edu.ncu.cc.oauth.resource.core.ApiCredentialHolder
import tw.edu.ncu.cc.oauth.resource.exception.InvalidRequestException
import tw.edu.ncu.cc.oauth.resource.service.TokenConfirmService

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.springframework.http.HttpStatus.*
import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.*

public class AccessTokenDecisionFilter extends AbstractFilter {

    def TokenConfirmService tokenConfirmService
    def TokenMetaDecider tokenMetaDecider

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

        HttpServletRequest  httpRequest  = ( HttpServletRequest ) request
        HttpServletResponse httpResponse = ( HttpServletResponse ) response

        try {
            checkAuthentication( httpRequest )
            chain.doFilter( request, response )
        } catch ( HttpClientErrorException e ) {
            httpResponse.sendError( e.statusCode.value(), "access token decision failed: ${ e.message }" )
        }
    }

    private void checkAuthentication( HttpServletRequest request ) {
        if( isOAuthRequest( request ) ) {
            try {
                bindAccessToken( request, findAccessToken( request ) )
            } catch ( HttpClientErrorException e ) {
                if( e.statusCode == NOT_FOUND ) {
                    throw new HttpClientErrorException( UNAUTHORIZED, "invalid access token" )
                } else if( e.statusCode == FORBIDDEN ) {
                    throw new HttpClientErrorException( FORBIDDEN, "invalid request: ${ e.message }" )
                } else {
                    throw e
                }
            }
        } else {
            throw new HttpClientErrorException(
                    BAD_REQUEST, "access token not provided in Header like: ${ ACCESS_TOKEN_HEADER }: ${ ACCESS_TOKEN_PREFIX } [ your token ]"
            )
        }
    }

    private static boolean isOAuthRequest( HttpServletRequest request ) {
        String authorization = request.getHeader( ACCESS_TOKEN_HEADER )
        return authorization != null && authorization.startsWith( ACCESS_TOKEN_PREFIX )
    }

    private TokenObject findAccessToken( HttpServletRequest request ) {
        String accessToken = readAccessTokenFromRequest( request )
        if( ApiCredentialHolder.containsAccessToken( accessToken ) ) {
            ApiCredentialHolder.getAccessToken( accessToken )
        } else {
            tokenConfirmService.readAccessToken( accessToken, tokenMetaDecider.decide( request ) )
        }
    }

    private static String readAccessTokenFromRequest( HttpServletRequest request ) {
        String authorization = request.getHeader( ACCESS_TOKEN_HEADER )
        return authorization.trim().substring( ACCESS_TOKEN_PREFIX.length() ).trim()
    }

    private static void bindAccessToken( HttpServletRequest request, TokenObject accessToken ) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        accessToken.user, "", AuthorityUtils.createAuthorityList( accessToken.scope )
                )
        )
        request.setAttribute( ACCESS_TOKEN_ATTR, accessToken )
    }

}
