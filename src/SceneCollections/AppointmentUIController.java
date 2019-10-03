package SceneCollections;

import MainClass.MainScheduler;
import ObjectCreation.Appointment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class AppointmentUIController{

    @FXML
    private Button refreshButton;
    @FXML
    private Button detailsButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button createNewButton;
    @FXML
    private Button goBack;
    @FXML
    private TableView apptTableView;
    static List<List> allAppointments;
    //static ObservableList<Appointment> briefAppointments = FXCollections.observableArrayList();
    static List<Appointment> briefAppointments = new ArrayList<>();
    static int usID;
    static List<Object> userStuff;
    MainScheduler myMainScheduler;
    // Setup the Appointment Window
    public void setupApptUI(MainScheduler myApptScheduler, List<Object> userInfo)
    {
 
        this.usID = (int) userInfo.get(0);
        this.myMainScheduler = myApptScheduler;
        this.userStuff = userInfo;
        allAppointments =  DBCommands.DBRequest.getAllAppointments();
        /* Single Appointment List Information
        0 = appointmentId
        1 = customerId
        2 = userId
        3 = title
        4 = description
        5 = location
        6 = contact
        7 = type
        8 = url
        9 = start
        10 = end
        11 = createDate
        12 = createdBy
        13 = lastUpdate
        14 = lastUpdateBy
        */
        // Create the table columns
        TableColumn<Appointment, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));

        TableColumn<Appointment, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        
        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Appointment, Integer> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        
        TableColumn<Appointment, Integer> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        // Add the table columns to the table view
        apptTableView.getColumns().add(startColumn);
        apptTableView.getColumns().add(endColumn);
        apptTableView.getColumns().add(titleColumn);
        apptTableView.getColumns().add(customerColumn);
        apptTableView.getColumns().add(userColumn);
        
        // Populate the list of lists for the Table
        for (int i = 0; i < allAppointments.size(); i++)
        {
            List<Object> apptInfo = FXCollections.observableArrayList();
            apptInfo.add(allAppointments.get(i).get(9));
            apptInfo.add(allAppointments.get(i).get(10));
            apptInfo.add(allAppointments.get(i).get(3));
            apptInfo.add(allAppointments.get(i).get(1));
            apptInfo.add(allAppointments.get(i).get(2));
            apptInfo.add(allAppointments.get(i).get(0));
            apptInfo.add(allAppointments.get(i).get(9));
            apptInfo.add(allAppointments.get(i).get(8));
            Appointment newAppointment = new Appointment(apptInfo);
            briefAppointments.add(newAppointment);
            apptTableView.getItems().add(newAppointment);
        }
    }
    // Method to get back to the main menu
    public void goBackPressed()
    {
     myMainScheduler.startMain(usID, userStuff);
    }
    // Method for the details button
    public void detailsButtonPressed()
    {
        try {
            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CreateNewAppointmentUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        
        CreateNewAppointmentUIController controller = loader.getController();
        int selectedIndex = apptTableView.getSelectionModel().getSelectedIndex();
        int apptId = briefAppointments.get(selectedIndex).getAppointmentId();
        System.out.println(apptId);
        List<Object> completeAppt = DBCommands.DBRequest.getAppointmentByApptId(apptId);
        controller.setupApptDetails(completeAppt);
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setX(detailsButton.getScene().getWindow().getX() + detailsButton.getScene().getWindow().getWidth());
        detailsStage.setY(editButton.getScene().getWindow().getY());
        detailsStage.setScene(detailsScene);
        detailsStage.show();

        
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }
    // Method for the edit button
    public void editButtonPressed()
    {
        try {
            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CreateNewAppointmentUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        
        CreateNewAppointmentUIController controller = loader.getController();
        int selectedIndex = apptTableView.getSelectionModel().getSelectedIndex();
        int apptId = briefAppointments.get(selectedIndex).getAppointmentId();
            System.out.println(apptId);
        List<Object> completeAppt = DBCommands.DBRequest.getAppointmentByApptId(apptId);
        controller.setupApptDetails(usID, completeAppt);
        detailsStage.setX(editButton.getScene().getWindow().getX() + editButton.getScene().getWindow().getWidth());
        detailsStage.setY(editButton.getScene().getWindow().getY());
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setScene(detailsScene);
        detailsStage.show();

        
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    // method for the refresh button
    public void refreshButtonPressed()
    {
        List<List> apptQueryList = DBCommands.DBRequest.getAllAppointments();
        List<Appointment> smallAppointments = new ArrayList<>();
        // Setting up the column
        apptTableView.getItems().removeAll();
        apptTableView.getItems().clear();
        TableColumn<Appointment, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        
        TableColumn<Appointment, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        
        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Appointment, Integer> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        
        TableColumn<Appointment, Integer> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        
        apptTableView.getColumns().set(0, startColumn);
        apptTableView.getColumns().set(1, endColumn);
        apptTableView.getColumns().set(2, titleColumn);
        apptTableView.getColumns().set(3, customerColumn);
        apptTableView.getColumns().set(4, userColumn);
        /*
        apptTableView.getColumns().add(startColumn);
        apptTableView.getColumns().add(endColumn);
        apptTableView.getColumns().add(titleColumn);
        apptTableView.getColumns().add(customerColumn);
        apptTableView.getColumns().add(userColumn);
        */
        // Populate the list of lists for the Table
        for (int i = 0; i < apptQueryList.size(); i++)
        {
            List<Object> apptInfo = new ArrayList<>();
            apptInfo.add(apptQueryList.get(i).get(9));
            apptInfo.add(apptQueryList.get(i).get(10));
            apptInfo.add(apptQueryList.get(i).get(3));
            apptInfo.add(apptQueryList.get(i).get(1));
            apptInfo.add(apptQueryList.get(i).get(2));
            apptInfo.add(apptQueryList.get(i).get(0));
            apptInfo.add(apptQueryList.get(i).get(9));
            apptInfo.add(apptQueryList.get(i).get(8));
            
            System.out.println(apptInfo);
            Appointment newAppointment = new Appointment(apptInfo);
            smallAppointments.add(newAppointment);
            
        }
        briefAppointments = smallAppointments;
        apptTableView.getItems().setAll(smallAppointments);

    }
    // Method for the delete button
    public void deleteButtonPressed()
    {
        int selectedIndex = apptTableView.getSelectionModel().getSelectedIndex();
        int apptId = briefAppointments.get(selectedIndex).getAppointmentId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("If you cancel, all new entered information will be lost. Continue?");
                alert.showAndWait()
                        // Lambda to set up the response variable
                        .filter(response -> response == ButtonType.OK || response == ButtonType.CANCEL)
                        // Lambda to check the response using if statements
                        .ifPresent(response -> 
                        {
                            System.out.println(response.getButtonData());
                            if (response.getButtonData().equals(ButtonType.OK.getButtonData()) == true)
                            {
                                DBCommands.DBRemove.removeAppointment(apptId);
                                refreshButtonPressed();
                            }
                        });
    }
    // Method for the create new button
    public void createNewButtonPressed()
    {
        try {
            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CreateNewAppointmentUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        
        CreateNewAppointmentUIController controller = loader.getController();
        controller.setupApptDetails(usID);
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setX(editButton.getScene().getWindow().getX() + editButton.getScene().getWindow().getWidth());
        detailsStage.setY(editButton.getScene().getWindow().getY());
        detailsStage.setScene(detailsScene);
        detailsStage.show();

        
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    // Method for the weekly appointments calendar button
    public void weeksButtonPressed()
    {
        try {
            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CalendarViewUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        
        CalendarViewUIController controller = loader.getController();
        controller.setupCalendarViewUI(0);
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setScene(detailsScene);
        detailsStage.show();

        
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    // Method for the monthly appointments calendar button
    public void monthsButtonPressed()
    {
        try {
            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CalendarViewUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        
        CalendarViewUIController controller = loader.getController();
        controller.setupCalendarViewUI(1);
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setScene(detailsScene);
        detailsStage.show();

        
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    
}
