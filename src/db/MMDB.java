/*
 * Marhamajor application's DB class
 */
package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ferenc Varga
 * Marhamajor application's DB class
 * 
 */
public class MMDB {
    // database variables
    // use local Derby database
    final String JDBC_DRIVER="org.apache.derby.jdbc.EmbeddedDriver";
    // create database if it has not created yet
    final String URL = "jdbc:derby:MMBD;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";
    
    static private Connection conn = null;
    static private String loggedinUsername = null;
    static private Statement createStatement = null;
    private DatabaseMetaData dbmd = null;

 
    public MMDB(){
       
       // try to connect to dabase 
       try {
            conn = DriverManager.getConnection(URL);
            //System.out.println("Database connection is ready...");
        } catch (SQLException ex) {
            System.out.println("SQL error (connection): "+ex);
        }
        
        // create sql statement
        try {
            createStatement = conn.createStatement();
        } catch (SQLException ex) {
            System.out.println("SQL error (createStatement): "+ex);
        }
        
        // get metadata from the database
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("SQL error (getMetaData): "+ex);
        }
        
        // check the USERS database table
        try {
            ResultSet rs = dbmd.getTables(null, "MM", "USERS", null);
            if (!rs.next())
            {
                // drop table
                //createStatement.execute("drop table users");
                //System.out.println("Table users dropped");
                createStatement.execute("create table users (id int not null primary key generated always as identity (start with 1, increment by 1), username varchar(20), password varchar(20), email varchar(20))");
                //System.out.println("Table users created");
            }
        } catch (SQLException ex) {
            System.out.println("SQL error (create table USERS): "+ex);
        }
    }
       
    public static Connection getConnection(){
        return conn;
    }

    public static void setLoggedinUsername(String username){
        loggedinUsername = username;
    }

    public static String getLoggedinUsername(){
        return loggedinUsername;
    }

    
}   
