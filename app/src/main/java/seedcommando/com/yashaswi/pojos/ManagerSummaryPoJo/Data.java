package seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/7/2018.
 */

public class Data {
    @SerializedName("AvgHoursPerDay")
    private String AvgHoursPerDay;
    @SerializedName("ManHours")
    private String ManHours;
    @SerializedName("OverTime")
    private String OverTime;
    @SerializedName("WeekTitle")//WeekTitle
    private String WeekTitle;

    public String getAvgHoursPerDay ()
    {
        return AvgHoursPerDay;
    }

    public void setAvgHoursPerDay (String AvgHoursPerDay)
    {
        this.AvgHoursPerDay = AvgHoursPerDay;
    }

    public String getManHours ()
    {
        return ManHours;
    }

    public void setManHours (String ManHours)
    {
        this.ManHours = ManHours;
    }

    public String getOverTime ()
    {
        return OverTime;
    }

    public void setOverTime (String OverTime)
    {
        this.OverTime = OverTime;
    }

    public String getWeekTitle ()
    {
        return WeekTitle;
    }

    public void setWeekTitle (String WeekTitle)
    {
        this.WeekTitle = WeekTitle;
    }
}
