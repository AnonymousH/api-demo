package com.palmaplus.nagrand.api_demo.hospital.databean;

import java.util.List;

/**
 * Created by Administrator on 2018-5-29.
 */

public class CategotyBean  {

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private String category;

    private List<String> department;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }
}
