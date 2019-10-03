/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCommands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

/**
 *
 * @author Jerod
 */
public class DBRequest extends DBConnect{
    

    // Query the database to find out user information by the userName
    public static List<Object> getUserInfoByName(String userName)
    {
        List<Object> userInfo = new ArrayList<>();
        String userInfoString = "select * from user where userName = '" + userName + "';";
        // User Table Information
        /*
        1 = userId
        2 = userName
        3 = password
        4 = active
        5 = createDate
        6 = createdBy
        7 = lastUpdate
        8 = lastUpdateBy
        */
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(userInfoString);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next())
            {
                userInfo.add(rs.getObject(1));
                userInfo.add(rs.getObject(2));
                userInfo.add(rs.getObject(3));
                userInfo.add(rs.getObject(4));
                userInfo.add(rs.getObject(5));
                userInfo.add(rs.getObject(6));
                userInfo.add(rs.getObject(7));
                userInfo.add(rs.getObject(8));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userInfo;
        // List Information
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
    }
    // // Query the database to find out user information by the userId
    public static List<Object> getUserInfoByID(int userID)
    {
        List<Object> userInfo = new ArrayList<>();
        String userInfoString = "select * from user where userId = '" + userID + "';";
        // User Table Information
        /*
        1 = userId
        2 = userName
        3 = password
        4 = active
        5 = createDate
        6 = createdBy
        7 = lastUpdate
        8 = lastUpdateBy
        */
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(userInfoString);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next())
            {
                userInfo.add(rs.getObject(1));
                userInfo.add(rs.getObject(2));
                userInfo.add(rs.getObject(3));
                userInfo.add(rs.getObject(4));
                userInfo.add(rs.getObject(5));
                userInfo.add(rs.getObject(6));
                userInfo.add(rs.getObject(7));
                userInfo.add(rs.getObject(8));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userInfo;
        // List Information
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
    }
    // Retrieve all users and their information in the database
    public static List<List> getAllUsers()
    {
        List<List> allUsers = new ArrayList<>();
        String getUsres = "select * from user";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getUsres);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> userInfo = new ArrayList<>();
                userInfo.add(rs.getObject(1));
                userInfo.add(rs.getObject(2));
                userInfo.add(rs.getObject(3));
                userInfo.add(rs.getObject(4));
                userInfo.add(rs.getObject(5));
                userInfo.add(rs.getObject(6));
                userInfo.add(rs.getObject(7));
                allUsers.add(userInfo);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }
    // Generates the report for the number of appointment types per selected month and year
    public static ObservableList<PieChart.Data> getAppointmentTypes(ZonedDateTime startMonth, ZonedDateTime endMonth)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm");
        int doNext = 0;
        ObservableList<PieChart.Data> dataNeeded = FXCollections.observableArrayList();
        List<String> apptTypesList = new ArrayList<>();
        List<Integer> numApptperType = new ArrayList<>();
        if (doNext == 0)
        {
          String getApptTypes = "select distinct type from appointment where start between '" + startMonth.format(dtf) + "' and '" + endMonth.format(dtf) + "';";  
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(getApptTypes);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                    apptTypesList.add(rs.getString(1));
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            doNext++;
        }
        if (doNext == 1)
        {
            for (int i =0; i < apptTypesList.size(); i++)
            {
                String getNumberofEachType = "select count(type) from appointment where type = '" + apptTypesList.get(i) + "' and start between '" + startMonth.format(dtf) + "' and '" + endMonth.format(dtf) + "';";
                try {
                    PreparedStatement pstmt = myConnection.prepareStatement(getNumberofEachType);
                    ResultSet rs = pstmt.executeQuery();
                    
                    while(rs.next())
                    {
                        numApptperType.add(rs.getInt(1));
                    }
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            doNext++;
        }
        if (doNext == 2)
        {
            for (int i =0; i < apptTypesList.size(); i++)
            {
                String apptType = apptTypesList.get(i);
                int numAppt = numApptperType.get(i);
                PieChart.Data pieData = new PieChart.Data(apptType, numAppt);
                dataNeeded.add(pieData);
            }
            doNext++;
        }
        
        return dataNeeded;
    }
  // Generated a report for the users and their average appointment times for the selected month and year
    public static double getAverageMonthlyAppointmentTime(int userID, ZonedDateTime startMonth, ZonedDateTime endMonth)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm");
        double averageTime = 0;
        List<List> userApptInfo = new ArrayList<>();
        List<Double> startEnd = new ArrayList<>();
        int doNext = 0;
        // Get the appointments for the specific user
        if (doNext == 0)
        {
            String getUserAppointments = "select * from appointment where userId = '" + userID + "' and start between '" + startMonth.format(dtf) + "' and '" + endMonth.format(dtf) + "';";
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(getUserAppointments);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                    List<Object> startAndEnds = new ArrayList<>();
                    startAndEnds.add(rs.getObject(10));
                    startAndEnds.add(rs.getObject(11));
                    userApptInfo.add(startAndEnds);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            doNext++;
        }
        if (doNext == 1)
        {
            double totalTimeTaken = 0;
            for (int i = 0; i < userApptInfo.size(); i++)
            {
                Timestamp start = (Timestamp) userApptInfo.get(i).get(0);
                Timestamp end = (Timestamp) userApptInfo.get(i).get(1);
                totalTimeTaken += (((end.getTime() - start.getTime()) % 3600000)) / 60000;
            }
            averageTime = (totalTimeTaken / userApptInfo.size());
            doNext++;
        }
        return averageTime;
    }
    // Query the database to find appointments based on the userId
    public static ObservableList<List> getAppointmentsByUserId(int userID)
    {
        
        String getAppointments = "select * from appointment where userId = '" + userID + "';";
        // Appointment table information
        /*
        1 = appointmentId
        2 = customerId
        3 = userId
        4 = title
        5 = description
        6 = location
        7 = contact
        8 = type
        9 = url
        10 = start
        11 = end
        12 = createDate
        13 = createdBy
        14 = lastUpdate
        15 = lastUpdateBy
        */
        ObservableList<List> allAppointments = FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getAppointments);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {

                List<Object> singleAppointment = new ArrayList<>();
                singleAppointment.add(rs.getObject(1));
                singleAppointment.add(rs.getObject(2));
                singleAppointment.add(rs.getObject(3));
                singleAppointment.add(rs.getObject(4));
                singleAppointment.add(rs.getObject(5));
                singleAppointment.add(rs.getObject(6));
                singleAppointment.add(rs.getObject(7));
                singleAppointment.add(rs.getObject(8));
                singleAppointment.add(rs.getObject(9));
                singleAppointment.add(rs.getObject(10));
                singleAppointment.add(rs.getObject(11));
                singleAppointment.add(rs.getObject(12));
                singleAppointment.add(rs.getObject(13));
                singleAppointment.add(rs.getObject(14));
                singleAppointment.add(rs.getObject(15));
                allAppointments.add(singleAppointment);
            }
            
            for (int i = 0; i < allAppointments.size(); i++)
        {
            Timestamp tsStart = (Timestamp) allAppointments.get(i).get(9);
            Object start = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            allAppointments.get(i).set(9, start);
            Timestamp tsEnd = (Timestamp) allAppointments.get(i).get(10);
            Object end = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            allAppointments.get(i).set(10, end);
        }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
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
        
        return allAppointments;
    }
    // Query the database to appointmentDates to populate the Calendar section
    public static List<List> getDatesAppointments(ZonedDateTime start, ZonedDateTime end)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm");
        List<List> appointmentsList = new ArrayList<>();
        String getAppointments = "select * from appointment where start between '" + start.format(dtf) + "' and '" + end.format(dtf) + "';";
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getAppointments);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> apptInfo = new ArrayList<>();
                apptInfo.add(rs.getObject(1));
                apptInfo.add(rs.getObject(2));
                apptInfo.add(rs.getObject(3));
                apptInfo.add(rs.getObject(4));
                apptInfo.add(rs.getObject(5));
                apptInfo.add(rs.getObject(6));
                apptInfo.add(rs.getObject(7));
                apptInfo.add(rs.getObject(8));
                apptInfo.add(rs.getObject(9));
                apptInfo.add(rs.getObject(10));
                apptInfo.add(rs.getObject(11));
                apptInfo.add(rs.getObject(12));
                apptInfo.add(rs.getObject(13));
                apptInfo.add(rs.getObject(14));
                apptInfo.add(rs.getObject(15));
                appointmentsList.add(apptInfo);
                System.out.println(apptInfo);
            }
            for (int i = 0; i < appointmentsList.size(); i++)
        {
            Timestamp tsStart = (Timestamp) appointmentsList.get(i).get(9);
            Object startZ = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            appointmentsList.get(i).set(9, startZ);
            Timestamp tsEnd = (Timestamp) appointmentsList.get(i).get(10);
            Object endZ = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            appointmentsList.get(i).set(10, endZ);
        }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return appointmentsList;
    }
    
   
    // Query the database to retrieve all appointments and their information
    public static List<List> getAllAppointments()
    {
        List<List> allAppointments = new ArrayList<>();
        
        String getAppointments = "select * from appointment";
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getAppointments);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> singleAppointment = new ArrayList<>();
                singleAppointment.add(rs.getObject(1));
                singleAppointment.add(rs.getObject(2));
                singleAppointment.add(rs.getObject(3));
                singleAppointment.add(rs.getObject(4));
                singleAppointment.add(rs.getObject(5));
                singleAppointment.add(rs.getObject(6));
                singleAppointment.add(rs.getObject(7));
                singleAppointment.add(rs.getObject(8));
                singleAppointment.add(rs.getObject(9));
                singleAppointment.add(rs.getObject(10));
                singleAppointment.add(rs.getObject(11));
                singleAppointment.add(rs.getObject(12));
                singleAppointment.add(rs.getObject(13));
                singleAppointment.add(rs.getObject(14));
                singleAppointment.add(rs.getObject(15));
                allAppointments.add(singleAppointment);
            }
            for (int i = 0; i < allAppointments.size(); i++)
        {
            Timestamp tsStart = (Timestamp) allAppointments.get(i).get(9);
            Object startZ = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            allAppointments.get(i).set(9, startZ);
            Timestamp tsEnd = (Timestamp) allAppointments.get(i).get(10);
            Object endZ = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            allAppointments.get(i).set(10, endZ);
        }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return allAppointments;
    }
    
    // Query the database to find appointments by their appointmentId
    public static List<Object> getAppointmentByApptId(int apptID)
    {
        List<Object> apptInfo = new ArrayList<>();
        String getApptInfo = "select * from appointment where appointmentId = '" + apptID + "';";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getApptInfo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next())
            {
                apptInfo.add(rs.getObject(1));
                apptInfo.add(rs.getObject(2));
                apptInfo.add(rs.getObject(3));
                apptInfo.add(rs.getObject(4));
                apptInfo.add(rs.getObject(5));
                apptInfo.add(rs.getObject(6));
                apptInfo.add(rs.getObject(7));
                apptInfo.add(rs.getObject(8));
                apptInfo.add(rs.getObject(9));
                apptInfo.add(rs.getObject(10));
                apptInfo.add(rs.getObject(11));
                apptInfo.add(rs.getObject(12));
                apptInfo.add(rs.getObject(13));
                apptInfo.add(rs.getObject(14));
                apptInfo.add(rs.getObject(15));
            }
            
            
            Timestamp tsStart = (Timestamp) apptInfo.get(9);
            Object startZ = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            apptInfo.set(9, startZ);
            Timestamp tsEnd = (Timestamp) apptInfo.get(10);
            Object endZ = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            apptInfo.set(10, endZ);
        
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apptInfo;
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
    }
    
    /*
    public static List<Timestamp> getAllAppointmentDateTimes(int startOrEnd)
    {
        // Start = 0 End = 1
        List<Timestamp> apptTimes = new ArrayList<>();
        String getApptTimes;
        if (startOrEnd == 0)
        {
           getApptTimes = "select start from appointment";
           try {
            PreparedStatement pstmt = myConnection.prepareStatement(getApptTimes);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                apptTimes.add(rs.getTimestamp(1));
            }
            
            Timestamp tsStart = (Timestamp) apptTimes.get(0);
            Object startZ = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-5"));
            apptTimes.set(0, startZ);
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        else
        {
            getApptTimes = "select end from appointment";
            try {
            PreparedStatement pstmt = myConnection.prepareStatement(getApptTimes);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                apptTimes.add(rs.getTimestamp(1));
            }
            Timestamp tsEnd = (Timestamp) apptInfo.get(10);
            Object endZ = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-5"));
            apptInfo.set(10, endZ);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        return apptTimes;
    }
    */
    // Query the database to find out customer information by their customerId
    public static List<Object> getCustomerInfoByID(int custID)
    {
        List<Object> customerInfo = new ArrayList<>();
        String getInfo = "select * from customer where customerId = '" + custID + "';";
        // Customer Table Information
        /*
        1 = customerId
        2 = customerName
        3 = addressId
        4 = active
        5 = createDate
        6 = createdBy
        7 = lastUpdate
        8 = lastUpdateBy
        */
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getInfo);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                
                customerInfo.add(rs.getObject(1));
                customerInfo.add(rs.getObject(2));
                customerInfo.add(rs.getObject(3));
                customerInfo.add(rs.getObject(4));
                customerInfo.add(rs.getObject(5));
                customerInfo.add(rs.getObject(6));
                customerInfo.add(rs.getObject(7));
                customerInfo.add(rs.getObject(8));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        return customerInfo;
    }
    // Query the database to retrieve all customers and their information
    public static List<List> getAllCustomers()
    {
        // Customer Table Information
        /*
        1 = customerId
        2 = customerName
        3 = addressId
        4 = active
        5 = createDate
        6 = createdBy
        7 = lastUpdate
        8 = lastUpdateBy
        */
        List<List> customersList = new ArrayList<>();
        String getCustomers = "select * from customer";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getCustomers);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> customerInfo = new ArrayList<>();
                customerInfo.add(rs.getObject(1));
                customerInfo.add(rs.getObject(2));
                customerInfo.add(rs.getObject(3));
                customerInfo.add(rs.getObject(4));
                customerInfo.add(rs.getObject(5));
                customerInfo.add(rs.getObject(6));
                customerInfo.add(rs.getObject(7));
                customerInfo.add(rs.getObject(8));
                customersList.add(customerInfo);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return customersList;
    }
    // Query the database to retrieve all addresses
    public static List<List> getAllAddresses()
    {
        // Address Table Information
        /*
        1 = addressId
        2 = address
        3 = address2
        4 = cityId
        5 = postalCode
        6 = phone
        7 = createDate
        8 = createdBy
        9 = lastUpdate
        10 = lastUpdateBy
        
        
        */
        List<List> addressList = new ArrayList<>();
        String getAddresses = "select * from address";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getAddresses);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> addressInfo = new ArrayList<>();
                addressInfo.add(rs.getObject(1));
                addressInfo.add(rs.getObject(2));
                addressInfo.add(rs.getObject(3));
                addressInfo.add(rs.getObject(4));
                addressInfo.add(rs.getObject(5));
                addressInfo.add(rs.getObject(6));
                addressInfo.add(rs.getObject(7));
                addressInfo.add(rs.getObject(8));
                addressInfo.add(rs.getObject(9));
                addressInfo.add(rs.getObject(10));
                addressList.add(addressInfo);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return addressList;
    }
    // Query the database to retrieve all cities
    public static List<List> getAllCities()
    {
     /*   City Table Information
        1 = cityId
        2 = city
        3 = countryId
        4 = createDate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
     */
        List<List> allCities = new ArrayList<>();
        /*   allCities Information
        0 = cityId
        1 = city
        2 = countryId
        3 = createDate
        4 = createdBy
        5 = lastUpdate
        6 = lastUpdateBy
     */
        String getCities = "select * from city";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getCities);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> cityInfo = new ArrayList<>();
                cityInfo.add(rs.getObject(1));
                cityInfo.add(rs.getObject(2));
                cityInfo.add(rs.getObject(3));
                cityInfo.add(rs.getObject(4));
                cityInfo.add(rs.getObject(5));
                cityInfo.add(rs.getObject(6));
                cityInfo.add(rs.getObject(7));
                allCities.add(cityInfo);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        return allCities;
    }
    // Query the database to retrieve all countries
    public static List<List> getAllCountries()
    {
        /*  Country Table Information
        1 = countryId
        2 = country
        3 = createDate
        4 = createdBy
        5 = lastUpdate
        6 = lastUpdateBy
        */
        
        List<List> allCountries = new ArrayList<>();
         /*  allCountries Information
        0 = countryId
        1 = country
        2 = createDate
        3 = createdBy
        4 = lastUpdate
        5 = lastUpdateBy
        */
        String getCountries = "select * from country";
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(getCountries);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
                List<Object> countryInfo = new ArrayList<>();
                countryInfo.add(rs.getObject(1));
                countryInfo.add(rs.getObject(2));
                countryInfo.add(rs.getObject(3));
                countryInfo.add(rs.getObject(4));
                countryInfo.add(rs.getObject(5));
                countryInfo.add(rs.getObject(6));
                allCountries.add(countryInfo);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println(allCountries);
        return allCountries;
    }
    
}
