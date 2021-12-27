package seedcommando.com.yashaswi.pojos.attendancedetails;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/29/2018.
 */

public class Data {
    @SerializedName("ShiftDate")
    private String ShiftDate;
    @SerializedName("InTime")
    @Nullable
    private String InTime;
    @SerializedName("OutTime")
    @Nullable
    private String OutTime;
    @SerializedName("ManHours")
    private String ManHours;
    @SerializedName("DayStatusCode")
    private String DayStatusCode;

    public String getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(String shiftDate) {
        ShiftDate = shiftDate;
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

    public String getManHours() {
        return ManHours;
    }

    public void setManHours(String manHours) {
        ManHours = manHours;
    }

    public String getDayStatusCode() {
        return DayStatusCode;
    }

    public void setDayStatusCode(String dayStatusCode) {
        DayStatusCode = dayStatusCode;
    }
}
