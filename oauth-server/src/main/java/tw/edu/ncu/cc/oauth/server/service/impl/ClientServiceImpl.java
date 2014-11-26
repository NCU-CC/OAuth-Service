package tw.edu.ncu.cc.oauth.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tw.edu.ncu.cc.oauth.server.entity.ClientEntity;
import tw.edu.ncu.cc.oauth.server.helper.TokenGenerator;
import tw.edu.ncu.cc.oauth.server.repository.ClientRepository;
import tw.edu.ncu.cc.oauth.server.repository.ClientTokenRepository;
import tw.edu.ncu.cc.oauth.server.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientTokenRepository clientTokenRepository;
    private TokenGenerator tokenGenerator = new TokenGenerator();

    @Autowired
    public void setClientRepository( ClientRepository clientRepository ) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setClientTokenRepository( ClientTokenRepository clientTokenRepository ) {
        this.clientTokenRepository = clientTokenRepository;
    }

    @Override
    @Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
    public ClientEntity getClient( int clientID ) {
        return clientRepository.getClient( clientID );
    }

    @Override
    @Transactional
    public ClientEntity updateClient( ClientEntity client ) {
        return clientRepository.updateClient( client );
    }

    @Override
    @Transactional
    public void deleteClient( ClientEntity client ) {
        clientRepository.deleteClient( client );
    }

    @Override
    @Transactional
    public void revokeClientTokens( ClientEntity client ) {
        clientTokenRepository.deleteAllAccessTokenOfClient( client );
    }

    @Override
    @Transactional
    public void refreshClientSecret( ClientEntity client, String secret ) {
        client.setSecret( secret );
        updateClient( client );
        client.setSecret( secret );
    }

    @Override
    @Transactional
    public ClientEntity generateClient( ClientEntity client ) {
        String secret = tokenGenerator.generate();
        client.setSecret( secret );
        ClientEntity clientEntity = clientRepository.generateClient( client );
        client.setId( clientEntity.getId() );
        client.setSecret( secret );
        return client;
    }

}
