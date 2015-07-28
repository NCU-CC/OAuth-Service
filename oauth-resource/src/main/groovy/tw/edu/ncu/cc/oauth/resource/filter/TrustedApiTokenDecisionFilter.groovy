package tw.edu.ncu.cc.oauth.resource.filter

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject


class TrustedApiTokenDecisionFilter extends ApiTokenDecisionFilter {

    @Override
    protected void verifyApiTokenOject( ApiTokenClientObject apiTokenClientObject ) {
        if( ! apiTokenClientObject.client_trusted ) {
            throw new HttpClientErrorException( HttpStatus.FORBIDDEN, 'not an  api token of trusted client' )
        }
    }

}
