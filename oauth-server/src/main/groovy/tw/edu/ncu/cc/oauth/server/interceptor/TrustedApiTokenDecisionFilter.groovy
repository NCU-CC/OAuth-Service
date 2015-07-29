package tw.edu.ncu.cc.oauth.server.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException
import tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute
import tw.edu.ncu.cc.oauth.data.v1.message.ErrorObject
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TrustedApiTokenDecisionFilter implements Filter {

    @Autowired
    def ApiTokenService apiTokenService

    @Override
    void init( FilterConfig filterConfig ) throws ServletException {

    }

    @Override
    void destroy() {

    }

    @Override
    void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {
        HttpServletRequest  httpRequest  = ( HttpServletRequest ) request
        HttpServletResponse httpResponse = ( HttpServletResponse ) response

        try {
            checkAuthentication( httpRequest )
            chain.doFilter( request, response )
        } catch ( HttpServerErrorException e ) {
            httpResponse.setContentType( "application/json" )
            httpResponse.setStatus( e.statusCode.value() )
            new ObjectMapper().writeValue( httpResponse.outputStream, new ErrorObject( "trusted api token decision failed: ${ e.message }" ) )
        }
    }

    private void checkAuthentication( HttpServletRequest request ) {
        if( isApiRequest( request ) ) {
            ApiToken apiToken = findTrustedApiToken( request )
            if( apiToken == null ) {
                throw new HttpServerErrorException( HttpStatus.UNAUTHORIZED, "invalid trusted api token" )
            } else if( ! apiToken.client.trusted ) {
                throw new HttpServerErrorException( HttpStatus.FORBIDDEN, "not a trusted api token" )
            } else {
                bindApiToken( request, apiToken )
            }
        } else {
            throw new HttpServerErrorException( HttpStatus.BAD_REQUEST, "trusted api token not provided" )
        }
    }

    private static boolean isApiRequest( HttpServletRequest request ) {
        return request.getHeader( RequestAttribute.API_TOKEN_HEADER ) != null
    }

    private ApiToken findTrustedApiToken( HttpServletRequest request ) {
        String token = readApiTokenFromRequest( request )
        ApiToken apiToken = apiTokenService.findUnexpiredByToken( token )
        apiToken != null ? apiToken : null
    }

    private static String readApiTokenFromRequest( HttpServletRequest request ) {
        request.getHeader( RequestAttribute.API_TOKEN_HEADER )
    }

    private static void bindApiToken( HttpServletRequest request, ApiToken apiToken ) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken( apiToken.client.serialId, "" )
        )
        request.setAttribute( RequestAttribute.API_TOKEN_ATTR, apiToken )
    }

}
