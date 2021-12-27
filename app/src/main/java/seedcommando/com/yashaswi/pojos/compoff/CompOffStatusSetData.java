package seedcommando.com.yashaswi.pojos.compoff;

/**
 * Created by commando4 on 4/12/2018.
 */

public class CompOffStatusSetData {
    String ApplicationStatus;
    String TakenCompOffDate;
    String NoOfCompOffDays;
    String TakenAgainst;
    String ReasonDescription;
    String CompensatoryOffAppID;

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getTakenCompOffDate() {
        return TakenCompOffDate;
    }

    public void setTakenCompOffDate(String takenCompOffDate) {
        TakenCompOffDate = takenCompOffDate;
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

    public String getReasonDescription() {
        return ReasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        ReasonDescription = reasonDescription;
    }

    public String getCompensatoryOffAppID() {
        return CompensatoryOffAppID;
    }

    public void setCompensatoryOffAppID(String compensatoryOffAppID) {
        CompensatoryOffAppID = compensatoryOffAppID;
    }
}
