package tw.edu.ncu.cc.oauth.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tw.edu.ncu.cc.oauth.server.interceptor.TrustedApiTokenDecisionFilter

@EnableWebSecurity
public class SecurityConfig {

    @Order( 1 )
    @Configuration
    public static class OauthTrustedClientGuard extends WebSecurityConfigurerAdapter {

        @Autowired
        def TrustedApiTokenDecisionFilter trustedApiTokenDecisionFilter

        @Bean
        FilterRegistrationBean trustedApiTokenDecisionFilterRegistration( TrustedApiTokenDecisionFilter trustedApiTokenDecisionFilter ) {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean( trustedApiTokenDecisionFilter )
            registrationBean.setEnabled( false )
            registrationBean
        }

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.requestMatchers()
                    .antMatchers( "/management/v*/access_tokens/token/**" )
                    .antMatchers( "/management/v*/api_tokens/token/**" )
                    .and()
                    .addFilterAfter( trustedApiTokenDecisionFilter, UsernamePasswordAuthenticationFilter )
                    .csrf().disable()
        }

    }

    @Order( 2 )
    @Configuration
    public static class OauthConfig1 extends WebSecurityConfigurerAdapter {

        @Value( '${custom.management.security.access}' )
        def String managementAccess

        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.antMatcher( "/management/**" )
                .authorizeRequests()
                    .antMatchers( "/management/**" ).access( managementAccess )
                    .and()
                .csrf().disable()
        }
    }

    @Order( 3 )
    @Configuration
    public static class OauthConfig2 extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.antMatcher( "/oauth/token" )
                .authorizeRequests()
                    .antMatchers( "/oauth/token" ).permitAll()
                    .and()
                .csrf().disable()
        }
    }

    @Order( 4 )
    @Configuration
    public static class OauthConfig3 extends WebSecurityConfigurerAdapter {
        @Override
        void configure( WebSecurity web ) throws Exception {
            web.ignoring().antMatchers( "/resource/**" )
        }
        @Override
        protected void configure( HttpSecurity http ) throws Exception {
            http.authorizeRequests()
                    .antMatchers( "/oauth/authorize" ).hasAnyRole( "USER" )
                    .antMatchers( "/login_page" ).permitAll()
                    .antMatchers( "/login_confirm" ).permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage( "/login_page" ).permitAll()
        }
    }

}
