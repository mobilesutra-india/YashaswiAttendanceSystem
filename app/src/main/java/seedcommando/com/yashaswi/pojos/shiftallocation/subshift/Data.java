package seedcommando.com.yashaswi.pojos.shiftallocation.subshift;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 7/16/2018.
 */

public class Data {
    @SerializedName("EmployeeID")
    private String EmployeeID;
    @SerializedName("BranchName")
    private String BranchName;
    @SerializedName("SubDepartmentName")
    private String SubDepartmentName;
    @SerializedName("DivisionName")
    private String DivisionName;
    @SerializedName("Shift")
    private String Shift;
    @SerializedName("SectionName")
    private String SectionName;
    @SerializedName("SubsidiaryName")
    private String SubsidiaryName;
    @SerializedName("EmployeeCode")
    private String EmployeeCode;
    @SerializedName("SrNo")
    private String SrNo;
    @SerializedName("DepartmentName")
    private String DepartmentName;
    @SerializedName("EmployeeName")
    private String EmployeeName;
    @SerializedName("ShiftDate")
    private String ShiftDate;


    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getSubDepartmentName() {
        return SubDepartmentName;
    }

    public void setSubDepartmentName(String subDepartmentName) {
        SubDepartmentName = subDepartmentName;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String divisionName) {
        DivisionName = divisionName;
    }

    public String getShift() {
        return Shift;
    }

    public void setShift(String shift) {
        Shift = shift;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public String getSubsidiaryName() {
        return SubsidiaryName;
    }

    public void setSubsidiaryName(String subsidiaryName) {
        SubsidiaryName = subsidiaryName;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

    public String getSrNo() {
        return SrNo;
    }

    public void setSrNo(String srNo) {
        SrNo = srNo;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(String shiftDate) {
        ShiftDate = shiftDate;
    }
}
