package com.shhcm.ldaplogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
    private String secure(HttpSession httpSession, Model model) {

        List<String> authorities = new LinkedList<>();
        getAuthenticationFromContext().getAuthorities().forEach(authority-> {
            authorities.add(authority.toString());
        });

        String username = getAuthenticationFromContext().getName();

        // Pass username as model attribute, authorities as session attribute.
        httpSession.setAttribute("authorities", authorities);
        model.addAttribute("username", username);

        LOG.info("/secure aufgerufen von User " + username);

        return SECURE_TMPL;
    }

    @RequestMapping("/logout")
    private String logout() {
        LOG.info("/logout aufgerufen.");
        SecurityContextHolder.clearContext();
        return LOGIN_TMPL;
    }

    private Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
