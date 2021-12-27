package seedcommando.com.yashaswi.pojos.aprovels.leave;

/**
 * Created by commando4 on 4/12/2018.
 */

public class LeaveAprovelSetData {
    @Override
    public String toString() {
        return "LeaveAprovelSetData{" +
                "leavetype='" + leavetype + '\'' +
                ", balance='" + balance + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", reason='" + reason + '\'' +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                ", level='" + level + '\'' +
                ", status='" + status + '\'' +
                ", maxlevel='" + maxlevel + '\'' +
                ", leaveAppIDMaster='" + leaveAppIDMaster + '\'' +
                ", leaveAppLevelDetailID='" + leaveAppLevelDetailID + '\'' +
                ", DocumentName='" + DocumentName + '\'' +
                '}';
    }

    String leavetype;
    String balance;
    String fname;
    String lname;
    String reason;
    String fromdate;
    String todate;
    String level;
    String status;
    String maxlevel;
    String leaveAppIDMaster;
    String leaveAppLevelDetailID;
    String DocumentName;

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaxlevel() {
        return maxlevel;
    }

    public void setMaxlevel(String maxlevel) {
        this.maxlevel = maxlevel;
    }

    public String getLeaveAppIDMaster() {
        return leaveAppIDMaster;
    }

    public void setLeaveAppIDMaster(String leaveAppIDMaster) {
        this.leaveAppIDMaster = leaveAppIDMaster;
    }

    public String getLeaveAppLevelDetailID() {
        return leaveAppLevelDetailID;
    }

    public void setLeaveAppLevelDetailID(String leaveAppLevelDetailID) {
        this.leaveAppLevelDetailID = leaveAppLevelDetailID;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }
}
