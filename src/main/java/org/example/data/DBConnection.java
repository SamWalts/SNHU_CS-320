/**
Artifact Enhancement
Author: Samuel Walters
Date: 10/2/24
This class is used to create a connection to the database.
This class uses the Singleton pattern to ensure only one connection is created for resource efficiency.
For production environments, the connection should be encrypted and the username and password should be stored in a secure location.
 */
package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection con;
    private static final Object lock = new Object();
    private final String jdbcUrl;
    private final String username;
    private final String password;

    /**
     * Constructor for the DBConnection class
     * Passed in for the testing environment
     * @param jdbcUrl
     * @param username
     * @param password
     */
    public DBConnection(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * No-arg constructor for the DBConnection class
     * Default values for the connection
     */
    public DBConnection() {
        // Default values for the no-arg constructor
        this.jdbcUrl = "jdbc:mysql://localhost:3306/contact_list";
        this.username = "root";
        this.password = "YES";
    }

    public Connection getDBConnection() {
        synchronized (lock) {
            try {
                if (con == null || con.isClosed()) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(jdbcUrl, username, password);
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException("Error establishing database connection", e);
            }
        }
        return con;
    }

    public static Connection getCon() {
        return con;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection", e);
        }
    }
}