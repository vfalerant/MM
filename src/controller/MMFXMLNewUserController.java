/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import db.UserDB;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author felhasználó
 */
public class MMFXMLNewUserController implements Initializable {

    @FXML private TextField fieldUsernameReg;
    @FXML private TextField fieldPasswordReg;
    @FXML private TextField fieldConfirmPasswordReg;
    @FXML private TextField fieldEmailReg;

    private final UserDB userdb = new UserDB();
    
    // the caller of this window is Login page or User maintenance, save the caller
    private Object parent;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //System.out.println("MMFXMLNewUserController initialize() hívás, this:" + this);
    }  

    public void init(MMFXMLLoginController caller) {
        //System.out.println("MMFXMLNewUserController init() parameter: " + caller);
        //System.out.println("MMFXMLNewUserController init() parameter class: " + caller.getClass());
        parent = caller;
    }

    public void init(MMFXMLDocumentController caller) {
        parent = caller;
    }

    @FXML private void handleButtonRegistration(ActionEvent event) {
        if (validateRegistrationForm()) {
            if (userdb.addUser(fieldUsernameReg.getText(), fieldPasswordReg.getText(), fieldEmailReg.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Új felhasználó felvitel");
                alert.setHeaderText("A felhasználó rögzítése sikeresen megtörtént!");
                alert.showAndWait();
                fieldUsernameReg.clear();
                fieldPasswordReg.clear();
                fieldConfirmPasswordReg.clear();
                fieldEmailReg.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                System.out.println("Hiba az új felhasználó rögzítése során!");
                alert.setTitle("Hiba az új felhasználó rögzítése során");
                alert.setHeaderText("A megadott adatokkal nem sikerült rögzíteni az új felhasználót!");
                alert.showAndWait();
            }
        }
    }

    @FXML private void handleButtonCancel(ActionEvent event) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //System.out.println("Cancel button cicked...");
        //System.out.println("handleButtonCancel parent: " + parent);
        //System.out.println("handleButtonCancel parent.getClass(): " + parent.getClass());
        Class noparams[] = {};
        Method method = parent.getClass().getDeclaredMethod("newuserCancelClicked", noparams);
        method.invoke(parent, null);
    }
    
    // validate Registration form elements
    private boolean validateRegistrationForm() {
        if (fieldUsernameReg.getLength()== 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nincs  minden kötelező adat kitöltve");
            alert.setHeaderText("Felhasználónevet kötelező megadni!");
            alert.showAndWait();
            fieldUsernameReg.requestFocus();
            return false;
        }

        if (!userdb.isUsernameUnique(fieldUsernameReg.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Felhasználónév ellenőrzés");
            alert.setHeaderText("Ezzel a névvel már létezik felhasználó, adjon meg másik nevet!");
            alert.showAndWait();
            fieldPasswordReg.requestFocus();
            return false;
        }

        if (fieldPasswordReg.getLength()== 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nincs  minden kötelező adat kitöltve");
            alert.setHeaderText("Jelszót kötelező megadni!");
            alert.showAndWait();
            fieldPasswordReg.requestFocus();
            return false;
        }

        if (!(fieldPasswordReg.getText()).equals(fieldConfirmPasswordReg.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Jelszó ellenőrzés");
            alert.setHeaderText("A jelszó és a jelszó megerősítése mezők nem egyeznek meg!");
            alert.showAndWait();
            fieldPasswordReg.requestFocus();
            return false;
        }

        if (fieldEmailReg.getLength()<5 || !(fieldEmailReg.getText()).contains("@") || !(fieldEmailReg.getText()).contains(".")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("E-mail ellenőrzés");
            alert.setHeaderText("A megadott e-mail cím nem megfelelő formátumú!");
            alert.showAndWait();
            fieldPasswordReg.requestFocus();
            return false;
        }

        return true;
    }
}
