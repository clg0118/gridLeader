package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/1/12.
 */

public class DealModel {
    private long id;
    private String mlistNum;
    private String pointName;
    private String content;
    private String remark;
    private int status;
    private String sysName;
    private long categoryId;
    private long alarmId;
    private String location;
    private String alarmTimeStr;
    private String actUserName;
    private String JobNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMlistNum() {
        return mlistNum;
    }

    public void setMlistNum(String mlistNum) {
        this.mlistNum = mlistNum;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAlarmTimeStr() {
        return alarmTimeStr;
    }

    public void setAlarmTimeStr(String alarmTimeStr) {
        this.alarmTimeStr = alarmTimeStr;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    public String getJobNumber() {
        return JobNumber;
    }

    public void setJobNumber(String jobNumber) {
        JobNumber = jobNumber;
    }
}
