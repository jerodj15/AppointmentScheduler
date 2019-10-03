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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jerod
 */
public class DBSave extends DBConnect{
    
    // Save the edited customer information to the database
    public static void saveEditedCustomer(List<Object> userVals)
    {
        /* userVals List Structure
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
        int doNext = 0;
        if (doNext == 0)
        {
            Boolean isActive = (Boolean) userVals.get(2);
            int boolToInt = isActive ? 1 : 0;
            String updateCustomer = "update customer set customerName = '" + userVals.get(1) + "', " +
                                                          "active = '" + boolToInt + "', " +
                                                          "lastUpdate = '" + userVals.get(8) + "', " +
                                                          "lastUpdateBy = '" + userVals.get(9) + "'" + "where customerId = '" + userVals.get(0) + "';";
            
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(updateCustomer);
                pstmt.execute();
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage() + " secdn0");
            }
            doNext++;
        }
        
        if (doNext == 1)
        {
            String updateAddress = "update address set address = '" + userVals.get(4) + "', " +
                                                       "address2 = '" + userVals.get(5) + "', " +
                                                       "postalCode = '" + userVals.get(6) + "', " +
                                                       "phone = '" + userVals.get(7) + "' where addressId = '" + userVals.get(3) + "';";
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(updateAddress);
                pstmt.execute();
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage() + " secdn1");
            }
         doNext++;                                  
        }
        
    }
    
    // Save the new customer information to the database
    public static void saveNewCustomer(List<Object> userVals)
    {
        /* userVals Structure
        0 = customerName
        1 = customerActive
        2 = address1
        3 = address2
        4 = countryId
        5 = cityId
        6 = postalCode
        7 = phone
        8 = createDate
        9 = createdBy
        */
        int doNext = 0;
        int newAddressId = 0;
        // First, save the new address
        if (doNext == 0)
        {
            try {
                String saveAddress = "insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) values " +
                        "('" + userVals.get(2) + "', '" + userVals.get(3) + "', '" + userVals.get(5) + "', '" + userVals.get(6) + "', '" + userVals.get(7) + 
                        "', '" + userVals.get(8) + "', '" + userVals.get(9) + "', '" + userVals.get(8) + "', '" + userVals.get(9) + "');";
                
                PreparedStatement pstmt = myConnection.prepareStatement(saveAddress);
                pstmt.execute();
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
            doNext++;
        }
     
        // Second, retrieve the addressId for the new address
        if (doNext == 1)
        {
            try {
                String getAddressId = "select addressId from address where createDate = '" + userVals.get(8) + "' and address = '" + userVals.get(2) + "';";
                PreparedStatement pstmt = myConnection.prepareStatement(getAddressId);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                    newAddressId = rs.getInt(1);
                }
                
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
            doNext++;
        }
        // Third, save the customer and tie the addressId to their information
        if (doNext == 2)
        {
            try {
                String saveCustomer = "insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) values " +
                        "('" + userVals.get(0) + "', '" + newAddressId + "', '" + userVals.get(1) + "', '" + userVals.get(8) + "', '" + userVals.get(9) + "', '" + userVals.get(8) + "', '" + userVals.get(9) + "');";
                PreparedStatement pstmt = myConnection.prepareStatement(saveCustomer);
                pstmt.execute();
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        doNext++;
    }
    // Save the edited appointment information to the database
    public static void saveEditedAppointment(List<Object> userVals)
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
        String startInput = (String) userVals.get(8);
        System.out.println(startInput);
        String endInput = (String) userVals.get(9);
        java.sql.Timestamp tsStart = java.sql.Timestamp.valueOf(startInput) ;
        java.sql.Timestamp tsEnd = java.sql.Timestamp.valueOf(endInput) ;
        List<Object> userInput = userVals;
            Object startZ = tsStart.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(dateFormat);
            userInput.set(8, startZ);
            Object endZ = tsEnd.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(dateFormat);
            userInput.set(9, endZ);
        String saveEditedAppt = "update appointment set customerId = '" + userVals.get(0) + "', " +
                                                        "userId = '" + userVals.get(1) + "', " +
                                                        "title = '" + userVals.get(2) + "', " +
                                                        "description = '" + userVals.get(3) + "', " +
                                                        "location = '" + userVals.get(4) + "', " +
                                                        "contact = '" + userVals.get(5) + "', " +
                                                        "type = '" + userVals.get(6) + "', " +
                                                        "url = '" + userVals.get(7) + "', " +
                                                        "start = '" + userInput.get(8) + "', " +
                                                        "end = '" + userInput.get(9) + "', " +
                                                        "lastUpdate = '" + userVals.get(10) + "', " +
                                                        "lastUpdateBy = '" + userVals.get(11) + "' where appointmentId = '" + userVals.get(12) + "';";
        
        try {
            PreparedStatement pstmt = myConnection.prepareCall(saveEditedAppt);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
     }
    
    // Save the new appointment information to the database
    public static void saveNewAppointment(List<Object> userVals)
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
        // list size 12
        List<Object> userInput = userVals;
        String startInput = (String) userVals.get(8);
        String endInput = (String) userVals.get(9);
         java.sql.Timestamp tsStart = java.sql.Timestamp.valueOf(startInput);
            Object startZ = tsStart.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(dateFormat);
            userInput.set(8, startZ);
            java.sql.Timestamp tsEnd = java.sql.Timestamp.valueOf(endInput);
            Object endZ = tsEnd.toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(dateFormat);
            userInput.set(9, endZ);
        String saveAppt = "insert into appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ('" + userVals.get(0) + "', '" + userVals.get(1) + "', '" + userVals.get(2) + "', '" + userVals.get(3) + "', '" +
                                userVals.get(4) + "', '" + userVals.get(5) + "', '" + userVals.get(6) + "', '" + userVals.get(7) + "', '" +
                                userInput.get(8) + "', '" + userInput.get(9) + "', '" + userVals.get(10) + "', '" + userVals.get(11) + "', '" + userVals.get(10) + "', '" + userVals.get(11) + "');";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(saveAppt);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
