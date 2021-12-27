package seedcommando.com.yashaswi.pojos.regularizationpoJo.realtimedata;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



/**
 * Created by commando4 on 4/7/2018.
 */

public class RealTime {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    private ArrayList<seedcommando.com.yashaswi.pojos.regularizationpoJo.realtimedata.Data> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
