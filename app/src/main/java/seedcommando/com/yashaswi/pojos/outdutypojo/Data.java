package seedcommando.com.yashaswi.pojos.outdutypojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/30/2018.
 */

public class Data {
    @SerializedName("FromDate")
    private String FromDate;
    @SerializedName("ToDate")
    private String ToDate;
    @SerializedName("NoOfOutDutyDays")
    private String NoOfOutDutyDays;
    @SerializedName("FromTime")
    private String FromTime;
    @SerializedName("ToTime")
    private String ToTime;
    @SerializedName("ApplicationStatus")
    private String ApplicationStatus;
    @SerializedName("NoOfMinutes")
    private String NoOfMinutes;

    @SerializedName("OutDutyAppID")
    private String OutDutyAppID;

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

    public String getNoOfOutDutyDays() {
        return NoOfOutDutyDays;
    }

    public void setNoOfOutDutyDays(String noOfOutDutyDays) {
        NoOfOutDutyDays = noOfOutDutyDays;
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

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getNoOfMinutes() {
        return NoOfMinutes;
    }

    public void setNoOfMinutes(String noOfMinutes) {
        NoOfMinutes = noOfMinutes;
    }

    public String getOutDutyAppID() {
        return OutDutyAppID;
    }

    public void setOutDutyAppID(String outDutyAppID) {
        OutDutyAppID = outDutyAppID;
    }
}
