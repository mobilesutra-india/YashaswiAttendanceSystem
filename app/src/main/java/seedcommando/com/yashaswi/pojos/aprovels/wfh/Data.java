package seedcommando.com.yashaswi.pojos.aprovels.wfh;

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
    @SerializedName("NoOfWorkFromHomeDays")
    private String NoOfWorkFromHomeDays;
    @SerializedName("FinalApplicationStatus")
    private String FinalApplicationStatus;
    @SerializedName("WorkFromHomeAppLevelDetailID")
    private String WorkFromHomeAppLevelDetailID;
    @SerializedName("WorkFromHomeAppIDMaster")
    private String WorkFromHomeAppIDMaster;
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

    public String getNoOfWorkFromHomeDays() {
        return NoOfWorkFromHomeDays;
    }

    public void setNoOfWorkFromHomeDays(String noOfWorkFromHomeDays) {
        NoOfWorkFromHomeDays = noOfWorkFromHomeDays;
    }

    public String getFinalApplicationStatus() {
        return FinalApplicationStatus;
    }

    public void setFinalApplicationStatus(String finalApplicationStatus) {
        FinalApplicationStatus = finalApplicationStatus;
    }

    public String getWorkFromHomeAppLevelDetailID() {
        return WorkFromHomeAppLevelDetailID;
    }

    public void setWorkFromHomeAppLevelDetailID(String workFromHomeAppLevelDetailID) {
        WorkFromHomeAppLevelDetailID = workFromHomeAppLevelDetailID;
    }

    public String getWorkFromHomeAppIDMaster() {
        return WorkFromHomeAppIDMaster;
    }

    public void setWorkFromHomeAppIDMaster(String workFromHomeAppIDMaster) {
        WorkFromHomeAppIDMaster = workFromHomeAppIDMaster;
    }

    public String getNoOfMinutes() {
        return NoOfMinutes;
    }

    public void setNoOfMinutes(String noOfMinutes) {
        NoOfMinutes = noOfMinutes;
    }
}
