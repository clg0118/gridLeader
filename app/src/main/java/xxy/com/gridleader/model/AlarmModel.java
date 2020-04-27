package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class AlarmModel {
    private String id;
    private String alarmNumber;
    private int status;
    private List<AlarmContentModel> alarmContent;
    private String alarmTimeStr;
    private String jobNumber;
    private double longitude;
    private double latitude;
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public List<AlarmContentModel> getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(List<AlarmContentModel> alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAlarmTimeStr() {
        return alarmTimeStr;
    }

    public void setAlarmTimeStr(String alarmTimeStr) {
        this.alarmTimeStr = alarmTimeStr;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
