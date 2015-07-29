package tw.edu.ncu.cc.oauth.resource.filter

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.resource.component.TokenMetaDecider
import tw.edu.ncu.cc.oauth.resource.service.TokenConfirmService

import javax.servlet.FilterChain
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.*

class AccessTokenDecisionFilterTest extends Specification {

    def FilterChain filterChain
    def HttpServletRequest  request
    def HttpServletResponse response
    def TokenMetaDecider tokenMetaDecider
    def TokenConfirmService tokenConfirmService
    def AccessTokenDecisionFilter accessTokenDecisionFilter

    def setup() {
        filterChain = Mock( FilterChain )
        request  = Mock( HttpServletRequest )
        response = Mock( HttpServletResponse )
        tokenConfirmService = Mock( TokenConfirmService )
        tokenMetaDecider = Mock( TokenMetaDecider )
        response.getOutputStream() >> Mock( ServletOutputStream )

        accessTokenDecisionFilter = new AccessTokenDecisionFilter()
        accessTokenDecisionFilter.tokenConfirmService = tokenConfirmService
        accessTokenDecisionFilter.tokenMetaDecider = tokenMetaDecider
    }

    def "it should response with 400 directly if no access token appended"() {
        given:
            request.getHeader( ACCESS_TOKEN_HEADER ) >> null
        when:
            accessTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 400 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should response 401 if access token provided but it is not found"() {
        given:
            request.getHeader( ACCESS_TOKEN_HEADER ) >> ACCESS_TOKEN_PREFIX + " " + "ACCESS_TOKEN2"
        and:
            tokenConfirmService.readAccessToken( "ACCESS_TOKEN2", null ) >> {
                throw new HttpClientErrorException( HttpStatus.NOT_FOUND )
            }
        when:
            accessTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 401 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should response 403 if access token provided but forbidden"() {
        given:
            request.getHeader( ACCESS_TOKEN_HEADER ) >> ACCESS_TOKEN_PREFIX + " " + "ACCESS_TOKEN3"
        and:
            tokenConfirmService.readAccessToken( "ACCESS_TOKEN3", null ) >> {
                throw new HttpClientErrorException( HttpStatus.FORBIDDEN )
            }
        when:
            accessTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 403 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should hold access token as authentication in spring security context"() {
        given:
            request.getHeader( ACCESS_TOKEN_HEADER ) >> ACCESS_TOKEN_PREFIX + " " + "ACCESS_TOKEN"
        and:
            tokenConfirmService.readAccessToken( "ACCESS_TOKEN", null ) >> new TokenObject(
                    user: "USER",
                    scope: [ "PERMISSION" ]
            )
        when:
            accessTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().size() == 1
        and:
            1 * request.setAttribute( ACCESS_TOKEN_ATTR, _ )
        and:
            1 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

}
