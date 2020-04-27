package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/3/12.
 */

public class ResultModel {
    private int resultId;
    private boolean resultValue;
    private ResidentQueryModel data;

    public ResidentQueryModel getData() {
        return data;
    }

    public void setData(ResidentQueryModel data) {
        this.data = data;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public boolean isResultValue() {
        return resultValue;
    }

    public void setResultValue(boolean resultValue) {
        this.resultValue = resultValue;
    }
}
