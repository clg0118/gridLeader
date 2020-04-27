package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/3/9.
 */

public class StartCheckModel {
    private long actualCheckId;
    private long configTime;
    private String checkStartTimeStr;

    public long getActualCheckId() {
        return actualCheckId;
    }

    public void setActualCheckId(long actualCheckId) {
        this.actualCheckId = actualCheckId;
    }

    public long getConfigTime() {
        return configTime;
    }

    public void setConfigTime(long configTime) {
        this.configTime = configTime;
    }

    public String getCheckStartTimeStr() {
        return checkStartTimeStr;
    }

    public void setCheckStartTimeStr(String checkStartTimeStr) {
        this.checkStartTimeStr = checkStartTimeStr;
    }
}
