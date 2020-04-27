package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/1/9.
 */

public class PointListModel {
    private String name;
    private long id;
    private long people;
    private long staffNum;

    public long getPeople() {
        return people;
    }

    public void setPeople(long people) {
        this.people = people;
    }

    public long getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(long staffNum) {
        this.staffNum = staffNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
