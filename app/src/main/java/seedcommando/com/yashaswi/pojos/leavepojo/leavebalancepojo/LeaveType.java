package seedcommando.com.yashaswi.pojos.leavepojo.leavebalancepojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/16/2018.
 */

public class LeaveType {
    @SerializedName("LeaveTypeName")
    private String LeaveTypeName;
    @SerializedName("LeaveTypeCode")
    private String LeaveTypeCode;
    @SerializedName("IsActive")
    private String IsActive;
    @SerializedName("LeaveTypeID")
    private String LeaveTypeID;

    public String getLeaveTypeName() {
        return LeaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        LeaveTypeName = leaveTypeName;
    }

    public String getLeaveTypeCode() {
        return LeaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        LeaveTypeCode = leaveTypeCode;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getLeaveTypeID() {
        return LeaveTypeID;
    }

    public void setLeaveTypeID(String leaveTypeID) {
        LeaveTypeID = leaveTypeID;
    }

}
