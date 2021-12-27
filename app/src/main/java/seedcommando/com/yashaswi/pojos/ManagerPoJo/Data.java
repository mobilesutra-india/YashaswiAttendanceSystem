package seedcommando.com.yashaswi.pojos.ManagerPoJo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/5/2018.
 */

public class Data {
     @SerializedName("ShiftDate")
    private String ShiftDate;
    @SerializedName("DayStatusCode")
    private String DayStatusCode;

    public String getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(String shiftDate) {
        ShiftDate = shiftDate;
    }

    public String getDayStatusCode() {
        return DayStatusCode;
    }

    public void setDayStatusCode(String dayStatusCode) {
        DayStatusCode = dayStatusCode;
    }
}
