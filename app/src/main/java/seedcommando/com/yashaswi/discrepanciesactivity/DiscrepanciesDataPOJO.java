package seedcommando.com.yashaswi.discrepanciesactivity;

/**
 * Created by commando1 on 9/1/2017.
 */

public class DiscrepanciesDataPOJO {
    String Date,Day,Status;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "DiscrepanciesDataPOJO{" +
                "Date='" + Date + '\'' +
                ", Day='" + Day + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}
