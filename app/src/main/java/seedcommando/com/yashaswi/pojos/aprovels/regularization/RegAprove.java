package seedcommando.com.yashaswi.pojos.aprovels.regularization;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by commando4 on 4/12/2018.
 */

public class RegAprove {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    private ArrayList<Data> data;

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
