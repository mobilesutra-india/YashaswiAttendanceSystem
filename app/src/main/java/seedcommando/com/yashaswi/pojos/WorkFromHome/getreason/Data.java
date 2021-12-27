package seedcommando.com.yashaswi.pojos.WorkFromHome.getreason;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/19/2018.
 */

public class Data {



   @SerializedName("ReasonTitle")
    private String ReasonTitle;
     @SerializedName("ReasonID")
    private String ReasonID;

    public String getReasonTitle() {
        return ReasonTitle;
    }

    public void setReasonTitle(String reasonTitle) {
        ReasonTitle = reasonTitle;
    }

    public String getReasonID() {
        return ReasonID;
    }

    public void setReasonID(String reasonID) {
        ReasonID = reasonID;
    }
}
