package Misc;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.util.converter.LocalDateTimeStringConverter;


public class DateCompare {
    
    static LocalTime closeTime = LocalTime.of(17, 00);
    static LocalTime startingTime = LocalTime.of(9, 00);
    static List<String> warningList = new ArrayList<>();
    static List<String> isTimeList = new ArrayList<>();
    
    // Clear the warning lists 
    public static void clearList()
    {
        warningList.clear();
        isTimeList.clear();
    }
    
    // Queries the database to make sure the appointment will not conflict with other appointments
    public static boolean isInAvailableTimeSlot(ZonedDateTime startDateTime, ZonedDateTime endDateTime, int userId, int apptId)
    {
        ObservableList<List> allAppts = DBCommands.DBRequest.getAppointmentsByUserId(userId);
        List<Integer> allApptId = new ArrayList<>();
        List<ZonedDateTime> startZonedDateTimes = new ArrayList<>();
        List<ZonedDateTime> endZonedDateTimes = new ArrayList<>();
        boolean isAvailable = true;
        int doNext = 0;
        // Poulate the lists with the right date and time format
        if (doNext == 0)
        {
            for (int i =0; i < allAppts.size(); i++)
            {
                allApptId.add((Integer) allAppts.get(i).get(0));
            }
            for (int i = 0; i < allAppts.size(); i ++)
            {
                ZonedDateTime startTime =  (ZonedDateTime) allAppts.get(i).get(9);
                ZonedDateTime endTime = (ZonedDateTime) allAppts.get(i).get(10);
                startZonedDateTimes.add(startTime.toInstant().atZone(ZoneId.systemDefault()));
                endZonedDateTimes.add(endTime.toInstant().atZone(ZoneId.systemDefault()));
            }
            doNext++;
        }
        // Compare the user information with the database information
        if (doNext == 1)
        {
            System.out.println("submit start dayofyear" + startDateTime.getDayOfYear());
                        System.out.println("submit end dayofyear" + endDateTime.getDayOfYear());
                       
            for (int i = 0; i < allAppts.size(); i++)
            {   System.out.println("Begin list of datetime = " + startZonedDateTimes.get(i));
                System.out.println("End list of dateTime = " + endZonedDateTimes.get(i));
                System.out.println("Begin for state Appt ID = " + apptId);
                System.out.println("Begin for state List APPT ID = " + allApptId.get(i));
                if (startDateTime.getDayOfYear() == endDateTime.getDayOfYear())
                {
                if (startZonedDateTimes.get(i).getDayOfYear() == startDateTime.getDayOfYear() && endZonedDateTimes.get(i).getDayOfYear() == endDateTime.getDayOfYear())
                {
                    if ((startDateTime.toLocalTime().isAfter(startZonedDateTimes.get(i).toLocalTime()) == true) && (startDateTime.toLocalTime().isBefore(endZonedDateTimes.get(i).toLocalTime()) == true) && ((allApptId.get(i) != apptId)))
                    {
                        isTimeList.add("The start time is in between another appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if (((startDateTime.toLocalTime().equals(startZonedDateTimes.get(i).toLocalTime()) == true) && ((allApptId.get(i) != apptId))) || ((startDateTime.toLocalTime().equals(endZonedDateTimes.get(i).toLocalTime()) == true) && ((allApptId.get(i) != apptId))))
                    {
                        isTimeList.add("The start time falls on the same start or end time as another appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if (((endDateTime.toLocalTime().equals(startZonedDateTimes.get(i).toLocalTime())) == true || (endDateTime.toLocalTime().equals(endZonedDateTimes.get(i).toLocalTime()) == true)) && ((allApptId.get(i) != apptId)))
                    {
                        isTimeList.add("The end time falls on the same start or end time as anopther appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if (((endDateTime.toLocalTime().isAfter(startZonedDateTimes.get(i).toLocalTime()) == true) && (endDateTime.toLocalTime().isBefore(endZonedDateTimes.get(i).toLocalTime()) == true)) && (allApptId.get(i) != apptId))
                    {
                        isTimeList.add("The end time is in between another appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if (((startDateTime.isBefore(startZonedDateTimes.get(i)) == true) && (endDateTime.isAfter(endZonedDateTimes.get(i)) == true)))
                    {
                        isTimeList.add("So far the time shadows another appointment");
                       if (allApptId.get(i) != apptId)
                       {
                           isAvailable = false;
                           isTimeList.add("The appointment is shadowing another appointment by the same user");
                           break;
                       }
                       else
                       {
                           isTimeList.add("It shadowed but the apptID matched");
                           isAvailable = true;
                           break;
                       }
                    }
                    else
                    {
                        isTimeList.add("Everything looks good");
                        isAvailable = true;
                    }
                    
                }
                
                }
                else
                {
                    isTimeList.add("Appointment has to occur on the same day");
                    isAvailable = false;
                    return isAvailable;
                }
            }
            
            doNext++;
        }
       
            
           
        System.out.println(isTimeList);
        doNext++;
        return isAvailable;
    }
    
    // Queries the database to make sure the appointment will not conflict with other appointments
    public static boolean isInAvailableTimeSlot(ZonedDateTime startDateTime, ZonedDateTime endDateTime, int userId)
    {
        ObservableList<List> allAppts = DBCommands.DBRequest.getAppointmentsByUserId(userId);
        List<Integer> allApptId = new ArrayList<>();
        List<ZonedDateTime> startZonedDateTimes = new ArrayList<>();
        List<ZonedDateTime> endZonedDateTimes = new ArrayList<>();
        boolean isAvailable = true;
        int doNext = 0;
        // Poulate the lists with the right date and time format
        if (doNext == 0)
        {
            for (int i =0; i < allAppts.size(); i++)
            {
                allApptId.add((Integer) allAppts.get(i).get(0));
            }
            for (int i = 0; i < allAppts.size(); i ++)
            {
                ZonedDateTime startTime =  (ZonedDateTime) allAppts.get(i).get(9);
                ZonedDateTime endTime =  (ZonedDateTime) allAppts.get(i).get(10);
                startZonedDateTimes.add(startTime);
                endZonedDateTimes.add(endTime);
            }
            doNext++;
        }
        // Compare the user information with the database information
        if (doNext == 1)
        {
            System.out.println(allAppts);
            System.out.println("submit start dayofyear" + startDateTime.getDayOfYear());
                        System.out.println("submit end dayofyear" + endDateTime.getDayOfYear());
                       
            for (int i = 0; i < allAppts.size(); i++)
            {   System.out.println("Begin list of datetime = " + startZonedDateTimes.get(i));
                System.out.println("End list of dateTime = " + endZonedDateTimes.get(i));
                System.out.println("Begin for state List APPT ID = " + allApptId.get(i));
                if (startDateTime.getDayOfYear() == endDateTime.getDayOfYear())
                {
                if (startZonedDateTimes.get(i).getDayOfYear() == startDateTime.getDayOfYear() && endZonedDateTimes.get(i).getDayOfYear() == endDateTime.getDayOfYear())
                {
                    if ((startDateTime.toLocalTime().isAfter(startZonedDateTimes.get(i).toLocalTime()) == true) && (startDateTime.toLocalTime().isBefore(endZonedDateTimes.get(i).toLocalTime()) == true))
                    {
                        isTimeList.add("The start time is in between another appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if ((startDateTime.toLocalTime().equals(startZonedDateTimes.get(i).toLocalTime()) == true) || (startDateTime.toLocalTime().equals(endZonedDateTimes.get(i).toLocalTime()) == true))
                    {
                        isTimeList.add("The start time falls on the same start or end time as another appointment");
                        isAvailable = false;
                        break;
                    }
                    else if (((endDateTime.toLocalTime().equals(startZonedDateTimes.get(i).toLocalTime())) == true || (endDateTime.toLocalTime().equals(endZonedDateTimes.get(i).toLocalTime()) == true)))
                    {
                        isTimeList.add("The end time falls on the same start or end time as anopther appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if ((endDateTime.toLocalTime().isAfter(startZonedDateTimes.get(i).toLocalTime()) == true) && (endDateTime.toLocalTime().isBefore(endZonedDateTimes.get(i).toLocalTime()) == true))
                    {
                        isTimeList.add("The end time is in between another appointment and the appointment ID is not the same");
                        isAvailable = false;
                        break;
                    }
                    else if (((startDateTime.isBefore(startZonedDateTimes.get(i)) == true) && (endDateTime.isAfter(endZonedDateTimes.get(i)) == true)))
                    {
                        isTimeList.add("So far the time shadows another appointment");
                      
                           isAvailable = false;
                           isTimeList.add("The appointment is shadowing another appointment by the same user");
                           break;
                   
                    }
                    else
                    {
                        isTimeList.add("Everything looks good");
                        isAvailable = true;
                    }
                    
                }
                
                }
                else
                {
                    isTimeList.add("Appointment has to occur on the same day");
                    isAvailable = false;
                    break;
                }
            }
            
            doNext++;
        }
       
            
           
        System.out.println(isTimeList);
        doNext++;
        return isAvailable;
    }
    
     // Queries the database to make sure the appointment is in the businesses operating days
    public static boolean isInBusinessDays(ZonedDateTime startDateTime, ZonedDateTime endDateTime)
    {
        boolean isStartInBusinessDays;
        boolean isEndInBusinessDays;
        boolean inBusinessDays; 
        DayOfWeek startDayOfWeek = startDateTime.getDayOfWeek();
        DayOfWeek endDayOfWeek = endDateTime.getDayOfWeek();
        if (startDayOfWeek == DayOfWeek.SUNDAY || startDayOfWeek == DayOfWeek.SATURDAY)
        {
            isStartInBusinessDays = false;
        }
        else
        {
            isStartInBusinessDays = true;
        }
        if (endDayOfWeek == DayOfWeek.SATURDAY || endDayOfWeek == DayOfWeek.SUNDAY)
        {
            isEndInBusinessDays = false;
        }
        else
        {
            isEndInBusinessDays = true;
        }
        if (isStartInBusinessDays == true && isEndInBusinessDays == true)
        {
            inBusinessDays = true;
        }
        else
        {
            inBusinessDays = false;
        }
        warningList.add("Is in business days = " + inBusinessDays);
        return inBusinessDays;
    }
    
     // Queries the database to make sure the appointment is in the businesses operating hours
    public static boolean isInBusinessHours(ZonedDateTime startDateTime, ZonedDateTime endDateTime)
    {
        boolean isInBusinessHours;
        LocalTime startInLocalTime = startDateTime.toLocalTime();
        LocalTime endInLocalTime = endDateTime.toLocalTime();
        if (startInLocalTime.isBefore(startingTime) == true)
        {
            isInBusinessHours = false;
        }
        else if (startInLocalTime.isAfter(closeTime) == true)
        {
            isInBusinessHours = false;
        }
        else if (endInLocalTime.isBefore(startingTime) == true)
        {
            isInBusinessHours = false;
        }
        else if (endInLocalTime.isAfter(closeTime) == true)
        {
            isInBusinessHours = false;
        }
        else 
        {
            isInBusinessHours = true;
        }
        warningList.add("Is in business hours = " + isInBusinessHours);
        return isInBusinessHours;
    }
     // Queries the database to make sure the appointment start and end times are in the right order
    public static boolean isChronologicallyCorrect(ZonedDateTime startDateTime, ZonedDateTime finishDateTime)
    {
        boolean isChronological;
        if (startDateTime.isBefore(finishDateTime))
        {
            isChronological = true;
        }
        else
        {
            isChronological = false;
        }
        warningList.add("Is chronological = " + isChronological);
        return isChronological;       
    }
     // Queries the database to check all the methods within this class
    public static boolean isAllCorrect(ZonedDateTime startDateTime, ZonedDateTime endDateTime)
    {
        boolean correct;
        boolean isDays = isInBusinessDays(startDateTime, endDateTime);
        boolean isHours = isInBusinessHours(startDateTime, endDateTime);
        boolean isInOrder = isChronologicallyCorrect(startDateTime, endDateTime);
        if (isDays == true && isHours == true && isInOrder == true)
        {
            correct = true;
        }
        else
        {
            correct = false;
        }
        warningList.add("Is All correct = " + correct);
        System.out.println(warningList.toString());
        return correct;
    }
    
}
