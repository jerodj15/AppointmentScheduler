/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SceneCollections;

import ObjectCreation.Appointment;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jerod
 */
public class CalendarViewUIController{

    @FXML
    private TableView calendarTableView;
    @FXML
    private ComboBox monthCombo;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private Button weekBackButton;
    @FXML
    private Button weekForwardButton;
    @FXML
    private Button closeButton;
    @FXML 
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Button goButton;
    
    private int windowMode;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm");
    DateTimeFormatter displayDTF = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    String currentDateTime = ZonedDateTime.now().format(dtf);
    ZonedDateTime today = ZonedDateTime.now();
    ZonedDateTime sunday;
    ZonedDateTime saturday;
    ZonedDateTime monthBegin;
    ZonedDateTime monthEnd;
    List<Month> monthList = new ArrayList<>();
    List<Integer> yearList = new ArrayList<>();
    // Setup the calendar view based on the mode
     // Mode 0 = weeklyview
        // Mode 1 = monthlyView
    public void setupCalendarViewUI(int mode)
    {
       
        // Setup the months list
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
        
        
         // Mode 0 = weeklyview
        // Mode 1 = monthlyView
        this.windowMode = mode;
        if (windowMode == 0)
        {
         List<List> weeksApptList;
         List<Appointment> smallAppointments = new ArrayList<>();
         sunday = today;
         saturday = today;
         yearCombo.setVisible(false);
         monthCombo.setVisible(false);
         goButton.setVisible(false);
         while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
         {
             sunday = sunday.minusDays(1);
         }
         while (saturday.getDayOfWeek() != DayOfWeek.SATURDAY)
         {
             saturday = saturday.plusDays(1);
         }
        ZonedDateTime thisSunday = sunday.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        startLabel.setText(thisSunday.format(displayDTF));
        ZonedDateTime thisSaturday = saturday.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime subSaturday = saturday.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        endLabel.setText(thisSaturday.format(displayDTF));
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

        calendarTableView.getColumns().add(startColumn);
        calendarTableView.getColumns().add(endColumn);
        calendarTableView.getColumns().add(titleColumn);
        calendarTableView.getColumns().add(customerColumn);
        calendarTableView.getColumns().add(userColumn);
        weeksApptList = DBCommands.DBRequest.getDatesAppointments(thisSunday, subSaturday);
            System.out.println(weeksApptList);
        // Populate the list of lists for the Table
        for (int i = 0; i < weeksApptList.size(); i++)
        {
            
            List<Object> apptInfo = FXCollections.observableArrayList();
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(10));
            apptInfo.add(weeksApptList.get(i).get(3));
            apptInfo.add(weeksApptList.get(i).get(1));
            apptInfo.add(weeksApptList.get(i).get(2));
            apptInfo.add(weeksApptList.get(i).get(0));
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(8));
           // System.out.println(apptInfo);
            
            Appointment newAppointment = new Appointment(apptInfo);
            smallAppointments.add(newAppointment);
           // calendarTableView.getItems().add(newAppointment);
        }
        calendarTableView.getItems().setAll(smallAppointments);
            
        }
        // Month View
        if (windowMode == 1)
        {
            
            List<List> weeksApptList;
            List<Appointment> monthsAppointments = new ArrayList<>();
            weekBackButton.setVisible(false);
            weekForwardButton.setVisible(false);
            
            monthBegin = today;
            monthEnd = today;
            
            
            while (monthBegin.getDayOfMonth() != 1)
            {
               monthBegin = monthBegin.minusDays(1);
            }
            while (monthEnd.getDayOfMonth() != monthEnd.getMonth().length(true))
            {
                monthEnd = monthEnd.plusDays(1);
            }
            ZonedDateTime monthBeginDate = monthBegin.toLocalDate().atStartOfDay(ZoneId.systemDefault());
            System.out.println(monthBeginDate);
            ZonedDateTime monthEndDate = monthEnd.toLocalDate().atTime(23, 59).atZone(ZoneId.systemDefault());
            System.out.println(monthEndDate);
            int currentYear = monthBegin.getYear();
            Month currentMonth = monthBegin.getMonth();
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

        calendarTableView.getColumns().add(startColumn);
        calendarTableView.getColumns().add(endColumn);
        calendarTableView.getColumns().add(titleColumn);
        calendarTableView.getColumns().add(customerColumn);
        calendarTableView.getColumns().add(userColumn);
        weeksApptList = DBCommands.DBRequest.getDatesAppointments(monthBeginDate, monthEndDate);
        // Populate the list of lists for the Table
            System.out.println(weeksApptList.size());
        for (int i = 0; i < weeksApptList.size(); i++)
        {
            
            List<Object> apptInfo = new ArrayList<>();
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(10));
            apptInfo.add(weeksApptList.get(i).get(3));
            apptInfo.add(weeksApptList.get(i).get(1));
            apptInfo.add(weeksApptList.get(i).get(2));
            apptInfo.add(weeksApptList.get(i).get(0));
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(8));
            System.out.println(apptInfo);
            
            Appointment newAppointment = new Appointment(apptInfo);
            System.out.println(newAppointment);
            monthsAppointments.add(newAppointment);
        }
        calendarTableView.getItems().setAll(monthsAppointments);
        yearCombo.getItems().setAll(yearList);
        yearCombo.setValue(currentYear);
        monthCombo.getItems().setAll(monthList);
        monthCombo.setValue(currentMonth);
        goButtonPressed();
        }

    } 
    // Method for week back pressed
    public void weekBackPressed()
    {
        List<List> weeksApptList;
        List<Appointment> smallAppointments = new ArrayList<>();
        sunday = sunday.minusWeeks(1);
        saturday = saturday.minusWeeks(1);
        ZonedDateTime thisSunday = sunday.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        //String thisSunday = sunday.toLocalDate().format(displayDTF);
        String thisSaturday = saturday.toLocalDate().format(displayDTF);
        ZonedDateTime subSaturday = saturday.toLocalDateTime().withHour(22).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        
        startLabel.setText(thisSunday.format(displayDTF));
        endLabel.setText(thisSaturday);
        calendarTableView.getItems().removeAll();
        calendarTableView.getItems().clear();
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

        calendarTableView.getColumns().set(0, startColumn);
        calendarTableView.getColumns().set(1, endColumn);
        calendarTableView.getColumns().set(2, titleColumn);
        calendarTableView.getColumns().set(3, customerColumn);
        calendarTableView.getColumns().set(4, userColumn);
        weeksApptList = DBCommands.DBRequest.getDatesAppointments(thisSunday, subSaturday);
            System.out.println(weeksApptList);
        // Populate the list of lists for the Table
        for (int i = 0; i < weeksApptList.size(); i++)
        {
            
            List<Object> apptInfo = FXCollections.observableArrayList();
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(10));
            apptInfo.add(weeksApptList.get(i).get(3));
            apptInfo.add(weeksApptList.get(i).get(1));
            apptInfo.add(weeksApptList.get(i).get(2));
            apptInfo.add(weeksApptList.get(i).get(0));
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(8));
            System.out.println(apptInfo);
            
            Appointment newAppointment = new Appointment(apptInfo);
            //calendarTableView.getItems().add(newAppointment);
            smallAppointments.add(newAppointment);
        }
        calendarTableView.getItems().setAll(smallAppointments);
    }
    // Method for week forward pressed
    public void weekForwardPressed()
    {
        List<List> weeksApptList;
        List<Appointment> smallAppointments = new ArrayList<>();
        sunday = sunday.plusWeeks(1);
        saturday = saturday.plusWeeks(1);
        ZonedDateTime thisSunday = sunday.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        //String thisSunday = sunday.toLocalDate().format(displayDTF);
        String thisSaturday = saturday.toLocalDate().format(displayDTF);
        ZonedDateTime subSaturday = saturday.toLocalDateTime().withHour(22).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        startLabel.setText(thisSunday.format(displayDTF));
        endLabel.setText(thisSaturday);
        calendarTableView.getItems().removeAll();
        calendarTableView.getItems().clear();
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

        calendarTableView.getColumns().set(0, startColumn);
        calendarTableView.getColumns().set(1, endColumn);
        calendarTableView.getColumns().set(2, titleColumn);
        calendarTableView.getColumns().set(3, customerColumn);
        calendarTableView.getColumns().set(4, userColumn);
        weeksApptList = DBCommands.DBRequest.getDatesAppointments(thisSunday, subSaturday);
            System.out.println(weeksApptList);
        // Populate the list of lists for the Table
        for (int i = 0; i < weeksApptList.size(); i++)
        {
            
            List<Object> apptInfo = FXCollections.observableArrayList();
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(10));
            apptInfo.add(weeksApptList.get(i).get(3));
            apptInfo.add(weeksApptList.get(i).get(1));
            apptInfo.add(weeksApptList.get(i).get(2));
            apptInfo.add(weeksApptList.get(i).get(0));
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(8));
            System.out.println(apptInfo);
            
            Appointment newAppointment = new Appointment(apptInfo);
           // calendarTableView.getItems().add(newAppointment);
            smallAppointments.add(newAppointment);
        }
        calendarTableView.getItems().setAll(smallAppointments);
    }
    // Method for the go button pressed
    public void goButtonPressed()
    {
        List<List> weeksApptList;
        int yearSel = yearCombo.getSelectionModel().getSelectedIndex();
        int year = yearList.get(yearSel);
        int monthSel = monthCombo.getSelectionModel().getSelectedIndex();
        Month month = monthList.get(monthSel);
        System.out.println(year);
        System.out.println(month);
        LocalDateTime monthToDate = LocalDateTime.of(year, month, 01, 01, 01);
        LocalDateTime lastMonthtoDate = monthToDate.plusDays(month.length(true));
        ZonedDateTime selectedMonthStart = monthToDate.atZone(ZoneId.systemDefault());
        ZonedDateTime selectedMonthEnd = lastMonthtoDate.atZone(ZoneId.systemDefault());
        System.out.println(selectedMonthStart);
        System.out.println(selectedMonthEnd);
        List<Appointment> smallAppointments = new ArrayList<>();

        calendarTableView.getItems().removeAll();
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

        calendarTableView.getColumns().set(0, startColumn);
        calendarTableView.getColumns().set(1, endColumn);
        calendarTableView.getColumns().set(2, titleColumn);
        calendarTableView.getColumns().set(3, customerColumn);
        calendarTableView.getColumns().set(4, userColumn);
        weeksApptList = DBCommands.DBRequest.getDatesAppointments(selectedMonthStart, selectedMonthEnd);
        System.out.println(selectedMonthStart + "          " + selectedMonthEnd);
           System.out.println(weeksApptList);
         //Populate the list of lists for the Table
        for (int i = 0; i < weeksApptList.size(); i++)
        {
            
            List<Object> apptInfo = FXCollections.observableArrayList();
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(10));
            apptInfo.add(weeksApptList.get(i).get(3));
            apptInfo.add(weeksApptList.get(i).get(1));
            apptInfo.add(weeksApptList.get(i).get(2));
            apptInfo.add(weeksApptList.get(i).get(0));
            apptInfo.add(weeksApptList.get(i).get(9));
            apptInfo.add(weeksApptList.get(i).get(8));
            System.out.println(apptInfo);
            
            Appointment newAppointment = new Appointment(apptInfo);
            calendarTableView.getItems().add(newAppointment);
            smallAppointments.add(newAppointment);
        }
        calendarTableView.getItems().setAll(smallAppointments);
        
    }
    // Method for the close button
    public void closeButtonPressed()
    {
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        thisStage.close();
    }
    }

