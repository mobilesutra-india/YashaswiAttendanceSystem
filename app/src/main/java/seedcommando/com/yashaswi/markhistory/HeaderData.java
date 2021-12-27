package seedcommando.com.yashaswi.markhistory;

/**
 * Created by commando1 on 8/8/2017.
 */

public class HeaderData {

    String date;
    String punch;
    String status;

   /* public HeaderData(String date, String punch) {
        this.date = date;
        this.punch = punch;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPunch() {
        return punch;
    }

    public void setPunch(String punch) {
        this.punch = punch;
    }

    @Override
    public String toString() {
        return "HeaderData{" +
                "date='" + date + '\'' +
                ", punch='" + punch + '\'' +
                '}';
    }
}
