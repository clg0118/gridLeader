package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/1/9.
 */

public class TotalAlarmModel {
    private long allTotal;
    private long total;
    private long addtotal;
    private long unabs;
    private long inhand;
    private long assit;
    private long confirm;
    private long confi;
    private long addconfi;
    private long workOrder;
    private String message;

    public long getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(long workOrder) {
        this.workOrder = workOrder;
    }

    public long getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(long allTotal) {
        this.allTotal = allTotal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getAddtotal() {
        return addtotal;
    }

    public void setAddtotal(long addtotal) {
        this.addtotal = addtotal;
    }

    public long getUnabs() {
        return unabs;
    }

    public void setUnabs(long unabs) {
        this.unabs = unabs;
    }

    public long getInhand() {
        return inhand;
    }

    public void setInhand(long inhand) {
        this.inhand = inhand;
    }

    public long getAssit() {
        return assit;
    }

    public void setAssit(long assit) {
        this.assit = assit;
    }

    public long getConfirm() {
        return confirm;
    }

    public void setConfirm(long confirm) {
        this.confirm = confirm;
    }

    public long getConfi() {
        return confi;
    }

    public void setConfi(long confi) {
        this.confi = confi;
    }

    public long getAddconfi() {
        return addconfi;
    }

    public void setAddconfi(long addconfi) {
        this.addconfi = addconfi;
    }
}
