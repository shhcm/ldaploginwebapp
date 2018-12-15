package com.shhcm.ldaplogin.config;

import com.shhcm.ldaplogin.db.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Component
public class MyLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {
    /*
    * Used to add different authorities to different types of users, as given by db.
    * (Authorities may not be given by LDAP lookup)
    */
    private static final Logger LOG = LoggerFactory.getLogger(MyLdapAuthoritiesPopulator.class);

    @Autowired
    DAO dao;

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations dirContextOperations, String username) {
        // Lookup the authorities for a user in the db.
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        List<String> authortiesList = dao.getAuthoritiesForUser(username);

        authortiesList.forEach(authority -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
            LOG.info("Granted authority: " + authority);
        });

        return grantedAuthorities;
    }
}
