package tw.edu.ncu.cc.oauth.server.service.security

import org.springframework.beans.factory.annotation.Autowired
import specification.SpringSpecification

class SecretServiceImplTest extends SpringSpecification {

    @Autowired
    def SecretService secretService

    def "it can encrypt/decrypt text"() {
        given: "a text to be encrypted"
            def text = "abc123"
        when: "encryptOnce text"
            def encryptedText = secretService.encryptOnce( text )
        and: "decrypt the encrypted text"
            def decryptedText = secretService.decryptOnce( encryptedText )
        then: "decrypted text should be same as origin text"
            decryptedText == text
    }

}
