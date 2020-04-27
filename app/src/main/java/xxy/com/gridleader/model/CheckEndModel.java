package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/3/9.
 */

public class CheckEndModel {
    private String checkStartTimeStr;
    private String checkEndTimeStr;
    private String checkDuration;
    private String todayDuration;

    public String getCheckStartTimeStr() {
        return checkStartTimeStr;
    }

    public void setCheckStartTimeStr(String checkStartTimeStr) {
        this.checkStartTimeStr = checkStartTimeStr;
    }

    public String getCheckEndTimeStr() {
        return checkEndTimeStr;
    }

    public void setCheckEndTimeStr(String checkEndTimeStr) {
        this.checkEndTimeStr = checkEndTimeStr;
    }

    public String getCheckDuration() {
        return checkDuration;
    }

    public void setCheckDuration(String checkDuration) {
        this.checkDuration = checkDuration;
    }


    public String getTodayDuration() {
        return todayDuration;
    }

    public void setTodayDuration(String todayDuration) {
        this.todayDuration = todayDuration;
    }
}
