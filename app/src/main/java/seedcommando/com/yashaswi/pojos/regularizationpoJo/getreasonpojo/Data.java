package seedcommando.com.yashaswi.pojos.regularizationpoJo.getreasonpojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/20/2018.
 */

public class Data {


     @SerializedName("LeaveTypeID")
    private String LeaveTypeID;
    @SerializedName("ReasonTitle")
    private String ReasonTitle;
    @SerializedName("ReasonID")
    private String ReasonID;

    public String getLeaveTypeID() {
        return LeaveTypeID;
    }

    public void setLeaveTypeID(String leaveTypeID) {
        LeaveTypeID = leaveTypeID;
    }

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
