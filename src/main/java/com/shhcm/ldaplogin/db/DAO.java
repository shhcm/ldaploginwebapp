package com.shhcm.ldaplogin.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

@Component
public class DAO {

    private static final Logger LOG = LoggerFactory.getLogger(DAO.class);

    private static final String SELECT_AUTHORITIES_FOR_USER = "SELECT AUTHORITY FROM USER_AUTHORITIES WHERE USERNAME=?";

    @Autowired
    DataSource dataSource;

    public List<String> getAuthoritiesForUser(String username) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        LOG.info("Looking up user authorities for user " + username);

        List<String> results = jdbcTemplate.query(
                SELECT_AUTHORITIES_FOR_USER,
                new Object[] {username},
                new int[] {Types.VARCHAR},
                (ResultSet resultSet, int rowNum) -> {
                    return resultSet.getString("AUTHORITY");
                });

        LOG.info("Found user authorities for user " + username + ": " + results);
        return results;
    }
 }
