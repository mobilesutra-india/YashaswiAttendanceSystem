package seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/28/2018.
 */

public class Data {

    @SerializedName("ShiftDateFrom")
    private String ShiftDateFrom;
    @SerializedName("ShiftDateTo")
    private String ShiftDateTo;
    @SerializedName("InTime")
    private String InTime;
    @SerializedName("ApplicationStatus")
    private String ApplicationStatus;
    @SerializedName("OutTime")
    private String OutTime;
    @SerializedName("ReasonTitle")
    private String ReasonTitle;
    @SerializedName("ShiftName")
    private String ShiftName;
    @SerializedName("RegularizationAppID")
    private String RegularizationAppId;
    @SerializedName("AppFromEmployeeID")
    private String AppFromEmployeeId;



    public String getShiftDateFrom() {
        return ShiftDateFrom;
    }

    public void setShiftDateFrom(String shiftDateFrom) {
        ShiftDateFrom = shiftDateFrom;
    }

    public String getShiftDateTo() {
        return ShiftDateTo;
    }

    public void setShiftDateTo(String shiftDateTo) {
        ShiftDateTo = shiftDateTo;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getReasonTitle() {
        return ReasonTitle;
    }

    public void setReasonTitle(String reasonTitle) {
        ReasonTitle = reasonTitle;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getRegularizationAppId() {
        return RegularizationAppId;
    }

    public void setRegularizationAppId(String regularizationAppId) {
        RegularizationAppId = regularizationAppId;
    }

    public String getAppFromEmployeeId() {
        return AppFromEmployeeId;
    }

    public void setAppFromEmployeeId(String appFromEmployeeId) {
        AppFromEmployeeId = appFromEmployeeId;
    }
}
