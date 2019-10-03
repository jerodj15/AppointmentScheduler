package SceneCollections;

import MainClass.MainScheduler;
import ObjectCreation.Appointment;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class MainUIController {

    @FXML
    private Button customersButton;
    @FXML
    private Button apptButton;
    @FXML
    private Label welcomeUser;
    @FXML
    private Label totalApptsToday;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView mainTableView;
    
    // Create variables to setup the mainUI window
    private MainScheduler myMainScheduler;
    private int userID;
    static List<Object> userInformation;
    static ObservableList<List> allAppointments;
    static ObservableList<Appointment> briefAppointments = FXCollections.observableArrayList();
    static int beenClicked = 0;
    
// Initial setup of the MainUI window
    public void setupMainUI(MainScheduler myApptScheduler, List<Object> userInfo)
    {
        
        this.myMainScheduler = myApptScheduler;
        this.userInformation = userInfo;
        this.userID = (int) userInfo.get(0);
        allAppointments =  DBCommands.DBRequest.getAppointmentsByUserId(userID);
        // Set up the Table View
        TableColumn<Appointment, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));

        TableColumn<Appointment, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        
        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Appointment, Integer> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        mainTableView.getColumns().add(startColumn);
        mainTableView.getColumns().add(endColumn);
        mainTableView.getColumns().add(titleColumn);
        mainTableView.getColumns().add(customerColumn);
        
        // Populate the list of lists for the Table
        for (int i = 0; i < allAppointments.size(); i++)
        {
            ZonedDateTime ts = (ZonedDateTime) allAppointments.get(i).get(9);
            ZonedDateTime zonedStart = ZonedDateTime.of(ts.toLocalDateTime(), ZoneId.systemDefault());
            System.out.println(zonedStart.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
            int zonedStartDay = zonedStart.getDayOfYear();
            int todayDay = ZonedDateTime.now().getDayOfYear();
            List<Object> apptInfo = FXCollections.observableArrayList();
            if (zonedStartDay == todayDay)
            {
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
            mainTableView.getItems().add(newAppointment);
            }
        }
        totalApptsToday.setText("You have " + briefAppointments.size() + " appointments scheduled for today");

        // Personalize the label to include the userNamew
        welcomeUser.setText("Welcome " + userInformation.get(1));
        // Call the 15 minutes function
        if (beenClicked == 0)
        {
        appointmentFifteenPopUp();
        beenClicked++;
        }

    }
    // To go the the customer information section
    public void customerButtonPressed()
    {
        try {
            Stage customerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CustomersUI.fxml"));
            Pane customersPane = loader.load();
            CustomersUIController customersUIController = loader.getController();
            customersUIController.setupCustomerUI(userID, (String) userInformation.get(1));
            Scene custScene = new Scene(customersPane);
            customerStage.setTitle("Customers Menu");
            customerStage.setScene(custScene);
            customerStage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
        
    }
    // To go to the appointment information window
    public void appointmentButtonPressed()
    {
        myMainScheduler.startAppt(userInformation);
    }
   // Method for the 15 minute appointment popup window
    public void appointmentFifteenPopUp()
    {
        ZonedDateTime rightNow = ZonedDateTime.now();
        ZonedDateTime startZoned;
        Timestamp startTimestamp;
        for (int i = 0; i < briefAppointments.size(); i++)
        {
            startTimestamp = Timestamp.valueOf(briefAppointments.get(i).getStartTime());
            startZoned = startTimestamp.toInstant().atZone(ZoneId.systemDefault());
            if ((startZoned.isBefore(rightNow.plusMinutes(15))) == true && (startZoned.isAfter(rightNow.minusMinutes(1)) == true))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("You have an appointment coming up within 15 minutes");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
        }
    }
    // Method to go to the reports section 
    public void reportsButtonPressed()
    {
        try {
        Stage reportsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/ReportsUI.fxml"));
        Pane reportsPane = loader.load();
        ReportsUIController reportsUIController = loader.getController();
        reportsUIController.setupReportsUI(userInformation, briefAppointments);
        Scene reportScene = new Scene(reportsPane);
        reportsStage.setTitle("Reports Menu");
        reportsStage.setScene(reportScene);
        reportsStage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
}
