package seedcommando.com.yashaswi.pojos.shiftallocation.subshift;

import java.util.ArrayList;

/**
 * Created by commando4 on 7/16/2018.
 */

public class SubOrdinateShift {

    private String message;

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
