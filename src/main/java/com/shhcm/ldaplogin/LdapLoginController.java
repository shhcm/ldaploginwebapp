package com.shhcm.ldaplogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableWebSecurity
public class LdapLoginController {
    private static final String LOGIN_TMPL = "login";
    private static final String SECURE_TMPL = "secure";

    private static final Logger LOG = LoggerFactory.getLogger(LdapLoginWebApp.class);

    @RequestMapping("/login")
    private String login() {
        LOG.info("/login aufgerufen.");
        return LOGIN_TMPL;
    }

    @RequestMapping("/secure")
    private String secure() {
        String username = getUserFromContext();
        LOG.info("/secure aufgerufen von User " + username);
        return SECURE_TMPL;
    }

    @RequestMapping("/logout")
    private String logout() {
        LOG.info("/logout aufgerufen.");
        SecurityContextHolder.clearContext();
        return LOGIN_TMPL;
    }

    private String getUserFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
