package ObjectCreation;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Customer {
    private int customerID;
    private String customerName;
    private int addressID;
    private boolean active;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
    public Customer(List<Object> customerInfo)
    {
        customerID = (int) customerInfo.get(0);
        customerName = (String) customerInfo.get(1);
        addressID = (int) customerInfo.get(2);
        active = (boolean) customerInfo.get(3);
        Timestamp createdDate = (Timestamp) customerInfo.get(4);
        createDate = createdDate.toLocalDateTime().format(dtf);
        createdBy = (String) customerInfo.get(5);
        Timestamp lastUp = (Timestamp) customerInfo.get(6);
        lastUpdate = lastUp.toLocalDateTime().format(dtf);
        lastUpdateBy = (String) customerInfo.get(7);
    }
    
    
    // Setters and getters for customer object
    public void setCustomerId(int custId)
    {
        this.customerID = custId;
    }
    public int getCustomerId()
    {
        return this.customerID;
    }
    public void setCustomerName(String custName)
    {
        this.customerName = custName;
    }
    public String getCustomerName()
    {
        return this.customerName;
    }
    public void setAddressId(int addId)
    {
        this.addressID = addId;
    }
    public int getAddressId()
    {
        return this.addressID;
    }
    public void setActive(boolean act)
    {
        this.active = act;
    }
    public boolean getActive()
    {
        return this.active;
    }
    public void setCreateDate(String create)
    {
        this.createDate = create;
    }
    public String getCreateDate()
    {
        return this.createDate;
    }
    public void setCreatedBy(String create)
    {
        this.createdBy = create;
    }
    public String getCreatedBy()
    {
        return this.createdBy;
    }
    public void setLastUpdate(String lastUp)
    {
        this.lastUpdate = lastUp;
    }
    public String getLastUpdate()
    {
        return this.lastUpdate;
    }
    public void setLastUpdatedBy(String lastUp)
    {
        this.lastUpdateBy = lastUp;
    }
    public String getLastUpdatedBy()
    {
        return this.lastUpdateBy;
    }
    
    
}
