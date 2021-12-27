package seedcommando.com.yashaswi.pojos.aprovels.regularization.daystatus;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/17/2018.
 */

public class Data {
    @SerializedName("DayStatusID")
    private String DayStatusID;
    @SerializedName("DisplayCode")
    private String DisplayCode;

    public String getDayStatusID() {
        return DayStatusID;
    }

    public void setDayStatusID(String dayStatusID) {
        DayStatusID = dayStatusID;
    }

    public String getDisplayCode() {
        return DisplayCode;
    }

    public void setDisplayCode(String displayCode) {
        DisplayCode = displayCode;
    }
}
