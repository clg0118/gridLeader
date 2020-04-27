package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by XPS13 on 2018/11/13.
 */

public class CheckListResultModel {

    private boolean result;

    private String message;

    private List<CheckItemResultModel> resultData;


    public List<CheckItemResultModel> getResultData() {
        return resultData;
    }

    public void setResultData(List<CheckItemResultModel> resultData) {
        this.resultData = resultData;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
