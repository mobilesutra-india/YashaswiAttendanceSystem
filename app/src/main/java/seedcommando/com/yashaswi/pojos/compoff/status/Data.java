package seedcommando.com.yashaswi.pojos.compoff.status;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/12/2018.
 */

public class Data {
    @SerializedName("CompensatoryOffAppID")
    private String CompensatoryOffAppID;
    @SerializedName("TakenCompOffDate")
    private String TakenCompOffDate;
    @SerializedName("ReasonDescription")
    private String ReasonDescription;
    @SerializedName("NoOfCompOffDays")
    private String NoOfCompOffDays;
    @SerializedName("TakenAgainst")
    private String TakenAgainst;
    @SerializedName("ApplicationStatus")
    private String ApplicationStatus;

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

    public String getNoOfCompOffDays() {
        return NoOfCompOffDays;
    }

    public void setNoOfCompOffDays(String noOfCompOffDays) {
        NoOfCompOffDays = noOfCompOffDays;
    }

    public String getTakenAgainst() {
        return TakenAgainst;
    }

    public void setTakenAgainst(String takenAgainst) {
        TakenAgainst = takenAgainst;
    }

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getCompensatoryOffAppID() {
        return CompensatoryOffAppID;
    }

    public void setCompensatoryOffAppID(String compensatoryOffAppID) {
        CompensatoryOffAppID = compensatoryOffAppID;
    }
}
