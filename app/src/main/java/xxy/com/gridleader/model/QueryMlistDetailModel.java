package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by XPS13 on 2018/11/19.
 */

public class QueryMlistDetailModel {

    private String id;
    private String alarmNumber;
    private String lastAssist;
    private int status;
    private List<AlarmContentModel> categoryContent;
    private String alarmTimeStr;
    private String mlistFinishTimeStr;
    private List<ImgListModel> alarmImgList;
    private List<ImgListModel> confirmImgList;


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

    public String getLastAssist() {
        return lastAssist;
    }

    public void setLastAssist(String lastAssist) {
        this.lastAssist = lastAssist;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<AlarmContentModel> getCategoryContent() {
        return categoryContent;
    }

    public void setCategoryContent(List<AlarmContentModel> categoryContent) {
        this.categoryContent = categoryContent;
    }

    public String getAlarmTimeStr() {
        return alarmTimeStr;
    }

    public void setAlarmTimeStr(String alarmTimeStr) {
        this.alarmTimeStr = alarmTimeStr;
    }

    public String getMlistFinishTimeStr() {
        return mlistFinishTimeStr;
    }

    public void setMlistFinishTimeStr(String mlistFinishTimeStr) {
        this.mlistFinishTimeStr = mlistFinishTimeStr;
    }

    public List<ImgListModel> getAlarmImgList() {
        return alarmImgList;
    }

    public void setAlarmImgList(List<ImgListModel> alarmImgList) {
        this.alarmImgList = alarmImgList;
    }

    public List<ImgListModel> getConfirmImgList() {
        return confirmImgList;
    }

    public void setConfirmImgList(List<ImgListModel> confirmImgList) {
        this.confirmImgList = confirmImgList;
    }
}
