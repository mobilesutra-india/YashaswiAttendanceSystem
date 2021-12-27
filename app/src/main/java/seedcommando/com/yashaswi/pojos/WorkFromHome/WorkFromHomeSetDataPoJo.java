package seedcommando.com.yashaswi.pojos.WorkFromHome;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/15/2018.
 */

public class WorkFromHomeSetDataPoJo {
    @SerializedName("fromdate")
    String fromdate;
    @SerializedName("todate")
    String todate;
    @SerializedName("fromtime")
    String fromtime;
    @SerializedName("totime")
    String totime;
    @SerializedName("days")
    String days;
    @SerializedName("hrs")
    String hrs;
    @SerializedName("status")
    String status;

     String WorkFromHomeAppID;

     String AppFromEmployeeId;

     String OutDutyAppID;

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHrs() {
        return hrs;
    }

    public void setHrs(String hrs) {
        this.hrs = hrs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkFromHomeAppID() {
        return WorkFromHomeAppID;
    }

    public void setWorkFromHomeAppID(String workFromHomeAppID) {
        WorkFromHomeAppID = workFromHomeAppID;
    }

    public String getAppFromEmployeeId() {
        return AppFromEmployeeId;
    }

    public void setAppFromEmployeeId(String appFromEmployeeId) {
        AppFromEmployeeId = appFromEmployeeId;
    }

    public String getOutDutyAppID() {
        return OutDutyAppID;
    }

    public void setOutDutyAppID(String outDutyAppID) {
        OutDutyAppID = outDutyAppID;
    }
}
