/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SceneCollections;

import MainClass.MainScheduler;
import ObjectCreation.Address;
import ObjectCreation.Customer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jerod
 */
public class CustomersUIController{

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
    private TableView customerTableView;

   // Variables needed to set up the table
    private int usID;
    String usName;
    
    List<List> addressQueryList = DBCommands.DBRequest.getAllAddresses();
    List<Customer> customersList = new ArrayList<>();
    List<Address> addressList = new ArrayList<>();
    List<Object> fullCustomer;
    
    // Initial setup of the customer window
    public void setupCustomerUI(int userID, String userName)
    {
        this.customersList.clear();
        List<List> customersQueryList = DBCommands.DBRequest.getAllCustomers();
        this.usID = userID;
        this.usName = userName;
        TableColumn<Customer, String> nameColumn = new TableColumn<>("Customer Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        nameColumn.setMinWidth(830);
        nameColumn.setStyle( "-fx-alignment: CENTER");
        customerTableView.getColumns().add(nameColumn);
        
        for (int i = 0; i < customersQueryList.size(); i++)
        {
            Customer newCustomer = new Customer(customersQueryList.get(i));
            customersList.add(newCustomer);
            customerTableView.getItems().add(newCustomer);
        }
        

    }
    // Setting up a details window to view the customer information
    public void detailsButtonPressed()
    {
        try {

            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CustomerDetailsUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        CustomerDetailsUIController controller = loader.getController();
        int selectedIndex = customerTableView.getSelectionModel().getSelectedIndex();
        int custoId = customersList.get(selectedIndex).getCustomerId();
        int addId = customersList.get(selectedIndex).getAddressId();
        fullCustomer = DBCommands.DBCompare.getCustomerDetails(addId, custoId);
        controller.setupDetailsUI(fullCustomer);
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setScene(detailsScene);
        detailsStage.show();
        System.out.println(fullCustomer);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        
    }
    // Setting up the window to edit a customer
    public void editButtonPressed()
    {
        try {
            Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CustomerDetailsUI.fxml"));
        Pane detailsPane = (Pane) loader.load();
        CustomerDetailsUIController controller = loader.getController();
        int selectedIndex = customerTableView.getSelectionModel().getSelectedIndex();
        int custoId = customersList.get(selectedIndex).getCustomerId();
        int addId = customersList.get(selectedIndex).getAddressId();
        fullCustomer = DBCommands.DBCompare.getCustomerDetails(addId, custoId);
        controller.setupDetailsUI(usName , fullCustomer);
        Scene detailsScene = new Scene(detailsPane);
        detailsStage.setScene(detailsScene);
        detailsStage.show();
        System.out.println(fullCustomer);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
    }
    // Method for setting up the create new customer window
    public void createNewButtonPressed()
    {
        try {
            Stage createNewStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainScheduler.class.getResource("/SceneCollections/CreateNewCustomerUI.fxml"));
            Pane newCustomerPane = (Pane) loader.load();
            CreateNewCustomerUIController createNewCustomerUIController = loader.getController();
            createNewCustomerUIController.setupCreateNewUI(usID, usName);
            Scene createNewScene = new Scene(newCustomerPane);
            createNewStage.setScene(createNewScene);
            createNewStage.show();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    // Refresh the table view of customers
    public void refreshButtonPressed()
    {
        this.customersList.clear();
        List<List> customersQueryList = DBCommands.DBRequest.getAllCustomers();
        customerTableView.getItems().clear();
        // Setting up the column
        TableColumn<Customer, String> nameColumn = new TableColumn<>("Customer Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        nameColumn.setMinWidth(830);
        nameColumn.setStyle( "-fx-alignment: CENTER");
        customerTableView.getColumns().set(0, nameColumn);
        
        for (int i = 0; i < customersQueryList.size(); i++)
        {
            Customer newCustomer = new Customer(customersQueryList.get(i));
            customersList.add(newCustomer);
        }
        customerTableView.getItems().setAll(customersList);
        
    }
    // Permanately delete a customer from the database and table
    public void deleteButtonPressed()
    {
        int selectedIndex = customerTableView.getSelectionModel().getSelectedIndex();
        int custoId = customersList.get(selectedIndex).getCustomerId();
        int addId = customersList.get(selectedIndex).getAddressId();
        DBCommands.DBRemove.removeCustomer(custoId, addId);
        refreshButtonPressed();
    }
    
}
