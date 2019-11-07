/*
 * Marhamajor application
 */
package marhamajor;

import db.MMDB;
import db.UserDB;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author Ferenc Varga
 */
public class Marhamajor extends Application {
    
    // database variable
    MMDB mmdb = new MMDB();
    UserDB userdb = new UserDB();
    //main window
    static private Stage mainWindow = null;
    
    private void initDB() {
        //System.out.println("Adatbázis inicializálása");
        //userdb.showUsersMetaData();
        //userdb.addUser("a","a","a");
        //System.out.println(userdb.getAllUsers());
        /*
        ArrayList<User> users = userdb.getAllUsers();
        for(User u : users)
        {
            System.out.println(u.getId() +", " +  u.getUsername() + ", " + u.getPassword() + ", " + u.getEmail());
        }
        */
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        // database initialization
        initDB();
        mainWindow = stage;

        //System.out.println("Marhamajor.java start()");
        Parent root = FXMLLoader.load(getClass().getResource("/view/MMFXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Marhamajor alkalmazás");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Stage getStage(){
        return mainWindow;
    }
    
    public void successfullLogin(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/MMFXMLDocument.fxml"));
            Scene scene = new Scene(root);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (IOException ex) {
            Logger.getLogger(Marhamajor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    
}
