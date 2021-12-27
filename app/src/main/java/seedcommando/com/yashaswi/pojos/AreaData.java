package seedcommando.com.yashaswi.pojos;

import com.google.gson.annotations.SerializedName;

public class AreaData {
    @SerializedName("EmployeeID")
    private String EmployeeID;

    @SerializedName("AreaID")
    private String AreaID;

    @SerializedName("Latitude")
    private String Latitude;

    @SerializedName("Longitude")
    private String Longitude;

    @SerializedName("DistanceOfficeArea")
    private String DistanceOfficeArea;

    @SerializedName("DistanceNearBy")
    private String DistanceNearBy;

    public String getEmployeeID ()
    {
        return EmployeeID;
    }

    public void setEmployeeID (String EmployeeID)
    {
        this.EmployeeID = EmployeeID;
    }

    public String getAreaID ()
    {
        return AreaID;
    }

    public void setAreaID (String AreaID)
    {
        this.AreaID = AreaID;
    }

    public String getDistanceOfficeArea ()
    {
        return DistanceOfficeArea;
    }

    public void setDistanceOfficeArea (String DistanceOfficeArea)
    {
        this.DistanceOfficeArea = DistanceOfficeArea;
    }

    public String getLatitude ()
    {
        return Latitude;
    }

    public void setLatitude (String Latitude)
    {
        this.Latitude = Latitude;
    }

    public String getLongitude ()
    {
        return Longitude;
    }

    public void setLongitude (String Longitude)
    {
        this.Longitude = Longitude;
    }

    public String getDistanceNearBy ()
    {
        return DistanceNearBy;
    }

    public void setDistanceNearBy (String DistanceNearBy)
    {
        this.DistanceNearBy = DistanceNearBy;
    }

}

