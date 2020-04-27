package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by Betty Li on 2018/1/9.
 */

public class LoginModel {
    private boolean resultValue;
    private UserModel user;
    private int roleId;
    private List<PointListModel> pointList;
    private String message;
    private String version;

    public boolean getResultValue() {
        return resultValue;
    }

    public void setResultValue(boolean resultValue) {
        this.resultValue = resultValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<PointListModel> getPointList() {
        return pointList;
    }

    public void setPointList(List<PointListModel> pointList) {
        this.pointList = pointList;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
