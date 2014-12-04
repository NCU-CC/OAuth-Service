package tw.edu.ncu.cc.oauth.server.service;

import tw.edu.ncu.cc.oauth.server.entity.AuthCodeEntity;

public interface AuthCodeService {

    public AuthCodeEntity getAuthCode( int id );
    public AuthCodeEntity getAuthCode( String code );
    public AuthCodeEntity generateAuthCode( AuthCodeEntity authCode );
    public AuthCodeEntity deleteAuthCode  ( AuthCodeEntity authCode );

}
