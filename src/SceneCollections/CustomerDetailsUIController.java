
package SceneCollections;

import Misc.CheckEntries;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class CustomerDetailsUIController{

    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField CustNameTF;
    @FXML
    private TextField ActiveTF;
    @FXML
    private TextField Address1TF;
    @FXML
    private TextField Address2TF;
    @FXML
    private TextField CountryTF;
    @FXML
    private TextField CityTF;
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
    
    
    private int usID;
    private int cancelCommand;
    private List<Object> customerVals;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm");

    // For the edit button
    public void setupDetailsUI(String userName, List<Object> userVals)
    {
        this.customerVals = userVals;
        cancelCommand = 1;
        cancelButton.setText("Cancel");
        saveButton.setVisible(true);
        boolean isActive = (boolean) userVals.get(3);
        String activeString = Boolean.toString(isActive);
        Timestamp cd = (Timestamp) userVals.get(4);
        String createDate = cd.toLocalDateTime().format(dtf);

        // combinedCustomerInfo Structure
        /*
        0 = customerId
        1 = customerName
        2 = addressId
        3 = active
        4 = createate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
        8 = address
        9 = address2
        10 = cityId
        11 = postalCode
        12 = phone
        13 = cityName
        14 = countryId
        15 = country
        */
        CustNameTF.setEditable(true);
        ActiveTF.setEditable(true);
        Address1TF.setEditable(true);
        Address2TF.setEditable(true);
        PostalCodeTF.setEditable(true);
        PhoneTF.setEditable(true);
        
        CustNameTF.setText((String) userVals.get(1));
        ActiveTF.setText(activeString);
        Address1TF.setText((String) userVals.get(8));
        Address2TF.setText((String) userVals.get(9));
        CountryTF.setText((String) userVals.get(15));
        CityTF.setText((String) userVals.get(13));
        PostalCodeTF.setText((String) userVals.get(11));
        PhoneTF.setText((String) userVals.get(12));
        CustCreateTF.setText(createDate);
        CustCreateByTF.setText((String) userVals.get(5));
        CustLastUpTF.setText(ZonedDateTime.now().format(dtf));
        CustLastUpByTF.setText(userName);
        
    }
    // For the details button
    public void setupDetailsUI(List<Object> userVals)
    {
        this.customerVals = userVals;
        cancelCommand = 2;
        cancelButton.setText("Close");
        saveButton.setVisible(false);
        boolean isActive = (boolean) userVals.get(3);
        String activeString = Boolean.toString(isActive);
        Timestamp cd = (Timestamp) userVals.get(4);
        String createDate = cd.toInstant().atZone(ZoneId.systemDefault()).format(dtf);
        Timestamp ud = (Timestamp) userVals.get(6);
        String lastUpdate = ud.toInstant().atZone(ZoneId.systemDefault()).format(dtf);
        // combinedCustomerInfo Structure
        /*
        0 = customerId
        1 = customerName
        2 = addressId
        3 = active
        4 = createate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
        8 = address
        9 = address2
        10 = cityId
        11 = postalCode
        12 = phone
        13 = cityName
        14 = countryId
        15 = country
        */
        CustNameTF.setText((String) userVals.get(1));
        ActiveTF.setText(activeString);
        Address1TF.setText((String) userVals.get(8));
        Address2TF.setText((String) userVals.get(9));
        CountryTF.setText((String) userVals.get(15));
        CityTF.setText((String) userVals.get(13));
        PostalCodeTF.setText((String) userVals.get(11));
        PhoneTF.setText((String) userVals.get(12));
        CustCreateTF.setText(createDate);
        CustCreateByTF.setText((String) userVals.get(5));
        CustLastUpTF.setText(lastUpdate);
        CustLastUpByTF.setText((String) userVals.get(7));
        
    }
    
    @FXML
    public void cancelButtonPressed()
    {
        Stage currentStage = (Stage) cancelButton.getScene().getWindow();
        if (cancelCommand == 1)
        {
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
        
        if (cancelCommand == 2)
        {
            currentStage.close();
        }
        
    }
    
    @FXML
    public void saveButtonPressed()
    {
        List<Object> infoToSubmit = new ArrayList<>();
        // Get the input date from the text fields
        String customerName = CustNameTF.getText();
        String isActive = ActiveTF.getText();
        Boolean customerActive = Boolean.parseBoolean(isActive);
        System.out.println(customerActive);
        int custAddId = (int) customerVals.get(2);
        String customerAddress1 = Address1TF.getText();
        String customerAddress2 = Address2TF.getText();
        String customerPostalCode = PostalCodeTF.getText();
        String customerPhone = PhoneTF.getText();
        String lastUpdate = CustLastUpTF.getText();
        String lastUpdateBy = CustLastUpByTF.getText();
        
        // If editing a customer and pressing the save button
        
        /*
        infoToSubmit Structure
        0 = customerId
        1 = customerName
        2 = active
        3 = addressId
        4 = address
        5 = address2
        6 = postalCode
        7 = phone
        8 = lastUpdate (Customer)
        9 = lastUpdateBy (Customer)
        */
        if (cancelCommand == 1)
        {
            infoToSubmit.add(customerVals.get(0));
            infoToSubmit.add(customerName);
            infoToSubmit.add(customerActive);
            infoToSubmit.add(custAddId);
            infoToSubmit.add(customerAddress1);
            infoToSubmit.add(customerAddress2);
            infoToSubmit.add(customerPostalCode);
            infoToSubmit.add(customerPhone);
            infoToSubmit.add(lastUpdate);
            infoToSubmit.add(lastUpdateBy);
            System.out.println(infoToSubmit);
             boolean isCorrect = CheckEntries.checkForEmpty(infoToSubmit);
             System.out.println(isCorrect);
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
            DBCommands.DBSave.saveEditedCustomer(infoToSubmit);
             Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        }
           
            
       
        }
        
        // combinedCustomerInfo Structure
        /*
        0 = customerId
        1 = customerName
        2 = addressId
        3 = active
        4 = createate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
        8 = address
        9 = address2
        10 = cityId
        11 = postalCode
        12 = phone
        13 = cityName
        14 = countryId
        15 = country
        */
    }
    
}
