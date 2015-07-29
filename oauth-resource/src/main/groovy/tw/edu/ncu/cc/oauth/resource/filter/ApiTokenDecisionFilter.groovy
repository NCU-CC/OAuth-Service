package tw.edu.ncu.cc.oauth.resource.filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.HttpClientErrorException
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.resource.component.TokenMetaDecider
import tw.edu.ncu.cc.oauth.resource.core.ApiCredentialHolder
import tw.edu.ncu.cc.oauth.resource.helper.MessageHelper
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
import static tw.edu.ncu.cc.oauth.resource.helper.MessageHelper.errorDescription

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
        } catch ( HttpClientErrorException e ) {
            httpResponse.setContentType( "application/json" )
            httpResponse.setStatus( e.statusCode.value() )
            MessageHelper.writeErrorMessage( "api token decision failed: ${ e.message }", httpResponse.outputStream )
        }
    }

    private void checkAuthentication( HttpServletRequest request ) {
        if( isApiRequest( request ) ) {
            try {
                ApiTokenClientObject apiTokenClientObject = findApiToken( request )
                verifyApiTokenOject( apiTokenClientObject )
                bindApiToken( request, apiTokenClientObject )
            } catch ( HttpClientErrorException e  ) {
                if( e.statusCode == NOT_FOUND ) {
                    throw new HttpClientErrorException( UNAUTHORIZED, "invalid api token" )
                } else if( e.statusCode == FORBIDDEN ) {
                    throw new HttpClientErrorException( FORBIDDEN, "invalid request: ${ errorDescription( e ) }" )
                } else {
                    throw e
                }
            }
        } else {
            throw new HttpClientErrorException(
                    BAD_REQUEST, "api token not provided in Header like: ${ API_TOKEN_HEADER }: [ your token ]"
            )
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

    protected void verifyApiTokenOject( ApiTokenClientObject apiTokenClientObject ) {}

    private static void bindApiToken( HttpServletRequest request, ApiTokenClientObject apiTokenObject ) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken( apiTokenObject.client_id, "" )
        )
        request.setAttribute( API_TOKEN_ATTR, apiTokenObject )
    }

}
