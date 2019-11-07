/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author felhasználó
 */
public class MMFXMLDocumentController implements Initializable {
    
    //@FXML
    //private AnchorPane mainPane;

    @FXML private MenuBar menuBar;
    @FXML private MenuItem menuitemShowUsers;
    @FXML private MenuItem menuitemDeleteUser;
    @FXML private Label mainLabel;
    @FXML private AnchorPane contentPane;
    
    // warning: the Controller name comes from the FXML file, include fx:id field!
    @FXML private MMFXMLNewUserController newuserController;
    private MMFXMLUserController userController;
    private MMFXMLDocumentController thisController;
    
    private final String MMFXML_LOGIN = "/view/MMFXMLLogin.fxml";
    private final String MMFXML_USER = "/view/MMFXMLUser.fxml";
    private final String MMFXML_NEWUSER = "/view/MMFXMLNewUser.fxml";
    
    // last loaded FXML URL of the contentPane
    static private String contentFXML = null;

    /*
    private void setContentFXML(String fxml){
        contentFXML = fxml;
    }
    
    private String getContentFXML(){
        return contentFXML;
    }
    */
    
    @FXML private void handleMenuitemClose(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML private void handleMenuUserMaintenanceShowing(Event event) throws IOException {
        //System.out.println("Felhasználók karbantartása menu showing!");
        //System.out.println("Content pane resource: " + contentFXML);
        menuitemShowUsers.setDisable(contentFXML.equals(MMFXML_USER));
        menuitemDeleteUser.setDisable(!contentFXML.equals(MMFXML_USER));
    }

    
    @FXML private void handleMenuShowUsers(ActionEvent event) throws IOException {
        //System.out.println("You clicked Felhasználók megjelenítése menuitem!");
        mainLabel.setText("Felhasználók karbantartása");
        mainLabel.setVisible(true);
        loadUserPage();
    }

    @FXML private void handleMenuNewUser(ActionEvent event) {
        //System.out.println("You clicked Új felhasználó... menuitem!");
        loadNewUserPage();
    }    

    @FXML private void handleMenuDeleteUser(ActionEvent event) {
        //System.out.println("You clicked Felhasználó törlés... menuitem!");
        deleteUser(event);
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println("MMFXMLDocumentController.java initialize(), this: " + this);
        //System.out.println("newuserController: " + newuserController);
        // save this object to pass child windows
        thisController = this;
        try {
            // load the login page at the start of the application
            loadLoginPage();
        } catch (IOException ex) {
            Logger.getLogger(MMFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // open login page
    private void loadLoginPage() throws IOException {
        //System.out.println("MMFXMLDocumentController.java loadLoginPage()");
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(MMFXML_LOGIN));
        contentFXML = MMFXML_LOGIN;
        Pane loginPane = (Pane) loginLoader.load();
        try {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loginPane);
            // center loginPane
            //System.out.println("contentPane.getPrefWidth(): " + contentPane.getPrefWidth());
            //System.out.println("loginPane.getPrefWidth(): " + loginPane.getPrefWidth());
            loginPane.setLayoutX((contentPane.getPrefWidth()-loginPane.getPrefWidth())/2);
            
            MMFXMLLoginController loginController = loginLoader.getController();
            loginController.loggedInProperty().addListener((obs, wasLoggedIn, isNowLoggedIn) -> {
                if (isNowLoggedIn) {
                    try {
                        closeContentPage();
                    } catch (IOException ex) {
                        Logger.getLogger(MMFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

    // close tha actual pane in contentPane
    @FXML private void closeContentPage() throws IOException {
        try{
            contentPane.getChildren().clear();
            contentFXML = "";
            mainLabel.setText("");
            menuBar.setVisible(true);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load the user maintenance page
    @FXML private void loadUserPage() throws IOException {
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(MMFXML_USER));
        contentFXML = MMFXML_USER;
        Pane userPane = (Pane) userLoader.load();
        try {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(userPane);
            // center userPane
            //System.out.println("contentPane.getPrefWidth(): " + contentPane.getPrefWidth());
            //System.out.println("loginPane.getPrefWidth(): " + loginPane.getPrefWidth());
            userPane.setLayoutX((contentPane.getPrefWidth()-userPane.getPrefWidth())/2);
            mainLabel.setText("Felhasználók");
            mainLabel.setVisible(true);

            // set userController with the new User pane object
            userController = userLoader.getController();
            userController.init(thisController);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

    // the Cancel puchbutton clicked on the new user window
    public void newuserCancelClicked() throws IOException{
        System.out.println("MMFXMLDocumentController newuserCancelClicked() called...");
        closeContentPage();
    }

    // load the new user page from the menuitem or pushbutton
    private void loadNewUserPage() {
        mainLabel.setText("Új felhasználó rögzítése");
        mainLabel.setVisible(true);
        try {
            // load the login page at the start of the application
            FXMLLoader newuserLoader = new FXMLLoader(getClass().getResource(MMFXML_NEWUSER));
            contentFXML = MMFXML_NEWUSER;
            Pane newuserPane = (Pane) newuserLoader.load();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newuserPane);
            // center alignment
            newuserPane.setLayoutX((contentPane.getPrefWidth()-newuserPane.getPrefWidth())/2);

            // refresh newuserController with the new NewUser pane object
            //System.out.println("newuserLoader: " + newuserLoader.getController());
            newuserController = newuserLoader.getController();
            newuserController.init(thisController);
            
            //System.out.println("newuserController: " + newuserController);
            //System.out.println("thisController: " + thisController);
        } catch (IOException ex) {
            Logger.getLogger(MMFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // new user ushbutton clicked on the user maintenance window
    public void userNewUserClicked(){
        loadNewUserPage();
    }

    // delete the user
    private void deleteUser(ActionEvent event) {
        // call the user maintenance window's delete user pushbutton action
        System.out.println("contentFXML: " + contentFXML);
        System.out.println("contentPane.getChildren().size(): " + contentPane.getChildren().size());
        userController.handleButtonDeleteUser(event);
    }
}
    
    
