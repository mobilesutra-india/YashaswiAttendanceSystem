package seedcommando.com.yashaswi.pojos.shiftallocation.shifts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 8/16/2018.
 */

public class Data {
    @SerializedName("ShiftID")
    private String ShiftID;
    @SerializedName("ShiftName")
    private String ShiftName;
    @SerializedName("ShiftCode")
    private String ShiftCode;

    public String getShiftID() {
        return ShiftID;
    }

    public void setShiftID(String shiftID) {
        ShiftID = shiftID;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getShiftCode() {
        return ShiftCode;
    }

    public void setShiftCode(String shiftCode) {
        ShiftCode = shiftCode;
    }
}
