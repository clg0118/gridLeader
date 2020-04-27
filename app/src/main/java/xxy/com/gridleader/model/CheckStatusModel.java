package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/3/9.
 */

public class CheckStatusModel {
    private int checkStatus;
    private long actualCheckId;
    private String checkStartTimeStr;

    public String getCheckStartTimeStr() {
        return checkStartTimeStr;
    }

    public void setCheckStartTimeStr(String checkStartTimeStr) {
        this.checkStartTimeStr = checkStartTimeStr;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public long getActualCheckId() {
        return actualCheckId;
    }

    public void setActualCheckId(long actualCheckId) {
        this.actualCheckId = actualCheckId;
    }
}
