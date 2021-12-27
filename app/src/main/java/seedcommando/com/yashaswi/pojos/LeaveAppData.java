package seedcommando.com.yashaswi.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 2/5/2018.
 */

public class LeaveAppData {
    @SerializedName("leave_code")
   int leave_code;
    @SerializedName("leave_flag")
   int leave_flag;
    @SerializedName(" no_of_days")
  int   no_of_days;
    @SerializedName("from_date")
  String  from_date;
    @SerializedName("to_date")
   String to_date;
    @SerializedName("reason")
   String reason;
    @SerializedName("aprove_id")
   int aprove_id;
    @SerializedName("message")
    String message;
    @SerializedName("reponse_status")
    String reponse_status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReponse_status() {
        return reponse_status;
    }

    public void setReponse_status(String reponse_status) {
        this.reponse_status = reponse_status;
    }

    public int getLeave_code() {
        return leave_code;
    }

    public void setLeave_code(int leave_code) {
        this.leave_code = leave_code;
    }

    public int getLeave_flag() {
        return leave_flag;
    }

    public void setLeave_flag(int leave_flag) {
        this.leave_flag = leave_flag;
    }

    public int getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(int no_of_days) {
        this.no_of_days = no_of_days;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAprove_id() {
        return aprove_id;
    }

    public void setAprove_id(int aprove_id) {
        this.aprove_id = aprove_id;
    }
}
