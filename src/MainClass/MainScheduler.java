package MainClass;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import SceneCollections.*;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class MainScheduler extends Application {
    
    private Stage primaryStage;
    private Pane loginPane;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        startLogin();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBCommands.DBConnect.connectToDB();
        launch(args);
        DBCommands.DBConnect.stopConnection();
    }
    // Start the login window
    public void startLogin()
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScheduler.class.getResource("/SceneCollections/LoginUI.fxml"));
            Pane loginPane = (Pane) loader.load();
            LoginUIController loginUIController = loader.getController();
            loginUIController.setupLoginUI(this);
            Scene loginScene = new Scene(loginPane);
            primaryStage.setTitle("Login");
            primaryStage.setScene(loginScene);
            primaryStage.show();
            
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            
        }
        
    }
    // Start the main window in the same window as the login window
    public void startMain(int userID, List<Object> userInfo)
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScheduler.class.getResource("/SceneCollections/MainUI.fxml"));
            Pane mainPane = (Pane) loader.load();
            MainUIController mainUIController = loader.getController();
            mainUIController.setupMainUI(this, userInfo);
            Scene mainScene = new Scene(mainPane);
            primaryStage.setTitle("Main Menu");
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    // Start the appointment info in the same window as the mainui
    public void startAppt(List<Object> userInfo)
    {
         try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScheduler.class.getResource("/SceneCollections/AppointmentUI.fxml"));
            Pane apptPane = loader.load();
            AppointmentUIController appointmentUIController = loader.getController();
            appointmentUIController.setupApptUI(this, userInfo);
            Scene apptScene = new Scene(apptPane);
            primaryStage.setTitle("Appointment Menu");
            primaryStage.setScene(apptScene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
