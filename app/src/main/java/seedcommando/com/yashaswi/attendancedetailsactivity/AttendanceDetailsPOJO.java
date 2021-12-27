package seedcommando.com.yashaswi.attendancedetailsactivity;

/**
 * Created by commando1 on 9/1/2017.
 */

public class AttendanceDetailsPOJO {
    String Date,In,Out,Manhrs,Status;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIn() {
        return In;
    }

    public void setIn(String in) {
        In = in;
    }

    public String getOut() {
        return Out;
    }

    public void setOut(String out) {
        Out = out;
    }

    public String getManhrs() {
        return Manhrs;
    }

    public void setManhrs(String manhrs) {
        Manhrs = manhrs;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "AttendanceDetailsPOJO{" +
                "Date='" + Date + '\'' +
                ", In='" + In + '\'' +
                ", Out='" + Out + '\'' +
                ", Manhrs='" + Manhrs + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}
