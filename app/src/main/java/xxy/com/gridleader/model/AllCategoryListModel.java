package xxy.com.gridleader.model;

import java.util.List;

/**
 * Created by Betty Li on 2018/1/14.
 */

public class AllCategoryListModel {
    private List<AllCategoryModel> allCategoryList;

    private List<SystemModuleModel> sysModuleList;

    public List<AllCategoryModel> getAllCategoryList() {
        return allCategoryList;
    }

    public void setAllCategoryList(List<AllCategoryModel> allCategoryList) {
        this.allCategoryList = allCategoryList;
    }

    public List<SystemModuleModel> getSysModuleList() {
        return sysModuleList;
    }

    public void setSysModuleList(List<SystemModuleModel> sysModuleList) {
        this.sysModuleList = sysModuleList;
    }
}
