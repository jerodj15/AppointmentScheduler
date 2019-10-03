/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCommands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jerod
 */
public class DBCompare extends DBConnect{
    // Get the customer details for the customerDetails window
    public static List<Object> getCustomerDetails(int addressID, int customerID)
    {
        // Customer Table Information                City Table Information
        /*                                          1 = cityId
        1 = customerId                              2 = city
        2 = customerName                            3 = countryId
        3 = addressId                               4 = createDate
        4 = active                                  5 = createdBy
        5 = createDate                              6 = lastUpdate
        6 = createdBy                               7 = lastUpdateBy
        7 = lastUpdate
        8 = lastUpdateBy
        */
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
        List<Object> combinedCustomerInfo = new ArrayList<>();
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
        List<Object> customerInfo = new ArrayList<>();
        // customeerInfo Structure
        /*
        0 = customerId
        1 = customerName
        2 = addressId
        3 = active
        4 = createate
        5 = createdBy
        6 = lastUpdate
        7 = lastUpdateBy
        */
        List<Object> addressInfo = new ArrayList<>();
        // addressInfo Structure
        /*
        0 = address
        1 = address2
        2 = cityId
        3 = postalCode
        4 = phone
        */
        List<Object> cityInfo = new ArrayList<>();
        // cityInfo Structure
        /*
        0 = cityName
        1 = countryId
        */
        List<Object> countryInfo = new ArrayList<>();
        // countryInfo Structure
        // 0 = country
        String requestCustomerInfo = "select * from customer where customerId = '" + customerID + "';";
        int doNext = 0;
        // Get the customer information
        if (doNext == 0)
        {
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(requestCustomerInfo);
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
            doNext++;
        }
        // Get the customers address information based on their address ID
        if (doNext == 1)
        {
            String requestAddressInfo = "select * from address where addressId = '" + addressID + "';";
            
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(requestAddressInfo);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                    addressInfo.add(rs.getObject(2));
                    addressInfo.add(rs.getObject(3));
                    addressInfo.add(rs.getObject(4));
                    addressInfo.add(rs.getObject(5));
                    addressInfo.add(rs.getObject(6));
                }
                
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
            doNext++;
        }
        // Get the name of the city using the cityId
        if (doNext == 2)
        {
            String requestCityInfo = "select * from city where cityId = '" + addressInfo.get(2) + "';";
            
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(requestCityInfo);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                    cityInfo.add(rs.getObject(2));
                    cityInfo.add(rs.getObject(3));
                }
                
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
            doNext++;
        }
        // Get the name of the country
        if (doNext == 3)
        {
           
            String requestCountryName = "select * from country where countryId = '" + cityInfo.get(1) + "';";
            
            try {
                PreparedStatement pstmt = myConnection.prepareStatement(requestCountryName);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                    countryInfo.add(rs.getObject(2));
                }
                
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
            doNext++;
        }
        if (doNext == 4)
        {
            combinedCustomerInfo.addAll(customerInfo);
            combinedCustomerInfo.addAll(addressInfo);
            combinedCustomerInfo.addAll(cityInfo);
            combinedCustomerInfo.addAll(countryInfo);
            doNext++;
        }
        
        
        return combinedCustomerInfo;
    }
    
}
