package tw.edu.ncu.cc.oauth.server.view.tokenAccessLog

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenLogObject
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog


@Component
class TokenAccessLog_TokenLogObject implements Converter< TokenAccessLog, TokenLogObject >{

    @Override
    TokenLogObject convert( TokenAccessLog source ) {
        TokenLogObject tokenLogObject = new TokenLogObject()
        tokenLogObject.ip = source.ip
        tokenLogObject.date = source.dateCreated
        tokenLogObject.application = source.application
        tokenLogObject.referer = source.referer
        tokenLogObject.token_id = source.tokenId
        tokenLogObject.token_type = source.tokenType
        tokenLogObject
    }

}
