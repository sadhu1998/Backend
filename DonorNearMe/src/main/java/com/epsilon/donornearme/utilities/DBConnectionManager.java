package com.epsilon.donornearme.utilities;

import com.epsilon.donornearme.Common;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DBConnectionManager {
    //    @Value("${spring.datasource.username}")
//    private String dburl = "jdbc:postgresql://35.238.212.200:5432/donornearme";
//    //    @Value("${spring.datasource.url}")
//    private String username = "admin";
//    //    @Value("${spring.datasource.password}")
//    private String password = "admin";

    public Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        try {
            return DriverManager.getConnection(Common.DB_URL, Common.DB_USERNAME, Common.DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new Exception("Error connecting to DB");
    }

}
