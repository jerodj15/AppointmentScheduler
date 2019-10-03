package SceneCollections;

import ObjectCreation.Appointment;
import static SceneCollections.MainUIController.allAppointments;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;




public class ReportsUIController{

@FXML
private Tab myScheduleTab;
@FXML
private Tab numApptTypesMonth;
@FXML
private Tab avgApptTimesConsultant;
@FXML
private PieChart numApptsPie;
@FXML
private BarChart avgApptTimesBarChart;
@FXML
private Button resultsButton;
@FXML
private ComboBox yearCombo;
@FXML
private ComboBox monthCombo;
@FXML
private ComboBox yearComboAVG;
@FXML
private ComboBox monthComboAVG;
@FXML
private TableView scheduleTable;
@FXML
private ListView typeListView;

//Declare variables for storing the users information
List<Object> userInformation;
List<List> allUsers = DBCommands.DBRequest.getAllUsers();
ObservableList<List> userAppts;
ObservableList<Appointment> userAppointments;
static ObservableList<Appointment> briefAppointments = FXCollections.observableArrayList();
List<Month> monthList = new ArrayList<>();
List<Integer> yearList = new ArrayList<>();
ZonedDateTime today = ZonedDateTime.now();

// Initial setup of the reports window
public void setupReportsUI(List<Object> userInfo, ObservableList<Appointment> userAppts)
{
    briefAppointments.clear();
    scheduleTable.getItems().clear();
    // Setup the cosultants schedule chart
    this.userInformation = userInfo;
    this.userAppointments = userAppts;
    TableColumn<Appointment, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));

        TableColumn<Appointment, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        
        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Appointment, Integer> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        scheduleTable.getColumns().clear();
        scheduleTable.getColumns().add(startColumn);
        scheduleTable.getColumns().add(endColumn);
        scheduleTable.getColumns().add(titleColumn);
        scheduleTable.getColumns().add(customerColumn);
        
        // Populate the list of lists for the Table
        for (int i = 0; i < allAppointments.size(); i++)
        {
            ZonedDateTime sunday = today;
            ZonedDateTime saturday = today;
            // Adjust the days to make them the beginning and end of the current week
            while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
            {  
             sunday = sunday.minusDays(1);
            }
            while (saturday.getDayOfWeek() != DayOfWeek.SATURDAY)
            {
             saturday = saturday.plusDays(1);
            }
            int sundayDay = sunday.getDayOfYear();
            int saturdayDay = saturday.getDayOfYear();
            ZonedDateTime ts = (ZonedDateTime) allAppointments.get(i).get(9);
            ZonedDateTime zonedStart = ZonedDateTime.of(ts.toLocalDateTime(), ZoneId.systemDefault());
            int todayDay = ZonedDateTime.now().getDayOfYear();
            List<Object> apptInfo = FXCollections.observableArrayList();
            if ((zonedStart.getDayOfYear() <= saturdayDay) && zonedStart.getDayOfYear() >= sundayDay)
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
            scheduleTable.getItems().add(newAppointment);
            }
        }
    // Prepare the information for the number of appointment types by month
    setupPieChartTab();
    // Setup average times tab
    setupAverageTimesTab();
    
}
// Initial setup of the pieChartTab
public void setupPieChartTab()
{
    
        monthList.add(Month.JANUARY);
        monthList.add(Month.FEBRUARY);
        monthList.add(Month.MARCH);
        monthList.add(Month.APRIL);
        monthList.add(Month.MAY);
        monthList.add(Month.JUNE);
        monthList.add(Month.JULY);
        monthList.add(Month.AUGUST);
        monthList.add(Month.SEPTEMBER);
        monthList.add(Month.OCTOBER);
        monthList.add(Month.NOVEMBER);
        monthList.add(Month.DECEMBER);
        // Setup the year list
        for (int i = 1995; i < 2025; i++)
        {
            yearList.add(i);
        }
        yearCombo.getItems().setAll(yearList);
        monthCombo.getItems().setAll(monthList);
        yearCombo.setValue(2019);
        
        
}
// Setting up the average times tab
public void setupAverageTimesTab()
{
    yearComboAVG.getItems().setAll(yearList);
    monthComboAVG.getItems().setAll(monthList);
    yearComboAVG.setValue(2019);
}

// Method called when the go button is pressed
public void goButtonPressed()
{
    List<XYChart.Series> eachUserSeries = new ArrayList<>();
    List<Integer> userIDs = new ArrayList<>();
    for (int i = 0; i < allUsers.size(); i++)
    {
        userIDs.add((Integer) allUsers.get(i).get(0));
    }
    List<Double> userAverages = new ArrayList<>();
    int yearSel = yearComboAVG.getSelectionModel().getSelectedIndex();
        int year = yearList.get(yearSel);
        int monthSel = monthComboAVG.getSelectionModel().getSelectedIndex();
        Month month = monthList.get(monthSel);
        LocalDateTime monthToDate = LocalDateTime.of(year, month, 01, 01, 01);
        LocalDateTime lastMonthtoDate = monthToDate.plusDays(month.length(true));
        ZonedDateTime selectedMonthStart = monthToDate.atZone(ZoneId.systemDefault());
        ZonedDateTime selectedMonthEnd = lastMonthtoDate.atZone(ZoneId.systemDefault());
   for (int i = 0; i < userIDs.size(); i++)
   {
       int curUser = userIDs.get(i);
       userAverages.add(DBCommands.DBRequest.getAverageMonthlyAppointmentTime(curUser, selectedMonthStart, selectedMonthEnd));
   }
   for (int i = 0; i < userIDs.size(); i++)
   {
       XYChart.Series newSeries = new XYChart.Series<>();
       newSeries.setName(String.valueOf(userIDs.get(i)));
       newSeries.getData().add(new XYChart.Data(String.valueOf(userIDs.get(i)), userAverages.get(i)));
       eachUserSeries.add(newSeries);
   }
     avgApptTimesBarChart.getData().addAll(eachUserSeries);
}


// Method that is called when the results button is pressed in the piechart tab
public void resultsButtonPressed()
{
    typeListView.getItems().clear();
    int yearSel = yearCombo.getSelectionModel().getSelectedIndex();
        int year = yearList.get(yearSel);
        int monthSel = monthCombo.getSelectionModel().getSelectedIndex();
        Month month = monthList.get(monthSel);
        LocalDateTime monthToDate = LocalDateTime.of(year, month, 01, 01, 01);
        LocalDateTime lastMonthtoDate = monthToDate.plusDays(month.length(true));
        ZonedDateTime selectedMonthStart = monthToDate.atZone(ZoneId.systemDefault());
        ZonedDateTime selectedMonthEnd = lastMonthtoDate.atZone(ZoneId.systemDefault());
        ObservableList<PieChart.Data> pieChartData = DBCommands.DBRequest.getAppointmentTypes(selectedMonthStart, selectedMonthEnd);
        for (int i = 0; i < pieChartData.size(); i++)
        {
            String typeName = pieChartData.get(i).getName();
            String typeTot = String.valueOf(pieChartData.get(i).getPieValue()).replace(".0", "");
            typeListView.getItems().add(i, typeName + "       " + typeTot);
        }
        

        if (pieChartData.isEmpty() == true)
        {
            numApptsPie.setData(pieChartData);
            numApptsPie.setTitle("No data to display");
        }
        else
        {
            numApptsPie.setTitle("Number of Appointment Types By Month");
            numApptsPie.setData(pieChartData);

        }        
}
// Method to clear the average appointment time bar chart
public void clearButtonPressed()
{
    avgApptTimesBarChart.getData().clear();
}

public void viewAllAppts()
{
    briefAppointments.clear();
    typeListView.getItems().clear();
     TableColumn<Appointment, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));

        TableColumn<Appointment, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        
        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumn<Appointment, Integer> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        scheduleTable.getColumns().clear();
        scheduleTable.getColumns().add(startColumn);
        scheduleTable.getColumns().add(endColumn);
        scheduleTable.getColumns().add(titleColumn);
        scheduleTable.getColumns().add(customerColumn);
        
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
            //scheduleTable.getItems().add(newAppointment);
            
        }
        scheduleTable.getItems().setAll(briefAppointments);
}

// Reset back to week schedule
public void weekSchedulePressed()
{
    setupReportsUI(userInformation, userAppointments);
}


}
