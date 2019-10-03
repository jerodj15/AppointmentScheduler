/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SceneCollections;

import Misc.CheckEntries;
import Misc.DateCompare;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Jerod
 */
public class CreateNewAppointmentUIController {

    @FXML
    private TextField titleTF;
    @FXML
    private TextField descriptionTF;
    @FXML
    private TextField locationTF;
    @FXML
    private TextField contactTF;
    @FXML
    private TextField typeTF;
    @FXML
    private TextField urlTF;
    @FXML
    private TextField startTF;
    @FXML
    private TextField endTF;
    @FXML
    private TextField createDateTF;
    @FXML
    private TextField createByTF;
    @FXML
    private TextField lastUpTF;
    @FXML
    private TextField lastUpByTF;
    @FXML
    private ComboBox custNameCombo;
    @FXML
    private ComboBox userNameCombo;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox startAMPM;
    @FXML
    private ComboBox endAMPM;

    // Create variable to store needed information for setup
    private int cancelCommand = 0;
    private int usID;
    private List<Object> appointmentInfo;
    private List<Object> customerInfo;
    private List<Object> userInfo;
    private List<Object> usInfo;
    private int userID;
    private int custID;
    private boolean autoSet = false;
    private String userName;
    private String customerName;
    private List<List> listOfCustomers = DBCommands.DBRequest.getAllCustomers();
    private List<List> listOfUsers = DBCommands.DBRequest.getAllUsers();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm");
    DateTimeFormatter submitdtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm:ss");

    
    // Action for when the cancel button is pressed
    @FXML
    private void cancelButtonPressed(ActionEvent event) {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        // For editing an appointment
        if (cancelCommand == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("If you cancel, all new entered information will be lost. Continue?");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK || response == ButtonType.CANCEL)
                        .ifPresent(response -> 
                        {
                            System.out.println(response.getButtonData());
                            if (response.getButtonData().equals(ButtonType.OK.getButtonData()) == true)
                            {
                                currentStage.close();
                            }
                        });

        }
        // For creating a new appointment
        if (cancelCommand == 2) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("If you cancel, all new entered information will be lost. Continue?");
                alert.showAndWait()
                        // Lambda to set up alert window
                        .filter(response -> response == ButtonType.OK || response == ButtonType.CANCEL)
                        .ifPresent(response -> 
                        {
                            System.out.println(response.getButtonData());
                            if (response.getButtonData().equals(ButtonType.OK.getButtonData()) == true)
                            {
                                currentStage.close();
                            }
                        });
        } else {
            currentStage.close();
        }
    }

    // Action for when the saved button is pressed
    @FXML
    private void saveButtonPressed(ActionEvent event) {

        // For editing an appointment
        if (cancelCommand == 1) {
            String sTime = startTF.getText().concat(":00");
            Timestamp ts1 = Timestamp.valueOf(sTime);
            ZonedDateTime startZonedDateTime = null;
            // Make sure the times are the correct time format with the AM/PM box selection
            if (startAMPM.getSelectionModel().getSelectedIndex() == 0 || (ts1.toLocalDateTime().getHour() == 12)) {
                startZonedDateTime = ts1.toInstant().atZone(ZoneId.systemDefault());
            }
            if (startAMPM.getSelectionModel().getSelectedIndex() == 1) {
                if (ts1.toLocalDateTime().getHour() == 12) {
                    startZonedDateTime = ts1.toInstant().atZone(ZoneId.systemDefault());
                } else {
                    startZonedDateTime = ts1.toInstant().atZone(ZoneId.systemDefault()).plusHours(12);
                }
            }
            // add the :00 for the minutes to submit the time
            String eTime = endTF.getText().concat(":00");
            Timestamp ts2 = null;
            try {
               ts2 = Timestamp.valueOf(eTime);
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("The start and end time format is yyyy-mm-dd hh:mm");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
            
            ZonedDateTime endZonedDateTime = null;
            if (endAMPM.getSelectionModel().getSelectedIndex() == 0 || (ts2.toLocalDateTime().getHour() == 12)) {
                endZonedDateTime = ts2.toInstant().atZone(ZoneId.systemDefault());
            }
            if (endAMPM.getSelectionModel().getSelectedIndex() == 1) {
                if (ts2.toLocalDateTime().getHour() == 12) {
                    endZonedDateTime = ts2.toInstant().atZone(ZoneId.systemDefault());
                } else {
                    endZonedDateTime = ts2.toInstant().atZone(ZoneId.systemDefault()).plusHours(12);
                }
            }
            // Get the selected user for the appointment
            int selectedUser = userNameCombo.getSelectionModel().getSelectedIndex();
            String selUserName = (String) userNameCombo.getItems().get(selectedUser);
            for (int i = 0; i < userNameCombo.getItems().size(); i++) {
                if (selUserName.matches(listOfUsers.get(i).get(1).toString())) {
                    userID = (int) listOfUsers.get(i).get(0);
                }
            }
            boolean isOkay = DateCompare.isAllCorrect(startZonedDateTime, endZonedDateTime);
            int apptID = (Integer) appointmentInfo.get(0);
            boolean isNotConflicting = DateCompare.isInAvailableTimeSlot(startZonedDateTime, endZonedDateTime, userID, apptID);
            // Clear the error list for the DateCompare errors lists
            DateCompare.clearList();
            String start = startZonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).format(submitdtf);
            String end = endZonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).format(submitdtf);
            if ((isOkay == true) && (isNotConflicting == true)) {
                int selectedCustomer = custNameCombo.getSelectionModel().getSelectedIndex();
                String selCusName = (String) custNameCombo.getItems().get(selectedCustomer);
                for (int i = 0; i < custNameCombo.getItems().size(); i++) {
                    if (selCusName.matches(listOfCustomers.get(i).get(1).toString())) {
                        custID = (int) listOfCustomers.get(i).get(0);
                    }
                }
                // Add all the items to a list for submission to the database
                List<Object> userVals = new ArrayList<>();
                userVals.add(custID);
                userVals.add(userID);
                userVals.add(titleTF.getText());
                userVals.add(descriptionTF.getText());
                userVals.add(locationTF.getText());
                userVals.add(contactTF.getText());
                userVals.add(typeTF.getText());
                userVals.add(urlTF.getText());
                userVals.add(start);
                userVals.add(end);
                userVals.add(lastUpTF.getText());
                userVals.add(lastUpByTF.getText());
                userVals.add(appointmentInfo.get(0));
                
                // Begin running checks on the information in the list
                boolean isAllGood = CheckEntries.isCorrectDateTime(userVals);
                if (isAllGood == true) {
                    DBCommands.DBSave.saveEditedAppointment(userVals);
                    Stage currentStage = (Stage) cancelButton.getScene().getWindow();
                    currentStage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Make sure all fields contain entries and the date and times are correct");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
                }

            } else if (isOkay != true && isNotConflicting == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("The starting and ending date and / or time may not work with the company hours or business days. Please revise.");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            } else if (isOkay == true && isNotConflicting != true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("The time and / or date has a scheduling conflict with another appointment. Please revise.");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please check all boxes and make sure everything is correct");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
        }
        // For creating a new appointment
        if (cancelCommand == 2) {
            String sTime = startTF.getText().concat(":00");
            Timestamp ts1 = Timestamp.valueOf(sTime);
            ZonedDateTime startZonedDateTime = null;
            int startSelectAP = startAMPM.getSelectionModel().getSelectedIndex();
            System.out.println(startSelectAP);
            if (startAMPM.getSelectionModel().getSelectedIndex() == 0) {
                startZonedDateTime = ts1.toInstant().atZone(ZoneId.systemDefault());
            }
            if (startAMPM.getSelectionModel().getSelectedIndex() == 1) {
                startZonedDateTime = ts1.toInstant().atZone(ZoneId.systemDefault()).plusHours(12);
            }
            System.out.println(startZonedDateTime.toLocalDateTime().format(submitdtf));
            String eTime = endTF.getText().concat(":00");
            Timestamp ts2 = Timestamp.valueOf(eTime);
            ZonedDateTime endZonedDateTime = null;
            int endSelectAP = endAMPM.getSelectionModel().getSelectedIndex();
            System.out.println(endSelectAP);
            if (endAMPM.getSelectionModel().getSelectedIndex() == 0) {
                endZonedDateTime = ts2.toInstant().atZone(ZoneId.systemDefault());
            }
            if (endAMPM.getSelectionModel().getSelectedIndex() == 1) {
                endZonedDateTime = ts2.toInstant().atZone(ZoneId.systemDefault()).plusHours(12);
            }
            int selectedCustomer = custNameCombo.getSelectionModel().getSelectedIndex();
            String selCusName = (String) custNameCombo.getItems().get(selectedCustomer);
            for (int i = 0; i < custNameCombo.getItems().size(); i++) {
                if (selCusName.matches(listOfCustomers.get(i).get(1).toString())) {
                    custID = (int) listOfCustomers.get(i).get(0);
                }
            }
            int selectedUser = userNameCombo.getSelectionModel().getSelectedIndex();
            String selUserName = (String) userNameCombo.getItems().get(selectedUser);
            for (int i = 0; i < userNameCombo.getItems().size(); i++) {
                if (selUserName.matches(listOfUsers.get(i).get(1).toString())) {
                    userID = (int) listOfUsers.get(i).get(0);
                }
            }
            boolean isOkay = DateCompare.isAllCorrect(startZonedDateTime, endZonedDateTime);
            boolean isNotConflicting = DateCompare.isInAvailableTimeSlot(startZonedDateTime, endZonedDateTime, userID);
            DateCompare.clearList();
            String start = startZonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).format(submitdtf);
            String end = endZonedDateTime.toLocalDateTime().atZone(ZoneId.systemDefault()).format(submitdtf);
            if (isOkay == true && isNotConflicting == true) {

                DateCompare.clearList();
                List<Object> userVals = new ArrayList<>();
                userVals.add(custID);
                userVals.add(userID);
                userVals.add(titleTF.getText());
                userVals.add(descriptionTF.getText());
                userVals.add(locationTF.getText());
                userVals.add(contactTF.getText());
                userVals.add(typeTF.getText());
                userVals.add(urlTF.getText());
                userVals.add(start);
                userVals.add(end);
                userVals.add(lastUpTF.getText());
                userVals.add(lastUpByTF.getText());
                DBCommands.DBSave.saveNewAppointment(userVals);
                Stage currentStage = (Stage) cancelButton.getScene().getWindow();
                currentStage.close();
            } else if (isOkay != true && isNotConflicting == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("The starting and ending date and / or time may not work with the company hours or business days. Please revise.");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            } else if (isOkay == true && isNotConflicting != true) {
            } else if (isOkay == true && isNotConflicting != true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("The time and / or date has a scheduling conflict with another appointment. Please revise.");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
        }
    }

    // For the edit button 
    public void setupApptDetails(int userId, List<Object> apptVals) {
        List<String> ampm = new ArrayList<>();
        ampm.add("AM");
        ampm.add("PM");
        startAMPM.getItems().setAll(ampm);
        endAMPM.getItems().setAll(ampm);
        cancelCommand = 1;
        saveButton.setVisible(true);
        cancelButton.setText("Cancel");
        this.usID = userId;
        this.appointmentInfo = apptVals;
        this.custID = (int) apptVals.get(1);
        this.userID = (int) apptVals.get(2);
        ZonedDateTime st = (ZonedDateTime) appointmentInfo.get(9);
        String startTime;
        String endTime;

        if (st.toInstant().atZone(ZoneId.systemDefault()).getHour() > 12) {
            st.toInstant().atZone(ZoneId.systemDefault()).minusHours(12);
            startTime = st.toLocalDateTime().format(dtf);
            startAMPM.getSelectionModel().select(1);

        } else {
            startTime = st.toLocalDateTime().format(dtf);
            startAMPM.getSelectionModel().select(0);
        }

        ZonedDateTime et = (ZonedDateTime) appointmentInfo.get(10);
        if (et.toInstant().atZone(ZoneId.systemDefault()).getHour() > 12) {
            et.toInstant().atZone(ZoneId.systemDefault()).minusHours(12);
            endTime = et.toLocalDateTime().format(dtf);
            endAMPM.getSelectionModel().select(1);

        } else {
            endTime = et.toLocalDateTime().format(dtf);
            endAMPM.getSelectionModel().select(0);
        }
        Timestamp cd = (Timestamp) appointmentInfo.get(11);
        String createDate = cd.toLocalDateTime().format(dtf);
        Timestamp lu = (Timestamp) appointmentInfo.get(13);
        String lastUpdate = lu.toLocalDateTime().format(dtf);
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
        customerInfo = DBCommands.DBRequest.getCustomerInfoByID(custID);
        /*
        0 = customerId
        1 = customerName
        2 = addressId
        3 = active
        4 = createDate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
         */
        customerName = (String) customerInfo.get(1);
        usInfo = DBCommands.DBRequest.getUserInfoByID(userId);
        userInfo = DBCommands.DBRequest.getUserInfoByID(userID);
        /*
        0 = userId
        1 = userName
        2 = password
        3 = active
        4 = createDate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
         */
        userName = (String) userInfo.get(1);
        String currentUserName = (String) usInfo.get(1);
        // Set up the empty text fields
        titleTF.setEditable(true);
        titleTF.setText((String) appointmentInfo.get(3));
        descriptionTF.setEditable(true);
        descriptionTF.setText((String) appointmentInfo.get(4));
        locationTF.setEditable(true);
        locationTF.setText((String) appointmentInfo.get(5));
        contactTF.setEditable(true);
        contactTF.setText((String) appointmentInfo.get(6));
        typeTF.setEditable(true);
        typeTF.setText((String) appointmentInfo.get(7));
        urlTF.setEditable(true);
        urlTF.setText((String) appointmentInfo.get(8));
        startTF.setText(startTime);
        endTF.setText(endTime);
        createDateTF.setText(createDate);
        createByTF.setText((String) appointmentInfo.get(12));
        lastUpTF.setText(LocalDateTime.now().format(dtf));
        lastUpByTF.setText(currentUserName);
        // Set up the combo boxes
        List<String> customerNames = new ArrayList<>();
        for (int i = 0; i < listOfCustomers.size(); i++) {
            customerNames.add((String) listOfCustomers.get(i).get(1));
        }
        custNameCombo.getItems().setAll(customerNames);
        custNameCombo.setValue(customerName);
        List<String> usersNames = new ArrayList<>();
        for (int i = 0; i < listOfUsers.size(); i++) {
            usersNames.add((String) listOfUsers.get(i).get(1));
        }
        userNameCombo.getItems().setAll(usersNames);
        userNameCombo.setValue(userName);

    }

    // For the details initial setup
    public void setupApptDetails(List<Object> apptVals) {

        List<String> ampm = new ArrayList<>();
        ampm.add("AM");
        ampm.add("PM");
        startAMPM.getItems().setAll(ampm);
        endAMPM.getItems().setAll(ampm);

        String startTime = null;
        String endTime = null;
        cancelCommand = 0;
        saveButton.setVisible(false);
        cancelButton.setText("Close");
        this.usID = userID;
        this.appointmentInfo = apptVals;
        this.custID = (int) apptVals.get(1);
        this.userID = (int) apptVals.get(2);
        ZonedDateTime st = (ZonedDateTime) appointmentInfo.get(9);
        if (st.toInstant().atZone(ZoneId.systemDefault()).getHour() > 12) {
            st.toInstant().atZone(ZoneId.systemDefault()).minusHours(12);
            startTime = st.toLocalDateTime().format(dtf);
            startAMPM.getSelectionModel().select(1);

        } else {
            startTime = st.toLocalDateTime().format(dtf);
            startAMPM.getSelectionModel().select(0);
        }

        ZonedDateTime et = (ZonedDateTime) appointmentInfo.get(10);
        if (et.toInstant().atZone(ZoneId.systemDefault()).getHour() > 12) {
            et.toInstant().atZone(ZoneId.systemDefault()).minusHours(12);
            endTime = et.toLocalDateTime().format(dtf);
            endAMPM.getSelectionModel().select(1);

        } else {
            endTime = et.toLocalDateTime().format(dtf);
            endAMPM.getSelectionModel().select(0);
        }

        Timestamp cd = (Timestamp) appointmentInfo.get(11);
        String createDate = cd.toLocalDateTime().format(dtf);
        Timestamp lu = (Timestamp) appointmentInfo.get(13);
        String lastUpdate = lu.toLocalDateTime().format(dtf);
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
        customerInfo = DBCommands.DBRequest.getCustomerInfoByID(custID);
        /*
        0 = customerId
        1 = customerName
        2 = addressId
        3 = active
        4 = createDate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
         */
        customerName = (String) customerInfo.get(1);

        userInfo = DBCommands.DBRequest.getUserInfoByID(userID);
        /*
        0 = userId
        1 = userName
        2 = password
        3 = active
        4 = createDate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
         */
        userName = (String) userInfo.get(1);

        // Set up the empty text fields
        titleTF.setText((String) appointmentInfo.get(3));
        titleTF.setEditable(false);
        descriptionTF.setText((String) appointmentInfo.get(4));
        descriptionTF.setEditable(false);
        locationTF.setText((String) appointmentInfo.get(5));
        locationTF.setEditable(false);
        contactTF.setText((String) appointmentInfo.get(6));
        contactTF.setEditable(false);
        typeTF.setText((String) appointmentInfo.get(7));
        typeTF.setEditable(false);
        urlTF.setText((String) appointmentInfo.get(8));
        urlTF.setEditable(false);
        startTF.setText(startTime);
        startTF.setEditable(false);
        endTF.setText(endTime);
        endTF.setEditable(false);
        createDateTF.setText(createDate);
        createDateTF.setEditable(false);
        createByTF.setText((String) appointmentInfo.get(12));
        createByTF.setEditable(false);
        lastUpTF.setText(lastUpdate);
        lastUpTF.setEditable(false);
        lastUpByTF.setText((String) appointmentInfo.get(14));
        lastUpByTF.setEditable(false);
        custNameCombo.getItems().setAll(customerName);
        custNameCombo.setValue(customerName);
        userNameCombo.getItems().setAll(userName);
        userNameCombo.setValue(userName);

    }

    // For the create new Button
    public void setupApptDetails(int userId) {
        List<String> ampm = new ArrayList<>();
        ampm.add("AM");
        ampm.add("PM");
        startAMPM.getItems().setAll(ampm);
        endAMPM.getItems().setAll(ampm);
        ZonedDateTime rightNow = ZonedDateTime.now();
        String start;
        if (rightNow.getHour() > 12) {
            start = rightNow.format(dtf);
            startAMPM.setValue("PM");
        } else {
            start = rightNow.format(dtf);
            startAMPM.setValue("AM");
        }

        this.usID = userId;
        userInfo = DBCommands.DBRequest.getUserInfoByID(usID);
        /*
        0 = userId
        1 = userName
        2 = password
        3 = active
        4 = createDate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
         */
        userName = (String) userInfo.get(1);
        cancelCommand = 2;
        titleTF.setEditable(true);
        descriptionTF.setEditable(true);
        locationTF.setEditable(true);
        contactTF.setEditable(true);
        typeTF.setEditable(true);
        urlTF.setEditable(true);
        startTF.setEditable(true);
        startTF.setText(start);
        endTF.setEditable(true);
        createDateTF.setText(LocalDateTime.now().format(submitdtf));
        createByTF.setText(userName);
        lastUpTF.setText(LocalDateTime.now().format(submitdtf));
        lastUpByTF.setText(userName);
        List<String> customerNames = new ArrayList<>();
        for (int i = 0; i < listOfCustomers.size(); i++) {
            customerNames.add((String) listOfCustomers.get(i).get(1));
        }
        custNameCombo.getItems().setAll(customerNames);
        List<String> usersNames = new ArrayList<>();
        for (int i = 0; i < listOfUsers.size(); i++) {
            usersNames.add((String) listOfUsers.get(i).get(1));
        }
        userNameCombo.getItems().setAll(usersNames);
    }

    // method for autmoatically adding the date and time in the end box
    public void autoInsertDate() {
        if (autoSet == false) {
            endTF.clear();
            String startText = startTF.getText();
            endTF.setText(startText);
            this.autoSet = true;
        }
    }

}
