package seedcommando.com.yashaswi.pojos.regularizationpoJo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/20/2018.
 */

public class Regularization {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

