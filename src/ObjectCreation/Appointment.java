/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectCreation;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Jerod
 */
public class Appointment {

    public String startTime;
    public String endTime;
    public String titleString;
    public int customerId;
    public String custName;
    public int userID;
    public int apptId;
    public String apptStart;
    public ZonedDateTime apptStartTimeStamp;
    public String type;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm:ss");
    
    public Appointment(List<Object> briefAppointment)
    {
        ZonedDateTime startingTime =  (ZonedDateTime) briefAppointment.get(0);
        ZonedDateTime endingTime =  (ZonedDateTime) briefAppointment.get(1);
        this.startTime = startingTime.toLocalDateTime().format(dtf);
        this.endTime = endingTime.toLocalDateTime().format(dtf);
        this.titleString = (String) briefAppointment.get(2);
        this.customerId = (int) briefAppointment.get(3);
        this.userID = (int) briefAppointment.get(4);
        this.apptId = (int) briefAppointment.get(5);
        this.apptStartTimeStamp = (ZonedDateTime) briefAppointment.get(6);
        this.apptStart = apptStartTimeStamp.toLocalDateTime().format(dtf);
        this.type = (String) briefAppointment.get(7);
    }

    // Setters and getters for appointment object
    public String getStartTime()
    {
        return this.startTime;
    }
    public void setStartTime(String start)
    {
        this.startTime = start;
    }
    public String getEndTime()
    {
        return this.endTime;
    }
    public void setEndTime(String end)
    {
        this.endTime = end;
    }
    public String getTitle()
    {
        return this.titleString;
    }
    public void setTitle(String title)
    {
        this.titleString = title;
    }
    public int getCustomerId()
    {
        return this.customerId;
    }
    public void setCustomerId(int custId)
    {
        this.customerId = custId;
 
    }
    public int getUserId()
    {
        return this.userID;
    }
    public void setUserId(int usId)
    {
        this.userID = usId;
    }
    public int getAppointmentId()
    {
        return this.apptId;
    }
    public void setAppointmentId(int apptID)
    {
        this.apptId = apptID;
    }
    public String getStart()
    {
        return apptStart;
    }
    public void setStart(String startString)
    {
        this.apptStart = startString;
    }
    public String getType()
    {
        return this.type;
    }
    public void setType(String appttype)
    {
        this.type = appttype;
    }
}
