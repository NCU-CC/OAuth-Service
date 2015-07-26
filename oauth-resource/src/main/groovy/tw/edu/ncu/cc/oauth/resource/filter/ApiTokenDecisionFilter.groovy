package tw.edu.ncu.cc.oauth.resource.filter

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.HttpClientErrorException
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
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
import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.API_TOKEN_ATTR
import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.API_TOKEN_HEADER

public class ApiTokenDecisionFilter extends AbstractFilter {

    def TokenConfirmService tokenConfirmService
    def TokenMetaDecider tokenMetaDecider

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

        HttpServletRequest  httpRequest  = ( HttpServletRequest ) request
        HttpServletResponse httpResponse = ( HttpServletResponse ) response

        try {
            checkAuthentication( httpRequest )
            chain.doFilter( request, response )
        } catch ( InvalidRequestException e ) {
            httpResponse.sendError( e.httpStatus.value(), e.message )
        }
    }

    private void checkAuthentication( HttpServletRequest request ) {
        if( isApiRequest( request ) ) {
            try {
                bindApiToken( request, findApiToken( request ) )
            } catch ( HttpClientErrorException e  ) {
                if( e.statusCode == NOT_FOUND ) {
                    throw new InvalidRequestException( UNAUTHORIZED, "invalid api token" )
                } else if( e.statusCode == FORBIDDEN ) {
                    throw new InvalidRequestException( FORBIDDEN, "invalid request address" )
                } else {
                    throw e
                }
            }
        } else {
            throw new InvalidRequestException( HttpStatus.BAD_REQUEST, "api token not provided" )
        }
    }

    private static boolean isApiRequest( HttpServletRequest request ) {
        return request.getHeader( API_TOKEN_HEADER ) != null
    }

    private ApiTokenClientObject findApiToken( HttpServletRequest request ) {
        String apiToken = readApiTokenFromRequest( request )
        if( ApiCredentialHolder.containsApiToken( apiToken ) ) {
            ApiCredentialHolder.getApiToken( apiToken )
        } else {
            tokenConfirmService.readApiToken( apiToken, tokenMetaDecider.decide( request ) )
        }
    }

    private static String readApiTokenFromRequest( HttpServletRequest request ) {
        request.getHeader( API_TOKEN_HEADER )
    }

    private static void bindApiToken( HttpServletRequest request, ApiTokenClientObject apiTokenObject ) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken( apiTokenObject.client_id, "" )
        )
        request.setAttribute( API_TOKEN_ATTR, apiTokenObject )
    }

}
