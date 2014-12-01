package tw.edu.ncu.cc.oauth.server.component.impl;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import tw.edu.ncu.cc.oauth.server.component.SecretCodec;
import tw.edu.ncu.cc.oauth.server.data.SerialSecret;

@Component
public class SecretCodecImpl implements SecretCodec {

    @Override
    public String encode( SerialSecret secret ) {
        return new String( Base64.encode(
                ( secret.getId() + ":::" + secret.getSecret() ).getBytes()
        ) );
    }

    @Override
    public SerialSecret decode( String data ) {
        if( Base64.isBase64( data.getBytes() ) ) {
            String originCode = new String( Base64.decode( data.getBytes() ) );
            if( originCode.contains( ":::" ) ) {
                String[] pair = originCode.split( ":::" );
                if( isInteger( pair[0] ) ) {
                    return new SerialSecret( Integer.parseInt( pair[0] ), pair[1] );
                }
            }
        }
        return new SerialSecret( 0, "" );
    }

    private static boolean isInteger( String str ) {
        if ( str == null ) {
            return false;
        }
        int length = str.length();
        if ( length == 0 ) {
            return false;
        }
        int i = 0;
        if ( str.charAt( 0 ) == '-' ) {
            if ( length == 1 ) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++ ) {
            char c = str.charAt( i );
            if ( c < '0' || c > '9' ) {
                return false;
            }
        }
        return true;
    }

}
