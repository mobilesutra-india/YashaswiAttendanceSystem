package seedcommando.com.yashaswi.pojos.leavepojo.statuspojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/28/2018.
 */

public class Data {
    @SerializedName("LeaveAppID")
    private String LeaveAppID;

    @SerializedName("FromDate")
    private String FromDate;
    @SerializedName("ToDate")
    private String ToDate;
    @SerializedName("LeaveTypeCode")
    private String LeaveTypeCode;
    @SerializedName("ApplicationStatus")
    private String ApplicationStatus;
    @SerializedName("NoOfLeaveDays")
    private String NoOfLeaveDays;

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getLeaveTypeCode() {
        return LeaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        LeaveTypeCode = leaveTypeCode;
    }

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getNoOfLeaveDays() {
        return NoOfLeaveDays;
    }

    public void setNoOfLeaveDays(String noOfLeaveDays) {
        NoOfLeaveDays = noOfLeaveDays;
    }

    public String getLeaveAppID() {
        return LeaveAppID;
    }

    public void setLeaveAppID(String leaveAppID) {
        LeaveAppID = leaveAppID;
    }
}
