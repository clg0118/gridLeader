package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/1/11.
 */

public class PercentModel {
    private long allTotal;
    private long monthCount;
    private long addMonthCount;
    private long yearCount;
    private long addYearCount;
    private double percentOfYear;
    private double percentOfTotal;

    public long getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(long allTotal) {
        this.allTotal = allTotal;
    }

    public long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(long monthCount) {
        this.monthCount = monthCount;
    }

    public long getAddMonthCount() {
        return addMonthCount;
    }

    public void setAddMonthCount(long addMonthCount) {
        this.addMonthCount = addMonthCount;
    }

    public long getYearCount() {
        return yearCount;
    }

    public void setYearCount(long yearCount) {
        this.yearCount = yearCount;
    }

    public long getAddYearCount() {
        return addYearCount;
    }

    public void setAddYearCount(long addYearCount) {
        this.addYearCount = addYearCount;
    }

    public double getPercentOfYear() {
        return percentOfYear;
    }

    public void setPercentOfYear(double percentOfYear) {
        this.percentOfYear = percentOfYear;
    }

    public double getPercentOfTotal() {
        return percentOfTotal;
    }

    public void setPercentOfTotal(double percentOfTotal) {
        this.percentOfTotal = percentOfTotal;
    }
}
