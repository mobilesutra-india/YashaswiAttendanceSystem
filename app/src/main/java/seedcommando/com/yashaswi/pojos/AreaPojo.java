package seedcommando.com.yashaswi.pojos;


import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class AreaPojo {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<AreaData> data;


    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public ArrayList<AreaData> getData() {
        return data;
    }

    public void setData(ArrayList<AreaData> data) {
        this.data = data;
    }
}
