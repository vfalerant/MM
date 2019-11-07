/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Ferenc Varga
 * Plain Old Java Object for Users
 * (Entity)
 */
public class User {
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty email;
    private final SimpleStringProperty id;

    public User(){
        this.id = new SimpleStringProperty("");
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
    }

    public User(String username, String password, String email){
        this.id = new SimpleStringProperty("");
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
    }

        public User(Integer id, String username, String password, String email){
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
    }

    
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String email) {
        this.id.set(email);
    }
}
