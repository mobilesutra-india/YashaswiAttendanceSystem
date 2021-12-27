package seedcommando.com.yashaswi.pojos.aprovels.leave;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/12/2018.
 */

public class Data {

    @SerializedName("LevelNo")
    private String LevelNo;
    @SerializedName("MaxLevelCount")
    private String MaxLevelCount;
    @SerializedName("FromDate")
    private String FromDate;
    @SerializedName("ToDate")
    private String ToDate;
    @SerializedName("NoOfLeaveDays")
    private String NoOfLeaveDays;
    @SerializedName("LeaveTypeCode")
    private String LeaveTypeCode;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("ReasonDescription")
    private String ReasonDescription;
    @SerializedName("FinalApplicationStatus")
    private String FinalApplicationStatus;
    @SerializedName("LeaveAppLevelDetailID")
    private String LeaveAppLevelDetailID;
    @SerializedName("LeaveAppIDMaster")
    private String LeaveAppIDMaster;
    @SerializedName("LeaveAppDocumentName")
    private String LeaveAppDocumentName;
    //LeaveAppDocumentName


    public String getLevelNo() {
        return LevelNo;
    }

    public void setLevelNo(String levelNo) {
        LevelNo = levelNo;
    }

    public String getMaxLevelCount() {
        return MaxLevelCount;
    }

    public void setMaxLevelCount(String maxLevelCount) {
        MaxLevelCount = maxLevelCount;
    }

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

    public String getNoOfLeaveDays() {
        return NoOfLeaveDays;
    }

    public void setNoOfLeaveDays(String noOfLeaveDays) {
        NoOfLeaveDays = noOfLeaveDays;
    }

    public String getLeaveTypeCode() {
        return LeaveTypeCode;
    }

    public void setLeaveTypeCode(String leaveTypeCode) {
        LeaveTypeCode = leaveTypeCode;
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

    public String getReasonDescription() {
        return ReasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        ReasonDescription = reasonDescription;
    }

    public String getFinalApplicationStatus() {
        return FinalApplicationStatus;
    }

    public void setFinalApplicationStatus(String finalApplicationStatus) {
        FinalApplicationStatus = finalApplicationStatus;
    }

    public String getLeaveAppLevelDetailID() {
        return LeaveAppLevelDetailID;
    }

    public void setLeaveAppLevelDetailID(String leaveAppLevelDetailID) {
        LeaveAppLevelDetailID = leaveAppLevelDetailID;
    }

    public String getLeaveAppIDMaster() {
        return LeaveAppIDMaster;
    }

    public void setLeaveAppIDMaster(String leaveAppIDMaster) {
        LeaveAppIDMaster = leaveAppIDMaster;
    }

    public String getLeaveAppDocumentName() {
        return LeaveAppDocumentName;
    }

    public void setLeaveAppDocumentName(String leaveAppDocumentName) {
        LeaveAppDocumentName = leaveAppDocumentName;
    }
}
