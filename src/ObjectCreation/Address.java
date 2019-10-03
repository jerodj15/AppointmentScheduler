/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectCreation;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Jerod
 */
public class Address {
    
    private int addressId;
    private String address1;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
    public Address(List<Object> addressStuff)
    {
        this.addressId = (int) addressStuff.get(0);
        this.address1 = (String) addressStuff.get(1);
        this.address2 = (String) addressStuff.get(2);
        this.cityId = (int) addressStuff.get(3);
        this.postalCode = (String) addressStuff.get(4);
        this.phone = (String) addressStuff.get(5);
        Timestamp cd = (Timestamp) addressStuff.get(6);
        this.createDate = cd.toLocalDateTime().format(dtf);
        this.createdBy = (String) addressStuff.get(7);
        Timestamp lu = (Timestamp) addressStuff.get(8);
        this.lastUpdate = lu.toLocalDateTime().format(dtf);
        this.lastUpdateBy = (String) addressStuff.get(9);
    }
    
    // Setters and getter for address object
    public void setAddressId(int addId)
    {
        this.addressId = addId;
    }
    public int getAddressId()
    {
        return this.addressId;
    }
    public void setAddress1(String add1)
    {
        this.address1 = add1;
    }
    public String getAddress1()
    {
        return this.address1;
    }
    public void setAddress2(String add2)
    {
        this.address2 = add2;
    }
    public String getAddress2()
    {
        return this.address2;
    }
    public void setCityId(int cityid)
    {
        this.cityId = cityid;
    }
    public int getCityId()
    {
        return this.cityId;
    }
    public void setPostalCode(String postCode)
    {
        this.postalCode = postCode;
    }
    public String getPostalCode()
    {
        return this.postalCode;
    }
    public void setPhone(String phNum)
    {
        this.phone = phNum;
    }
    public String getPhone()
    {
        return this.phone;
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
