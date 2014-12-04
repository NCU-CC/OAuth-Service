package tw.edu.ncu.cc.oauth.server.service;

import tw.edu.ncu.cc.oauth.data.v1.management.application.Application;
import tw.edu.ncu.cc.oauth.server.entity.ClientEntity;

public interface ClientAPIService {

    public ClientEntity readClient( String id );
    public ClientEntity deleteClient( String id );
    public ClientEntity updateClient( String id, Application application );
    public ClientEntity createClient( Application application );
    public ClientEntity refreshClientSecret( String id );

}
