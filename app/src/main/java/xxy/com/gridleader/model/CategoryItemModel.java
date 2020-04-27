package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/1/10.
 */

public class CategoryItemModel {
    private String name;
    private long value;
    private double percent;
    private long addValue;

    public long getAddValue() {
        return addValue;
    }

    public void setAddValue(long addValue) {
        this.addValue = addValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
