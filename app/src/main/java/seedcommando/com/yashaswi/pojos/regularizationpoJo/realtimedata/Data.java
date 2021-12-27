package seedcommando.com.yashaswi.pojos.regularizationpoJo.realtimedata;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 4/7/2018.
 */

public class Data {
    @SerializedName("Intime")
    private String Intime;
    @SerializedName("OutTime")
    private String OutTime;

    public String getIntime() {
        return Intime;
    }

    public void setIntime(String intime) {
        Intime = intime;
    }

    public String getOutTime() {
        return   OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
       // ManageNUllValue(outTime)
    }
   /* public String ManageNUllValue(Object src)
    {
        if (src!= null)
             return src.toString();
        else
            return "";
    }*/
    }
