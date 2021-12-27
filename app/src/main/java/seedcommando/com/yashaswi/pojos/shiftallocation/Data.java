package seedcommando.com.yashaswi.pojos.shiftallocation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 6/21/2018.
 */

public class Data {
    @SerializedName("ShiftDate")
    private String ShiftDate;
    @SerializedName("Shift")
    private String Shift;

    public String getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(String shiftDate) {
        ShiftDate = shiftDate;
    }

    public String getShift() {
        return Shift;
    }

    public void setShift(String shift) {
        Shift = shift;
    }
}
