package seedcommando.com.yashaswi.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 3/5/2018.
 */

public class Data {

    private Employee Employee;

    @SerializedName("DesignationName")
    String DesignationName;//BranchName
    @SerializedName("BranchName")
    String BranchName;
    @SerializedName("CategoryName")
    String CategoryName;
    @SerializedName("DepartmentName")
    String DepartmentName;

    public seedcommando.com.yashaswi.pojos.Employee getEmployee() {
        return Employee;
    }

    public void setEmployee(seedcommando.com.yashaswi.pojos.Employee employee) {
        Employee = employee;
    }

    public String getDesignationName() {
        return DesignationName;
    }

    public void setDesignationName(String designationName) {
        DesignationName = designationName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }
}
