package seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/3/2018.
 */

public class Data {
    @SerializedName("ShiftDate")
    private String ShiftDate;
    @SerializedName("ExpiredDate")
    private String ExpiredDate;
    @SerializedName("WorkingHours")
    private String WorkingHours;
    @SerializedName("AvailableUnit")
    private String AvailableUnit;
    @SerializedName("UsedCompOffDetails")
    private String UsedCompOffDetails;
    @SerializedName("EarnedUnit")
    private String EarnedUnit;
    @SerializedName("IsHrsOrDays")
    private String IsHrsOrDays;
    @SerializedName("CoompOffId")
    private String CoompOffId;
    @SerializedName("PendingAppDetails")
    private String PendingAppDetails;

    public String getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(String shiftDate) {
        ShiftDate = shiftDate;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    public String getWorkingHours() {
        return WorkingHours;
    }

    public void setWorkingHours(String workingHours) {
        WorkingHours = workingHours;
    }

    public String getAvailableUnit() {
        return AvailableUnit;
    }

    public void setAvailableUnit(String availableUnit) {
        AvailableUnit = availableUnit;
    }

    public String getUsedCompOffDetails() {
        return UsedCompOffDetails;
    }

    public void setUsedCompOffDetails(String usedCompOffDetails) {
        UsedCompOffDetails = usedCompOffDetails;
    }

    public String getEarnedUnit() {
        return EarnedUnit;
    }

    public void setEarnedUnit(String earnedUnit) {
        EarnedUnit = earnedUnit;
    }

    public String getIsHrsOrDays() {
        return IsHrsOrDays;
    }

    public void setIsHrsOrDays(String isHrsOrDays) {
        IsHrsOrDays = isHrsOrDays;
    }

    public String getCoompOffId() {
        return CoompOffId;
    }

    public void setCoompOffId(String coompOffId) {
        CoompOffId = coompOffId;
    }

    public String getPendingAppDetails() {
        return PendingAppDetails;
    }

    public void setPendingAppDetails(String pendingAppDetails) {
        PendingAppDetails = pendingAppDetails;
    }
}
