/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBCommands;

import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect {
    
    static java.sql.Connection myConnection;
    private static final String dbUrl = "jdbc:mysql://52.206.157.109:3306/U05tHt?zeroDateTimeBehavior=convertToNull";
    // Initialize the connection to the database
    public static void connectToDB() {
        
        try {
            java.sql.Connection dbConn = DriverManager.getConnection(dbUrl, "U05tHt", "53688604209");
            myConnection = dbConn;

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    // Getter for connection to the database
    public static java.sql.Connection getConnection()
    {
        return myConnection;
    }
    // Method to stop the connection
    public static void stopConnection()
    {
        try {
            myConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
    }
    
}
