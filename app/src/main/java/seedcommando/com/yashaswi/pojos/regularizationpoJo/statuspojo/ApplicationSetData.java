package seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo;

/**
 * Created by commando4 on 3/28/2018.
 */

public class ApplicationSetData {
    private String fromDate;
    private String toDate;
    private String fromtime;
    private String totime;
    private String shift;
    private String reason;
    private String status;
    private String  RegularizationAppId;
    private  String AppFromEmployeeId;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegularizationAppId() {
        return RegularizationAppId;
    }

    public void setRegularizationAppId(String regularizationAppId) {
        RegularizationAppId = regularizationAppId;
    }

    public String getAppFromEmployeeId() {
        return AppFromEmployeeId;
    }

    public void setAppFromEmployeeId(String appFromEmployeeId) {
        AppFromEmployeeId = appFromEmployeeId;
    }
}
