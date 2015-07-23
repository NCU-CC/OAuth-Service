package tw.edu.ncu.cc.oauth.server.service.security

import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Service

@Service
class SecretServiceImpl implements SecretService {

    private TextEncryptor textEncryptor
    private TextEncryptor queryableEncryptor

    @Autowired
    SecretServiceImpl( @Value( '${secrets.encrypt-once-password}' ) String oncePassword,
                       @Value( '${secrets.encrypt-once-salt}' ) String onceSalt,
                       @Value( '${secrets.encrypt-queryable-password}' ) String queryablePassword,
                       @Value( '${secrets.encrypt-queryable-salt}' ) String queryableSalt ) {
        textEncryptor = Encryptors.text( oncePassword, onceSalt )
        queryableEncryptor = Encryptors.queryableText( queryablePassword, queryableSalt )
    }

    @Override
    String generateToken() {
        new String(
                Base64.encodeBase64URLSafe(
                    UUID.randomUUID().toString().getBytes()
                )
        )
    }

    @Override
    String encryptOnce( String text ) {
        textEncryptor.encrypt( text )
    }

    @Override
    String decryptOnce( String encryptedText ) {
        try {
            textEncryptor.decrypt( encryptedText )
        } catch ( Exception ignore ) {
            return ""
        }
    }

    @Override
    String encryptQueryable( String text ) {
        queryableEncryptor.encrypt( text )
    }

    @Override
    String decryptQueryable( String encryptedText ) {
        try {
            queryableEncryptor.decrypt( encryptedText )
        } catch ( Exception ignore ) {
            return ""
        }
    }

}
