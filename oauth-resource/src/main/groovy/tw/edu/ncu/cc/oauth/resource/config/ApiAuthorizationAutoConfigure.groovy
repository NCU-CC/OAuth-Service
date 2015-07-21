package tw.edu.ncu.cc.oauth.resource.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import tw.edu.ncu.cc.oauth.resource.component.TokenMetaDecider
import tw.edu.ncu.cc.oauth.resource.component.TokenMetaDeciderImpl
import tw.edu.ncu.cc.oauth.resource.filter.AccessTokenDecisionFilter
import tw.edu.ncu.cc.oauth.resource.filter.ApiTokenDecisionFilter
import tw.edu.ncu.cc.oauth.resource.service.BlackListService
import tw.edu.ncu.cc.oauth.resource.service.BlackListServiceImpl
import tw.edu.ncu.cc.oauth.resource.service.TokenConfirmService
import tw.edu.ncu.cc.oauth.resource.service.TokenConfirmServiceImpl

import java.nio.charset.Charset

@Configuration
@EnableConfigurationProperties( [ RemoteConfig, CloudConfig ] )
class ApiAuthorizationAutoConfigure {

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    @Autowired
    RemoteConfig remoteConfig

    @Autowired
    CloudConfig cloudConfig

    @Autowired
    RestTemplate restTemplate

    @Bean
    @ConditionalOnMissingBean( RestTemplate )
    RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate()
        template.getMessageConverters().add( 0, new StringHttpMessageConverter( Charset.forName( "UTF-8" ) ) )
        template
    }

    @Bean
    @ConditionalOnMissingBean( TokenConfirmService )
    TokenConfirmService tokenConfirmService() {
        new TokenConfirmServiceImpl( remoteConfig, restTemplate )
    }

    @Bean
    @ConditionalOnMissingBean( TokenMetaDecider )
    TokenMetaDecider tokenMetaDecider() {
        new TokenMetaDeciderImpl( application: cloudConfig.name )
    }

    @Bean
    @ConditionalOnMissingBean( BlackListService )
    BlackListService blackListService() {
        new BlackListServiceImpl( remoteConfig, restTemplate )
    }

    @Bean
    @ConditionalOnMissingBean( ApiTokenDecisionFilter )
    ApiTokenDecisionFilter apiTokenDecisionFilter( TokenConfirmService tokenConfirmService, TokenMetaDecider tokenMetaDecider ) {
        logger.info( "auto-configure oauth api token decision filter with server path : " + remoteConfig.serverPath )
        ApiTokenDecisionFilter filter = new ApiTokenDecisionFilter()
        filter.setTokenConfirmService( tokenConfirmService )
        filter.setTokenMetaDecider( tokenMetaDecider )
        filter
    }

    @Bean
    FilterRegistrationBean apiTokenDecisionFilterRegistration( ApiTokenDecisionFilter apiTokenDecisionFilter ) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean( apiTokenDecisionFilter )
        registrationBean.setEnabled( false )
        registrationBean
    }

    @Bean
    @ConditionalOnMissingBean( AccessTokenDecisionFilter )
    AccessTokenDecisionFilter accessTokenDecisionFilter( TokenConfirmService tokenConfirmService, TokenMetaDecider tokenMetaDecider ) {
        logger.info( "auto-configure oauth access token decision filter with server path : " + remoteConfig.serverPath )
        AccessTokenDecisionFilter filter = new AccessTokenDecisionFilter()
        filter.setTokenConfirmService( tokenConfirmService )
        filter.setTokenMetaDecider( tokenMetaDecider )
        filter
    }

    @Bean
    FilterRegistrationBean accessTokenDecisionFilterRegistration( AccessTokenDecisionFilter accessTokenDecisionFilter ) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean( accessTokenDecisionFilter )
        registrationBean.setEnabled( false )
        registrationBean
    }

}
