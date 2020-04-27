package xxy.com.gridleader.model;

/**
 * Created by XPS13 on 2018/11/14.
 */

public class MaintainResultModel {

    private long id;
    private String mlistNum;
    private int status;
    private String statusStr;
    private String updateDtStr;
    private String categoryName;
    private String content;
    private String location;
    private double longitude;
    private double latitude;
    private String pointName;
    private int difficult;
    private long alarmId;


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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getUpdateDtStr() {
        return updateDtStr;
    }

    public void setUpdateDtStr(String updateDtStr) {
        this.updateDtStr = updateDtStr;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }
}
