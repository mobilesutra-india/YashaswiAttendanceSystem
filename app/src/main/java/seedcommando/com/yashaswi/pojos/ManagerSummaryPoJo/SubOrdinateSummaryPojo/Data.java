package seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SubOrdinateSummaryPojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 6/1/2018.
 */

public class Data {
    @SerializedName("Status")
    private String Status;
    @SerializedName("EmployeeID")
    private String EmployeeID;
    @SerializedName("EmployeeCode")
    private String EmployeeCode;
    @SerializedName("EmployeeName")//WeekTitle
    private String EmployeeName;//InTime
    @SerializedName("InTime")
    private String InTime;
    @SerializedName("OutTime")
    private String OutTime;
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

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

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }
}
