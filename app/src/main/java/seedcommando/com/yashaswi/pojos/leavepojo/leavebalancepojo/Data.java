package seedcommando.com.yashaswi.pojos.leavepojo.leavebalancepojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by commando4 on 3/16/2018.
 */

public class Data {
    @SerializedName("OpeningBalance")
    private String OpeningBalance;
    @SerializedName("LeaveTypeID")
    private String LeaveTypeID;
    @SerializedName("PendingAppsDays")

    private String PendingAppsDays;
    //private LeaveType leaveType=new LeaveType();
    private List<LeaveType> LeaveType;

    //private ArrayList<LeaveType> LeaveType;
    @SerializedName("EmployeeID")
    private String EmployeeID;
    @SerializedName("CurrentBalance")
    private String CurrentBalance;
    @SerializedName("LeaveBalanceID")
    private String LeaveBalanceID;

    public String getOpeningBalance() {
        return OpeningBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        OpeningBalance = openingBalance;
    }

    public String getLeaveTypeID() {
        return LeaveTypeID;
    }

    public void setLeaveTypeID(String leaveTypeID) {
        LeaveTypeID = leaveTypeID;
    }

    public String getPendingAppsDays() {
        return PendingAppsDays;
    }

    public void setPendingAppsDays(String pendingAppsDays) {
        PendingAppsDays = pendingAppsDays;
    }

    public List<seedcommando.com.yashaswi.pojos.leavepojo.leavebalancepojo.LeaveType> getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(List<seedcommando.com.yashaswi.pojos.leavepojo.leavebalancepojo.LeaveType> leaveType) {
        LeaveType = leaveType;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getCurrentBalance() {
        return CurrentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        CurrentBalance = currentBalance;
    }

    public String getLeaveBalanceID() {
        return LeaveBalanceID;
    }

    public void setLeaveBalanceID(String leaveBalanceID) {
        LeaveBalanceID = leaveBalanceID;
    }
}
