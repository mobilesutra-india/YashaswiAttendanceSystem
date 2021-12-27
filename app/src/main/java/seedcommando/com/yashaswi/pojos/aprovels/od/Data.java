package seedcommando.com.yashaswi.pojos.aprovels.od;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/13/2018.
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

    @SerializedName("FromTime")
    private String FromTime;
    @SerializedName("ToTime")
    private String ToTime;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("NoOfOutDutyDays")
    private String NoOfOutDutyDays;
    @SerializedName("FinalApplicationStatus")
    private String FinalApplicationStatus;
    @SerializedName("OutDutyAppLevelDetailID")
    private String OutDutyAppLevelDetailID;
    @SerializedName("OutDutyIDMaster")
    private String OutDutyIDMaster;
    @SerializedName("NoOfMinutes")
    private String NoOfMinutes;

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

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
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

    public String getNoOfOutDutyDays() {
        return NoOfOutDutyDays;
    }

    public void setNoOfOutDutyDays(String noOfOutDutyDays) {
        NoOfOutDutyDays = noOfOutDutyDays;
    }

    public String getFinalApplicationStatus() {
        return FinalApplicationStatus;
    }

    public void setFinalApplicationStatus(String finalApplicationStatus) {
        FinalApplicationStatus = finalApplicationStatus;
    }

    public String getOutDutyAppLevelDetailID() {
        return OutDutyAppLevelDetailID;
    }

    public void setOutDutyAppLevelDetailID(String outDutyAppLevelDetailID) {
        OutDutyAppLevelDetailID = outDutyAppLevelDetailID;
    }

    public String getOutDutyIDMaster() {
        return OutDutyIDMaster;
    }

    public void setOutDutyIDMaster(String outDutyIDMaster) {
        OutDutyIDMaster = outDutyIDMaster;
    }

    public String getNoOfMinutes() {
        return NoOfMinutes;
    }

    public void setNoOfMinutes(String noOfMinutes) {
        NoOfMinutes = noOfMinutes;
    }
}
