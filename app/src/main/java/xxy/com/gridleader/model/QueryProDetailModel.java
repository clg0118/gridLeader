package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by Betty Li on 2018/1/11.
 */

public class QueryProDetailModel {
    private String id;
    private String alarmNumber;
    private String lastAssist;
    private int status;
    private List<AlarmContentModel> alarmContent;
    private String alarmTimeStr;
    private String mlistFinishTimeStr;
    private List<ImgListModel> alarmImgList;
    private List<ImgListModel> confirmImgList;
    private List<ImgListModel> deptImgList;
    private String deptDealContent;


    public String getLastAssist() {
        return lastAssist;
    }

    public void setLastAssist(String lastAssist) {
        this.lastAssist = lastAssist;
    }

    public String getMlistFinishTimeStr() {
        return mlistFinishTimeStr;
    }

    public void setMlistFinishTimeStr(String mlistFinishTimeStr) {
        this.mlistFinishTimeStr = mlistFinishTimeStr;
    }

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

    public List<ImgListModel> getDeptImgList() {
        return deptImgList;
    }

    public void setDeptImgList(List<ImgListModel> deptImgList) {
        this.deptImgList = deptImgList;
    }

    public String getDeptDealContent() {
        return deptDealContent;
    }

    public void setDeptDealContent(String deptDealContent) {
        this.deptDealContent = deptDealContent;
    }
}
