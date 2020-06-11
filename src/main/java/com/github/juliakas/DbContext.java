package com.github.juliakas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbContext {

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:01521/dba.sqli_test?autoReconnect=true&amp;allowMultiQueries=true","JR","JR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
