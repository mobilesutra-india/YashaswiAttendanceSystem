package seedcommando.com.yashaswi.notifications;

/**
 * Created by commando1 on 9/21/2017.
 */

public class NotificationPOJO {
    String leavetype,balance,reason,fromdate,todate,time,remark,todaydate;

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getTodaydate() {
        return todaydate;
    }

    public void setTodaydate(String todaydate) {
        this.todaydate = todaydate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "NotificationPOJO{" +
                "leavetype='" + leavetype + '\'' +
                ", balance='" + balance + '\'' +
                ", reason='" + reason + '\'' +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                ", time='" + time + '\'' +
                ", remark='" + remark + '\'' +
                ", todaydate='" + todaydate + '\'' +
                '}';
    }
}
