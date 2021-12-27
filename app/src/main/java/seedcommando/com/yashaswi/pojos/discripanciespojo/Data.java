package seedcommando.com.yashaswi.pojos.discripanciespojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/28/2018.
 */

public class Data {
    @SerializedName("ShiftDate")
    private String ShiftDate;
    @SerializedName("Day")
    private String Day;
    @SerializedName("DayStatusCode")
    private String DayStatusCode;

    public String getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(String shiftDate) {
        ShiftDate = shiftDate;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getDayStatusCode() {
        return DayStatusCode;
    }

    public void setDayStatusCode(String dayStatusCode) {
        DayStatusCode = dayStatusCode;
    }
}
