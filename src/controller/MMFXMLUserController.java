/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import db.MMDB;
import entity.User;
import db.UserDB;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * FXML Controller class
 *
 * @author felhasználó
 */
public class MMFXMLUserController implements Initializable {

    @FXML private TextField fieldUsernameSearch;
    @FXML private TextField fieldEmailSearch;
    @FXML private TableView tableUsers;
    @FXML private GridPane paneCreateUser;

    // data object
    private final ObservableList<User> data = FXCollections.observableArrayList();

    // database user object
    UserDB userDB = new UserDB();

    // the caller of this window
    private Object parent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTableData();
    }

    void init(MMFXMLDocumentController caller) {
        parent = caller;
    }
    
    // user search pushbutton clicked
    @FXML private void handleButtonSearchUser(ActionEvent event) {
        //System.out.println("You clicked Keresés button!");
        tableUsers.getItems().clear();
        // build where
        String where = "";
        //System.out.println("where1: " + where);
        where = buildWhere(where, "username", fieldUsernameSearch.getText());
        //System.out.println("where2: " + where);
        where = buildWhere(where, "email", fieldEmailSearch.getText());
        //System.out.println("where3: " + where);
        data.addAll(userDB.getAllUsers(where));
        tableUsers.setItems(data);
    }
    
    // buld where clause
    private String buildWhere(String where, String dbCol, String text) {
        if (text.length()> 0) {
            if (where.length()>0){
                where = where + " and ";
            } else {
                where = " where ";
            }
            if (text.contains("%")) {
                where = where + dbCol + " like '" + text + "'";
            } else {
                where = where + dbCol + " = '" + text + "'";
            }
        }
        return where;
    }

    // populate data into the table view
    public void setTableData(){
        // create table columns
        TableColumn colUsername = new TableColumn("Felhasználónév");
        colUsername.setMinWidth(300);
        colUsername.setCellFactory(TextFieldTableCell.forTableColumn());
        colUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        colUsername.setEditable(false);
        
        TableColumn colEmail = new TableColumn("E-mail cím");
        colEmail.setMinWidth(500);
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        colEmail.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<User, String> t) {
                        User actualUser = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        actualUser.setEmail(t.getNewValue());
                        userDB.modifyUser(actualUser);
                    }
                }
        );
        tableUsers.getColumns().addAll(colUsername, colEmail);
        data.addAll(userDB.getAllUsers());
        tableUsers.setItems(data);
    }
    
    // new user pushbutton clicked
    @FXML private void handleButtonNewUser(ActionEvent event) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //System.out.println("You clicked Új felhasználó.... button!");
        // call the new user function on the parent window
        //System.out.println("handleButtonCancel parent: " + parent);
        //System.out.println("handleButtonCancel parent.getClass(): " + parent.getClass());
        Class noparams[] = {};
        Method method = parent.getClass().getDeclaredMethod("userNewUserClicked", noparams);
        method.invoke(parent, null);
    }

    // delete user pushbutton clicked
    @FXML public boolean handleButtonDeleteUser(ActionEvent event) {
        //System.out.println("You clicked Felhasználó törlés button!");
        User actUser = (User) tableUsers.getSelectionModel().getSelectedItem();
        if (actUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba a felhasználó törlés során");
                alert.setHeaderText("Nincs kiválasztott felhasználó a táblázatban!");
                alert.showAndWait();
                return false;

        } else {
            // check the logged in username
            //System.out.println("A kiválasztott user: " + actUser.getUsername());
            //System.out.println("Felhasználó törlés, bejelentkezett felhasználó: " + MMDB.getLoggedinUsername());
            // confirmation logged in user delete
            if (actUser.getUsername().equals(MMDB.getLoggedinUsername())) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Marhamajor alkalmazás");
                alert.setHeaderText("Felhasználó törlés");
                alert.setContentText("A bejelentkezett felhasználót akarja törölni. Ha törli a felhasználót, egyúttal kilép az alkalmazásból. Biztosan törli a(z) " + actUser.getUsername() + " felhasználót?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // delete the user
                    userDB.deleteUser(actUser);
                    System.exit(0);
                    return true;
                } else return false;
            }
            
            // confirm the delete
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Marhamajor alkalmazás");
            alert.setHeaderText("Felhasználó törlés");
            alert.setContentText("Biztosan törli a(z) " + actUser.getUsername() + " felhasználót?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // delete the user
                userDB.deleteUser(actUser);
                tableUsers.getItems().clear();
                data.addAll(userDB.getAllUsers());
                tableUsers.setItems(data);
                return true;
            } else return false;
        }
    }
    
    // Cancel pushbutton clicked on the newuser window
    @FXML private void handleButtonCancelNewUser(ActionEvent event) {
        //System.out.println("You clicked Új felhasználó rögzítés Cancel button!");
        paneCreateUser.setVisible(false);
    }

  
}
