/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCommands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Jerod
 */
public class DBRemove extends DBConnect{
    
    // Permanently remove the customer from the database
    public static void removeCustomer(int customerId, int addressId)
    {
        String removeCustomer = "delete from customer where customerId = '" + customerId + "';";
        String removeAddress = "delete from address where addressId = '" + addressId + "';";
        try {
            PreparedStatement pstmt1 = myConnection.prepareStatement(removeCustomer);
            PreparedStatement pstmt2 = myConnection.prepareStatement(removeAddress);
            pstmt1.execute();
            pstmt2.execute();
   
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }
    // Permanently remove the appointment from the database
    public static void removeAppointment(int appointmentID)
    {
        String removeAppt = "delete from appointment where appointmentId = '" + appointmentID + "';";
        
        try {
            PreparedStatement pstmt = myConnection.prepareStatement(removeAppt);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
}
