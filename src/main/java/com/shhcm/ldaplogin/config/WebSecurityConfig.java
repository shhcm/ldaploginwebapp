package com.shhcm.ldaplogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import java.util.Arrays;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyLdapAuthoritiesPopulator authoritiesPopulator;

    @Value("${embedded_ldap}")
    private boolean isEmbeddedLdap;

    @Value("${spring.ldap.urls}")
    private String ldapUrls;

    @Value("${spring.ldap.base}")
    private String ldapBase;

    @Value("${spring.ldap.username}")
    private String ldapUsername;

    @Value("${spring.ldap.password}")
    private String ldapPassword;

    protected void configure(HttpSecurity httpSecurity) throws  Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/health/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successForwardUrl("/secure");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        if(!isEmbeddedLdap) {
            // Real lap.
            auth
                    .ldapAuthentication()
                    .contextSource()
                    .url(ldapUrls)
                    .managerDn(ldapUsername)
                    .managerPassword(ldapPassword)
                    .and()
                    .userSearchBase(ldapBase)
                    .userSearchFilter("(CN={0})")
                    .ldapAuthoritiesPopulator(authoritiesPopulator);

        } else {
            // Development/testing, embedded ldap.
            auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource(contextSourceEmbeddedLdap())
                .ldapAuthoritiesPopulator(authoritiesPopulator)
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute("userPassword");
        }
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSourceEmbeddedLdap() {
        return new DefaultSpringSecurityContextSource(
                    Arrays.asList("ldap://localhost:8389/"),
                "dc=springframework,dc=org");
    }
}