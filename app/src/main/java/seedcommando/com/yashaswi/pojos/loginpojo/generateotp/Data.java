package seedcommando.com.yashaswi.pojos.loginpojo.generateotp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 7/26/2018.
 */

public class Data {
    @SerializedName("Password")
    private String Password;
    @SerializedName("UserGroupID")
    private String UserGroupID;
    @SerializedName("UserID")

    private String UserID;
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("IsOnline")
    private String IsOnline;
    @SerializedName("EmployeeID")
    private String EmployeeID;
    @SerializedName("LastLoginDate")
    private String LastLoginDate;
    @SerializedName("UserGroup")
    private String UserGroup;
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("IsActive")
    private String IsActive;
    @SerializedName("CreatedBy")
    private String CreatedBy;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("IsDeleted")
    private String IsDeleted;
    @SerializedName("IsPwdChanged")
    private String IsPwdChanged;
    @SerializedName("UpdatedBy")
    private String UpdatedBy;

    public String getPassword ()
    {
        return Password;
    }

    public void setPassword (String Password)
    {
        this.Password = Password;
    }

    public String getUserGroupID ()
    {
        return UserGroupID;
    }

    public void setUserGroupID (String UserGroupID)
    {
        this.UserGroupID = UserGroupID;
    }

    public String getUserID ()
    {
        return UserID;
    }

    public void setUserID (String UserID)
    {
        this.UserID = UserID;
    }

    public String getUserName ()
    {
        return UserName;
    }

    public void setUserName (String UserName)
    {
        this.UserName = UserName;
    }

    public String getIsOnline ()
    {
        return IsOnline;
    }

    public void setIsOnline (String IsOnline)
    {
        this.IsOnline = IsOnline;
    }

    public String getEmployeeID ()
    {
        return EmployeeID;
    }

    public void setEmployeeID (String EmployeeID)
    {
        this.EmployeeID = EmployeeID;
    }

    public String getLastLoginDate ()
    {
        return LastLoginDate;
    }

    public void setLastLoginDate (String LastLoginDate)
    {
        this.LastLoginDate = LastLoginDate;
    }

    public String getUserGroup ()
    {
        return UserGroup;
    }

    public void setUserGroup (String UserGroup)
    {
        this.UserGroup = UserGroup;
    }

    public String getUpdatedDate ()
    {
        return UpdatedDate;
    }

    public void setUpdatedDate (String UpdatedDate)
    {
        this.UpdatedDate = UpdatedDate;
    }

    public String getIsActive ()
    {
        return IsActive;
    }

    public void setIsActive (String IsActive)
    {
        this.IsActive = IsActive;
    }

    public String getCreatedBy ()
    {
        return CreatedBy;
    }

    public void setCreatedBy (String CreatedBy)
    {
        this.CreatedBy = CreatedBy;
    }

    public String getCreatedDate ()
    {
        return CreatedDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    public String getIsDeleted ()
    {
        return IsDeleted;
    }

    public void setIsDeleted (String IsDeleted)
    {
        this.IsDeleted = IsDeleted;
    }

    public String getIsPwdChanged ()
    {
        return IsPwdChanged;
    }

    public void setIsPwdChanged (String IsPwdChanged)
    {
        this.IsPwdChanged = IsPwdChanged;
    }

    public String getUpdatedBy ()
    {
        return UpdatedBy;
    }

    public void setUpdatedBy (String UpdatedBy)
    {
        this.UpdatedBy = UpdatedBy;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Password = "+Password+", UserGroupID = "+UserGroupID+", UserID = "+UserID+", UserName = "+UserName+", IsOnline = "+IsOnline+", EmployeeID = "+EmployeeID+", LastLoginDate = "+LastLoginDate+", UserGroup = "+UserGroup+", UpdatedDate = "+UpdatedDate+", IsActive = "+IsActive+", CreatedBy = "+CreatedBy+", CreatedDate = "+CreatedDate+", IsDeleted = "+IsDeleted+", IsPwdChanged = "+IsPwdChanged+", UpdatedBy = "+UpdatedBy+"]";
    }
}
