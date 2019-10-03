package SceneCollections;

import Misc.CheckEntries;
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


public class CreateNewCustomerUIController{

    @FXML
    private TextField CustNameTF;
    @FXML
    private TextField ActiveTF;
    @FXML
    private TextField Address1TF;
    @FXML
    private TextField Address2TF;
    @FXML
    private ComboBox countryCombo;
    @FXML
    private ComboBox cityCombo;
    @FXML
    private TextField PostalCodeTF;
    @FXML
    private TextField PhoneTF;
    @FXML
    private TextField CustCreateTF;
    @FXML
    private TextField CustCreateByTF;
    @FXML
    private TextField CustLastUpTF;
    @FXML
    private TextField CustLastUpByTF;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    
    // Method for the cancel button
    @FXML
    private void cancelButtonPressed(ActionEvent event) {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
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

    // Method for the save button
    @FXML
    private void saveButtonPressed(ActionEvent event) {
        // Collect all the needed input to save the new customer and address information
        String countryName = (String) countryCombo.getValue();
        String cityName = (String) cityCombo.getValue();
        String customerName = CustNameTF.getText();
        Boolean customerActive = Boolean.parseBoolean(ActiveTF.getText());
        int active = customerActive ? 1 : 0;
        String address1 = Address1TF.getText();
        String address2 = Address2TF.getText();
        int countryId = 0;
        int cityId = 0;
        for (int i = 0; i < countryList.size(); i++)
        {
            if (countryName == countryList.get(i).get(1))
            {
                countryId = (int) countryList.get(i).get(0);
            }
        }
        for (int i = 0; i < citiesList.size(); i++)
        {
            if (cityName == citiesList.get(i).get(1))
            {
                cityId = (int) citiesList.get(i).get(0);
            }
        }
        String postalCode = PostalCodeTF.getText();
        String phone = PhoneTF.getText();
        String createDate = ZonedDateTime.now().format(dtf);
        String createdBy = usName;
        // Add all the information to a list
        List<Object> customerToSave = new ArrayList<>();
        customerToSave.add(customerName);
        customerToSave.add(active);
        customerToSave.add(address1);
        customerToSave.add(address2);
        customerToSave.add(countryId);
        customerToSave.add(cityId);
        customerToSave.add(postalCode);
        customerToSave.add(phone);
        customerToSave.add(createDate);
        customerToSave.add(createdBy);
        boolean isCorrect = CheckEntries.checkForEmpty(customerToSave);
        if (isCorrect == false)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please make sure all boxes are filled");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
        }
        else if (isCorrect == true)
        {
            // Call the database to save the customer and address information
            DBCommands.DBSave.saveNewCustomer(customerToSave);
        }
        
    }
    private String usName;
    private int usID;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm");
    List<List> countryList = DBCommands.DBRequest.getAllCountries();
     /*  countryList Information
        0 = countryId
        1 = country
        2 = createDate
        3 = createdBy
        4 = lastUpdate
        5 = lastUpdateBy
        */
    List<List> citiesList = DBCommands.DBRequest.getAllCities();
    /*   citiesList Information
        0 = cityId
        1 = city
        2 = countryId
        3 = createDate
        4 = createdBy
        5 = lastUpdate
        6 = lastUpdateBy
    */
    List<String> countryNames = new ArrayList<>();
    
   // Method for populating the comboboxes
    public void cityPopulate()
    {
        List<String> cityNames = new ArrayList<>();
        cityCombo.getItems().removeAll(cityNames);
        int countryId = 0; 
        System.out.println(countryCombo.getValue().toString());
        for (int i = 0; i < countryList.size(); i++)
        {
            if (countryList.get(i).get(1) == countryCombo.getValue().toString())
            {
                countryId = (int) countryList.get(i).get(0);
            }
        }
        for (int i = 0; i < citiesList.size(); i++)
        {
            int cityCountry = (int) citiesList.get(i).get(2);
            if (cityCountry == countryId)
            {
                cityNames.add((String) citiesList.get(i).get(1));
            }
        }
        cityCombo.getItems().setAll(cityNames);
    }
    
    // Initial setup of the window
    public void setupCreateNewUI(int userID, String userName)
    {
        System.out.println(countryList);
        this.usID = userID;
        this.usName = userName;
        for (int i = 0; i < countryList.size(); i++)
        {
            String cName = (String) countryList.get(i).get(1);
            System.out.println(cName);
            countryNames.add((String) countryList.get(i).get(1));

        }
        countryCombo.getItems().addAll(countryNames);
        CustCreateTF.setText(ZonedDateTime.now().format(dtf));
        CustCreateByTF.setText(usName);
        CustLastUpTF.setText(ZonedDateTime.now().format(dtf));
        CustLastUpByTF.setText(usName);
    }
 
}
