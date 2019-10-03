package SceneCollections;

import MainClass.MainScheduler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;


public class LoginUIController{

    @FXML
    private PasswordField passField;
    @FXML
    private TextField uNameTF;
    @FXML
    private Label userLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Label welcomeLabel;

    // Variable to store, setup, and format
    private MainScheduler myApptScheduler;
    Locale myLocale = Locale.getDefault();
    ResourceBundle rb = ResourceBundle.getBundle("LoginUI", myLocale);
    DateTimeFormatter submitdtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm");
    
    
    // Initial setup of the login window
    public void setupLoginUI(MainScheduler myApptScheduler)
    {
        
        this.myApptScheduler = myApptScheduler;
        userLabel.setText(rb.getString("userName"));
        passwordLabel.setText(rb.getString("passWord"));
        submitButton.setText(rb.getString("submit"));
        welcomeLabel.setText(rb.getString("welcome"));
        
    } 
    // Method for submitting the information to be checked 
    public void submitButtonPressed()
    {
        String userName = uNameTF.getText();
        String password = passField.getText();
        String errorCode = "";
        
        try {
             List<Object> posMatch = DBCommands.DBRequest.getUserInfoByName(userName);

        if (posMatch.get(1).equals(userName) && posMatch.get(2).equals(password))
        {
            errorCode = "Successful login";
            myApptScheduler.startMain((int) posMatch.get(0), posMatch);
        }
        if (posMatch.get(1).equals(userName) && (posMatch.get(2).equals(password) == false))
        {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(rb.getString("errorPass"));
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
                        
                errorCode = "Incorrect Password";
        }
        } catch (IndexOutOfBoundsException e) {
            if (userName.isEmpty() == true || password.isEmpty() == true)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(rb.getString("errorBlank"));
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
                        
                errorCode = "Blank field(s)";
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(rb.getString("errorUser"));
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            errorCode = "Incorrect userName and / or password";
            }
        }
       
        try {
            String fileLocation = "loginLog2.txt";
            File loginLog = new File(fileLocation);
            if (!loginLog.exists())
            {
                loginLog.createNewFile();
            }
            FileWriter fw = new FileWriter(loginLog.getAbsoluteFile(), true);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.newLine();
                bw.append("Date:  " + ZonedDateTime.now().format(submitdtf));
                bw.append("     ");
                bw.append("User Name:  " + userName);
                bw.append("     ");
                bw.append("Error Code:  " + errorCode);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
