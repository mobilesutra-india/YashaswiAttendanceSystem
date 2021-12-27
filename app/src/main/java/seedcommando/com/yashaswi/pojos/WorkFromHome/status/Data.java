package seedcommando.com.yashaswi.pojos.WorkFromHome.status;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/27/2018.
 */

public class Data {


    @SerializedName("FinalApplicationStatusID")
    private String FinalApplicationStatusID;
    @SerializedName("IsWFHAppInHours")
    private String IsWFHAppInHours;
    @SerializedName("WorkFlowMasterID")
    private String WorkFlowMasterID;
    @SerializedName("FromDate")
    private String FromDate;
    @SerializedName("NoOfWorkFromHomeDays")
    private String NoOfWorkFromHomeDays;
    @SerializedName("WorkFromHomeAppID")
    private String WorkFromHomeAppID;
    @SerializedName("ToDate")
    private String ToDate;
    @SerializedName("ApplicationStatus")
    private String ApplicationStatus;
    @SerializedName("IsFirstHalf")
    private String IsFirstHalf;
    /*@SerializedName("ToTime")
    private String ToTime;*/
    @SerializedName("ToTime")
    private String ToTime;
    @SerializedName("NoOfMinutes")
    private String NoOfMinutes;

    @SerializedName("FromTime")
    private String FromTime;
    @SerializedName("IsSecondHalf")
    private String IsSecondHalf;



    public String getFinalApplicationStatusID() {
        return FinalApplicationStatusID;
    }

    public void setFinalApplicationStatusID(String finalApplicationStatusID) {
        FinalApplicationStatusID = finalApplicationStatusID;
    }

    public String getIsWFHAppInHours() {
        return IsWFHAppInHours;
    }

    public void setIsWFHAppInHours(String isWFHAppInHours) {
        IsWFHAppInHours = isWFHAppInHours;
    }

    public String getWorkFlowMasterID() {
        return WorkFlowMasterID;
    }

    public void setWorkFlowMasterID(String workFlowMasterID) {
        WorkFlowMasterID = workFlowMasterID;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getNoOfWorkFromHomeDays() {
        return NoOfWorkFromHomeDays;
    }

    public void setNoOfWorkFromHomeDays(String noOfWorkFromHomeDays) {
        NoOfWorkFromHomeDays = noOfWorkFromHomeDays;
    }

    public String getWorkFromHomeAppID() {
        return WorkFromHomeAppID;
    }

    public void setWorkFromHomeAppID(String workFromHomeAppID) {
        WorkFromHomeAppID = workFromHomeAppID;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getIsFirstHalf() {
        return IsFirstHalf;
    }

    public void setIsFirstHalf(String isFirstHalf) {
        IsFirstHalf = isFirstHalf;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getNoOfMinutes() {
        return NoOfMinutes;
    }

    public void setNoOfMinutes(String noOfMinutes) {
        NoOfMinutes = noOfMinutes;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getIsSecondHalf() {
        return IsSecondHalf;
    }

    public void setIsSecondHalf(String isSecondHalf) {
        IsSecondHalf = isSecondHalf;
    }

}
