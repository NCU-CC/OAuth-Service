package tw.edu.ncu.cc.oauth.resource.service

import org.springframework.web.client.RestTemplate
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientRestrictedIdObject
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedIdObject
import tw.edu.ncu.cc.oauth.resource.config.RemoteConfig


class BlackListServiceImpl implements BlackListService {

    private RemoteConfig config
    private RestTemplate restTemplate

    public BlackListServiceImpl( RemoteConfig config, RestTemplate restTemplate ) {
        this.config = config
        this.restTemplate = restTemplate
    }

    @Override
    ClientRestrictedIdObject addClient( ClientRestrictedIdObject clientRestrictedIdObject ) {
        restTemplate.postForObject(
                config.serverPath + config.clientBlackListPath,
                clientRestrictedIdObject,
                ClientRestrictedIdObject
        )
    }

    @Override
    UserRestrictedIdObject addUser( UserRestrictedIdObject userRestrictedIdObject ) {
        restTemplate.postForObject(
                config.serverPath + config.userBlackListPath,
                userRestrictedIdObject,
                UserRestrictedIdObject
        )
    }

}
