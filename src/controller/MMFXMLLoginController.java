/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import db.MMDB;
import db.UserDB;

/**
 * FXML Controller class
 *
 * @author felhasználó
 */
public class MMFXMLLoginController implements Initializable {

    @FXML private TabPane tabpaneMain;
    @FXML private TextField fieldUsername;
    @FXML private TextField fieldPassword;
    
    // warning: the Controller name comes from the FXML file, include fx:id field!
    @FXML private MMFXMLNewUserController newuserController;

    private final UserDB userdb = new UserDB();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println("MMFXMLLoginController.java initialize(), this: " + this);
        //System.out.println("This: " + this.getClass());
        //System.out.println("mmFXMLNewUserController: " + newuserController);
        newuserController.init(this);
    } 
    
    // Login button is clicked
    @FXML private void handleButtonLogin(ActionEvent event) {
        if (validateLoginForm()) {
            login(fieldUsername.getText(), fieldPassword.getText());
        }
    }

    // Cancel button is clicked
    @FXML private void handleButtonCancel(ActionEvent event) {
        System.exit(0);
    }
    
    // successful login listener
    private final BooleanProperty loggedIn = new SimpleBooleanProperty();
    
    public BooleanProperty loggedInProperty() {
        return loggedIn ;
    }

    public final boolean isLoggedIn() {
        return loggedInProperty().get();
    }

    public final void setLoggedIn(boolean loggedIn) {
        loggedInProperty().set(loggedIn);
    }

    // validate Login form elements
    private boolean validateLoginForm() {
        if (fieldUsername.getLength()== 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nincs  minden kötelező adat kitöltve");
            alert.setHeaderText("Felhasználónevet kötelező megadni!");
            alert.showAndWait();
            fieldUsername.requestFocus();
            return false;
        }

        if (fieldPassword.getLength()== 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nincs  minden kötelező adat kitöltve");
            alert.setHeaderText("Jelszót kötelező megadni!");
            alert.showAndWait();
            fieldPassword.requestFocus();
            return false;
        }
        return true;
    }
    
    // try to loigin into database
    public boolean login(String username, String password){
        if (userdb.loginUser(username, password)) {
            System.out.println("Sikeres bejelentkezés!");
            MMDB.setLoggedinUsername(username);
            setLoggedIn(true);
            return true;
        } else {
            System.out.println("Hiba a bejelentkezés során!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba a bejelentkezés során");
            alert.setHeaderText("A megadott adatokkal nem sikerült bejelentkezni!");
            alert.showAndWait();
            return false;
        }
    }
    
    // Cancel clicked on new user window
    public void newuserCancelClicked(){
        //System.out.println("Cancel clicked on new user window");
        tabpaneMain.getSelectionModel().select(0);
    }

}
