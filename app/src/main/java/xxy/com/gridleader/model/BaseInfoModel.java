package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class BaseInfoModel {
    private List<PointListModel> pointInfoList;
    private String message;

    public List<PointListModel> getPointInfoList() {
        return pointInfoList;
    }

    public void setPointInfoList(List<PointListModel> pointInfoList) {
        this.pointInfoList = pointInfoList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
