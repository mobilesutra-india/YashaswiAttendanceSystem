package seedcommando.com.yashaswi.pojos.leavepojo.leavetypecode;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/23/2018.
 */

public class Data {
      @SerializedName("LeaveTypeName")
    private String LeaveTypeName;
    @SerializedName("StatutoryTypeID")
    private String StatutoryTypeID;

    @SerializedName("LeaveTypeCode")
    private String LeaveTypeCode;
    @SerializedName("LeaveTypeID")

    private String LeaveTypeID;

    public String getLeaveTypeName() {
        return LeaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        LeaveTypeName = leaveTypeName;
    }

    public String getStatutoryTypeID() {
        return StatutoryTypeID;
    }

    public void setStatutoryTypeID(String statutoryTypeID) {
        StatutoryTypeID = statutoryTypeID;
    }

    public String getLeaveTypeCode() {
        return LeaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        LeaveTypeCode = leaveTypeCode;
    }

    public String getLeaveTypeID() {
        return LeaveTypeID;
    }

    public void setLeaveTypeID(String leaveTypeID) {
        LeaveTypeID = leaveTypeID;
    }
}
