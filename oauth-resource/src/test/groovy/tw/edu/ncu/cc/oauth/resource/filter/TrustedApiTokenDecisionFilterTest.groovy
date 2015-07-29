package tw.edu.ncu.cc.oauth.resource.filter

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.resource.component.TokenMetaDecider
import tw.edu.ncu.cc.oauth.resource.service.TokenConfirmService

import javax.servlet.FilterChain
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.getAPI_TOKEN_ATTR
import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.getAPI_TOKEN_HEADER

class TrustedApiTokenDecisionFilterTest extends Specification {

    def FilterChain filterChain
    def HttpServletRequest  request
    def HttpServletResponse response
    def TokenMetaDecider tokenMetaDecider
    def TokenConfirmService tokenConfirmService
    def TrustedApiTokenDecisionFilter trustedApiTokenDecisionFilter

    def setup() {
        filterChain = Mock( FilterChain )
        request  = Mock( HttpServletRequest )
        response = Mock( HttpServletResponse )
        tokenConfirmService = Mock( TokenConfirmService )
        tokenMetaDecider = Mock( TokenMetaDecider )
        response.getOutputStream() >> Mock( ServletOutputStream )

        trustedApiTokenDecisionFilter = new TrustedApiTokenDecisionFilter()
        trustedApiTokenDecisionFilter.tokenConfirmService = tokenConfirmService
        trustedApiTokenDecisionFilter.tokenMetaDecider = tokenMetaDecider
    }

    def "it should response with 400 directly if no api token appended"() {
        given:
            request.getHeader( API_TOKEN_HEADER ) >> null
        when:
            trustedApiTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 400 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should response 401 if api token provided but it is not found"() {
        given:
            request.getHeader( API_TOKEN_HEADER ) >> "TOKEN1"
        and:
            tokenConfirmService.readApiToken( "TOKEN1", null ) >> {
                throw new HttpClientErrorException( HttpStatus.NOT_FOUND )
            }
        when:
            trustedApiTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 401 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should response 403 if api token provided but forbidden"() {
        given:
            request.getHeader( API_TOKEN_HEADER ) >> "TOKEN2"
        and:
            tokenConfirmService.readApiToken( "TOKEN2", null ) >> {
                throw new HttpClientErrorException( HttpStatus.FORBIDDEN )
            }
        when:
            trustedApiTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 403 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should response 403 if api token provided but its client is not trusted"() {
        given:
            request.getHeader( API_TOKEN_HEADER ) >> "TOKEN2"
        and:
            tokenConfirmService.readApiToken( "TOKEN2", null ) >> new ApiTokenClientObject(
                    client_trusted: false
            )
        when:
            trustedApiTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            1 * response.setStatus( 403 )
        and:
            0 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

    def "it should hold api token if valid"() {
        given:
            request.getHeader( API_TOKEN_HEADER ) >> "TOKEN"
        and:
            tokenConfirmService.readApiToken( "TOKEN", null ) >> new ApiTokenClientObject(
                    client_id: "testapp",
                    client_trusted: true
            )
        when:
            trustedApiTokenDecisionFilter.doFilter( request, response, filterChain )
        then:
            SecurityContextHolder.getContext().getAuthentication().name == "testapp"
        and:
            1 * request.setAttribute( API_TOKEN_ATTR, _ )
        and:
            1 * filterChain.doFilter( _ as HttpServletRequest, _ as HttpServletResponse )
    }

}
