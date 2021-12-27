package seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by commando4 on 3/28/2018.
 */

public class RegStatus {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    private ArrayList<seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo.Data> data;


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

    public ArrayList<seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo.Data> getData() {
        return data;
    }

    public void setData(ArrayList<seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo.Data> data) {
        this.data = data;
    }
}
