package seedcommando.com.yashaswi.pojos.regularizationpoJo.shift;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/29/2018.
 */

public class Data {
    @SerializedName("ShiftID")
    private String ShiftID;
    @SerializedName("ShiftName")
    private String ShiftName;

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
}
