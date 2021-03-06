package tw.edu.ncu.cc.oauth.server.model

import javax.persistence.*

@MappedSuperclass
class BasicEntity implements Serializable {

    @Id
    @GeneratedValue
    def Integer id

    @Version
    def Integer version = 0

    @Temporal( value = TemporalType.TIMESTAMP )
    def Date dateCreated

    @Temporal( value = TemporalType.TIMESTAMP )
    def Date lastUpdated

    @PrePersist
    protected void onCreate() {
        lastUpdated = dateCreated = new Date()
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date()
    }

}
