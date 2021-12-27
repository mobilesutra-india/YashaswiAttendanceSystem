package seedcommando.com.yashaswi.pojos.aprovels.compoff;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/13/2018.
 */

public class Data {
    @SerializedName("LevelNo")
    private String LevelNo;
    @SerializedName("MaxLevelCount")
    private String MaxLevelCount;


    @SerializedName("TakenCompOffDate")
    private String TakenCompOffDate;
    @SerializedName("ReasonDescription")
    private String ReasonDescription;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("NoOfCompOffDays")
    private String NoOfCompOffDays;
    @SerializedName("FinalApplicationStatus")
    private String FinalApplicationStatus;
    @SerializedName("CompensatoryOffAppLevelDetailID")
    private String CompensatoryOffAppLevelDetailID;
    @SerializedName("CompensatoryOffAppIDMaster")
    private String CompensatoryOffAppIDMaster;

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

    public String getTakenCompOffDate() {
        return TakenCompOffDate;
    }

    public void setTakenCompOffDate(String takenCompOffDate) {
        TakenCompOffDate = takenCompOffDate;
    }

    public String getReasonDescription() {
        return ReasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        ReasonDescription = reasonDescription;
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

    public String getNoOfCompOffDays() {
        return NoOfCompOffDays;
    }

    public void setNoOfCompOffDays(String noOfCompOffDays) {
        NoOfCompOffDays = noOfCompOffDays;
    }

    public String getFinalApplicationStatus() {
        return FinalApplicationStatus;
    }

    public void setFinalApplicationStatus(String finalApplicationStatus) {
        FinalApplicationStatus = finalApplicationStatus;
    }

    public String getCompensatoryOffAppLevelDetailID() {
        return CompensatoryOffAppLevelDetailID;
    }

    public void setCompensatoryOffAppLevelDetailID(String compensatoryOffAppLevelDetailID) {
        CompensatoryOffAppLevelDetailID = compensatoryOffAppLevelDetailID;
    }

    public String getCompensatoryOffAppIDMaster() {
        return CompensatoryOffAppIDMaster;
    }

    public void setCompensatoryOffAppIDMaster(String compensatoryOffAppIDMaster) {
        CompensatoryOffAppIDMaster = compensatoryOffAppIDMaster;
    }
}
