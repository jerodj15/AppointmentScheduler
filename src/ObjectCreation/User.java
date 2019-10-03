package ObjectCreation;

import java.util.ArrayList;
import java.util.List;


public class User {
    
    private int usID;
    private String userName;
    private String password;
    private int active;
    private static List<Object> userInfo = new ArrayList<>();
    
    public User(int userID)
    {
        this.usID = userID;
        this.userInfo = DBCommands.DBRequest.getUserInfoByID(userID);
        this.usID = (int) userInfo.get(0);
        this.userName = (String) userInfo.get(1);
        this.password = (String) userInfo.get(2);
        this.password = (String) userInfo.get(3);
        
        
    }
    public User(String userName)
    {
        this.userInfo = DBCommands.DBRequest.getUserInfoByName(userName);
        this.usID = (int) userInfo.get(0);
        this.userName = (String) userInfo.get(1);
        this.password = (String) userInfo.get(2);
        this.password = (String) userInfo.get(3);
    }
    
}
