package seedcommando.com.yashaswi.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Userlist  {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    private String employeeId;


    @SerializedName("data")
    //private List<User_Data> user_array = null;
    private ArrayList<User_Data> data;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public ArrayList<User_Data> getData() {
        return data;
    }

    public void setData(ArrayList<User_Data> data) {
        this.data = data;
    }
}
