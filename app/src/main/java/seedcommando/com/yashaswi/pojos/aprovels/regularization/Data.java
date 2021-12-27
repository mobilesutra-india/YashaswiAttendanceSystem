package seedcommando.com.yashaswi.pojos.aprovels.regularization;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/12/2018.
 */

public class Data {

    @SerializedName("LevelNo")
    private String LevelNo;
    @SerializedName("MaxLevelCount")
    private String MaxLevelCount;
    @SerializedName("ShiftDateFrom")
    private String ShiftDateFrom;
    @SerializedName("ShiftDateTo")
    private String ShiftDateTo;

    @SerializedName("InTime")
    private String InTime;
    @SerializedName("OutTime")
    private String OutTime;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("ReasonTitle")
    private String ReasonTitle;
    @SerializedName("FinalApplicationStatus")
    private String FinalApplicationStatus;
    @SerializedName("RegularizationAppLevelDetailID")
    private String RegularizationAppLevelDetailID;
    @SerializedName("RegularizationAppIDMaster")
    private String RegularizationAppIDMaster;


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

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
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

    public String getReasonTitle() {
        return ReasonTitle;
    }

    public void setReasonTitle(String reasonTitle) {
        ReasonTitle = reasonTitle;
    }

    public String getFinalApplicationStatus() {
        return FinalApplicationStatus;
    }

    public void setFinalApplicationStatus(String finalApplicationStatus) {
        FinalApplicationStatus = finalApplicationStatus;
    }

    public String getRegularizationAppLevelDetailID() {
        return RegularizationAppLevelDetailID;
    }

    public void setRegularizationAppLevelDetailID(String regularizationAppLevelDetailID) {
        RegularizationAppLevelDetailID = regularizationAppLevelDetailID;
    }

    public String getRegularizationAppIDMaster() {
        return RegularizationAppIDMaster;
    }

    public void setRegularizationAppIDMaster(String regularizationAppIDMaster) {
        RegularizationAppIDMaster = regularizationAppIDMaster;
    }
}
