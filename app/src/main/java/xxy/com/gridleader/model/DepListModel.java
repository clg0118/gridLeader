package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by Betty Li on 2018/1/13.
 */

public class DepListModel {
    private String departmentName;
    private List<ActorListModel> actorList;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<ActorListModel> getActorList() {
        return actorList;
    }

    public void setActorList(List<ActorListModel> actorList) {
        this.actorList = actorList;
    }
}
