package seedcommando.com.yashaswi.applicationstatus;

/**
 * Created by commando1 on 9/20/2017.
 */

public class ApplicationStatusPOJO {

    String leavetype,status,fromdate,todate ,LeaveAppID;

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getLeaveAppID() {
        return LeaveAppID;
    }

    public void setLeaveAppID(String leaveAppID) {
        LeaveAppID = leaveAppID;
    }

    @Override
    public String toString() {
        return "ApplicationStatusPOJO{" +
                "leavetype='" + leavetype + '\'' +
                ", status='" + status + '\'' +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                '}';
    }
}
