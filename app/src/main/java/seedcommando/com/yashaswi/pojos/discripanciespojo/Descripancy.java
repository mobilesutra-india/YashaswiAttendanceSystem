package seedcommando.com.yashaswi.pojos.discripanciespojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by commando4 on 3/28/2018.
 */

public class Descripancy {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    private ArrayList<Data> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
