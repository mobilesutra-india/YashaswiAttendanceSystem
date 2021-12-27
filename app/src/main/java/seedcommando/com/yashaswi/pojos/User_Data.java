package seedcommando.com.yashaswi.pojos;

import com.google.gson.annotations.SerializedName;


public class User_Data {

    @SerializedName("EmployeeID")
    private  String EmployeeID;

    @SerializedName("EmployeeCode")
    private  String EmployeeCode;

    @SerializedName("FirstName")
    private  String FirstName;

    @SerializedName("LastName")
    private  String  LastName;




    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }


}


