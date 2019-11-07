/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import entity.User;

/**
 *
 * @author Ferenc Varga
 * Users data functions
 */
public class UserDB {
    
    public void UserDB(){
    }
    
    // insert a new user into the database users table        
    public boolean addUser(String username, String password, String email){
        try {
            if (!isUsernameUnique(username)) return false;
            String sql = "insert into users (username, password, email) values (?, ?, ?)";
            PreparedStatement preparedStatement = MMDB.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            //System.out.println("addUser username parameter: " + username);
            preparedStatement.setString(2, password);
            //System.out.println("addUser password parameter: " + password);
            preparedStatement.setString(3, email);
            //System.out.println("addUser email parameter: " + email);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("SQL error (add user): "+ex);
            return false;
        }
        return true;
    }
    
    // show all users from the database users table
    public void showAllUsers(){
        String sql = "select * from users";
        try {
            ResultSet rs = MMDB.getConnection().createStatement().executeQuery(sql);
            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                //System.out.println(username + " ," + password + " ," + email);
            }
        } catch (SQLException ex) {
            System.out.println("SQL error (show all users): "+ex);
        }
    }
    
    // show the columns of the database users table
    public void showUsersMetaData(){
        String sql = "select * from users";
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            rs = MMDB.getConnection().createStatement().executeQuery(sql);
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i<=columnCount; i++){
                System.out.println(rsmd.getColumnName(i));
            }
        } catch (SQLException ex) {
            System.out.println("SQL error (show users metadata): "+ex);
        }
    }
    
    // get all users' data
    public ArrayList<User> getAllUsers(){
        String sql = "select * from users";
        ArrayList<User> users = null;
        try {
            ResultSet rs = MMDB.getConnection().createStatement().executeQuery(sql);
            users = new ArrayList();
            while(rs.next()){
                User actualUser = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
                users.add(actualUser);
            }
        } catch (SQLException ex) {
            System.out.println("SQL error (get all users): "+ex);
        }
        return users;
    }

    // get all users' data with where clause
    public ArrayList<User> getAllUsers(String where){
        String sql = "select * from users";
        if (where.length()>0) sql = sql + " " + where;
        ArrayList<User> users = null;
        try {
            ResultSet rs = MMDB.getConnection().createStatement().executeQuery(sql);
            users = new ArrayList();
            while(rs.next()){
                User actualUser = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"));
                users.add(actualUser);
            }
        } catch (SQLException ex) {
            System.out.println("SQL error (get all users): "+ex);
        }
        return users;
    }
    
    // insert a new user into the database users table        
    public void addUser(User user){
        try {
            String sql = "insert into users (username, password, email) values (?, ?, ?)";
            PreparedStatement preparedStatement = MMDB.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("SQL error (add user): "+ex);
        }
    }

    // modify user in the database users table        
    public void modifyUser(User user){
        try {
            String sql = "update users set username = ?, password = ?, email= ? where id = ?";
            PreparedStatement preparedStatement = MMDB.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setInt(4, Integer.parseInt(user.getId()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("SQL error (modify user): "+ex);
        }
    }

    // modify user in the database users table        
    public void deleteUser(User user){
        try {
            String sql = "delete from users where username = ?";
            PreparedStatement preparedStatement = MMDB.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("SQL error (delete user): "+ex);
        }
    }
    
    // show all users from the database users table
    public boolean loginUser(String username, String password){
        String sql = "select * from users where username = ? and password = ?";
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = MMDB.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.execute();
            rs = preparedStatement.executeQuery();
            boolean rsSize = rs.next();
            //System.out.println("Resultset mérete: " + rsSize);
            return rsSize;
        } catch (SQLException ex) {
            System.out.println("SQL error (loginUsers: "+ex);
        }
        return false;
    }
    
    // show all users from the database users table
    public boolean isUsernameUnique(String username){
        String sql = "select * from users where username = ?";
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = MMDB.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            rs = preparedStatement.executeQuery();
            boolean rsSize = rs.next();
            //System.out.println("Resultset mérete: " + rsSize);
            return !rsSize;
        } catch (SQLException ex) {
            System.out.println("SQL error (isUsernameUnique: "+ex);
        }
        return false;
    }
}
