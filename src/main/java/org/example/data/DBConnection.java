/*
Artifact Enhancement
Author: Samuel Walters
Date: 10/2/24
This class is used to create a connection to the database.
This class uses the Singleton pattern to ensure only one connection is created for resource efficiency.
 */
package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection con;
    private static final Object lock = new Object();

    public Connection getDBConnection() {
        synchronized (lock) {
            try {
                if (con == null || con.isClosed()) {
                    String dUrl = "jdbc:mysql://localhost:3306/contact_list";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(dUrl, "root", "YES");
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

    public static void setCon(Connection aCon) {
        con = aCon;
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