package tw.edu.ncu.cc.oauth.server.operation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition

@Component
class OperationTransaction {

    @Autowired
    private def PlatformTransactionManager transactionManager

    def executeSerializable( Closure closure ) {
        execute( new DefaultTransactionDefinition( isolationLevel: TransactionDefinition.ISOLATION_SERIALIZABLE ), closure )
    }

    def execute( DefaultTransactionDefinition definition = new DefaultTransactionDefinition(), Closure closure ) {
        TransactionStatus status = transactionManager.getTransaction( definition )
        def result
        try {
            result = closure.call()
        } catch ( Exception e ) {
            transactionManager.rollback( status )
            throw e
        }
        transactionManager.commit( status )
        return result
    }

}
