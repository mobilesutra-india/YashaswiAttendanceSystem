package seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by commando4 on 4/3/2018.
 */

public class CompOffDetails {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    private ArrayList<seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails.Data> data;

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

    public ArrayList<seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails.Data> getData() {
        return data;
    }

    public void setData(ArrayList<seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails.Data> data) {
        this.data = data;
    }
}
