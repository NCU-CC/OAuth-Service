package tw.edu.ncu.cc.oauth.resource.component

import tw.edu.ncu.cc.oauth.data.v1.management.resource.TokenRequestMetaObject

import javax.servlet.http.HttpServletRequest

import static org.springframework.http.HttpHeaders.REFERER

class TokenMetaDeciderImpl implements TokenMetaDecider {

    def String application

    @Override
    TokenRequestMetaObject decide( HttpServletRequest request ) {
        new TokenRequestMetaObject(
                ip: request.remoteAddr,
                referer: request.getHeader( REFERER ),
                application: application
        )
    }

}
