/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class CheckEntries {

    static List<String> errorsList = new ArrayList<>();
    
    // Check the submitted list for anything empty
    public static boolean checkForEmpty(List<Object> userVals)
    {
        boolean isCorrect = false;
        for (int i = 0; i < userVals.size(); i ++)
        {
            if (userVals.get(i).toString().isEmpty() == true)
            {
                errorsList.add("There are some fields that are blank");
                isCorrect = false;
                break;
            }
            else
            {
                errorsList.add("There are no errors with blank fields");
                isCorrect = true;
            }
        }
        System.out.println(errorsList);
        System.out.println(isCorrect);
        return isCorrect;
    }
    
    // Check the submitted datTime for the correct format
    public static boolean isCorrectDateTimeFormat(List<String> userVals)
    {
        boolean isCorrect = true;
        String dateFormat = "YYYY-MM-dd kk:mm";
        SimpleDateFormat dtf = new SimpleDateFormat(dateFormat);
        dtf.setLenient(false);
        for (int i = 0; i < userVals.size(); i++)
        {
            String stringInput = userVals.get(i);
        try {
            dtf.parse(stringInput);
            System.out.println(dtf.parse(stringInput));
            
        } catch (ParseException ex) {
            errorsList.add("The start or end is not the correct date time format");
            isCorrect = false;
        }
        }
        return isCorrect;
    }
    
    // Checks both the emptiness and the format of the date
    public static boolean isCorrectDateTime(List<Object> userVals)
    {
        List<String> startAndEnd = new ArrayList<>();
        startAndEnd.add((String) userVals.get(8));
        startAndEnd.add((String) userVals.get(9));
        boolean isCorrectDateTime;
        boolean isNotEmpty = checkForEmpty(userVals);
        boolean isCorrectDTF = isCorrectDateTimeFormat(startAndEnd);
        if ((isNotEmpty == true) && (isCorrectDTF == true))
        {
            isCorrectDateTime = true;
        }
        else
        {
            isCorrectDateTime = false;
        }
        System.out.println(errorsList);
        return isCorrectDateTime;
    }
     
}
