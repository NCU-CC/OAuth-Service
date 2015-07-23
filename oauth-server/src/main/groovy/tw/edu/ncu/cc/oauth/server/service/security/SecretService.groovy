package tw.edu.ncu.cc.oauth.server.service.security

interface SecretService {

    String generateToken()

    String encryptOnce( String text )
    String decryptOnce( String encryptedText )

    String encryptQueryable( String text )
    String decryptQueryable( String encryptedText )

}