package seedcommando.com.yashaswi.rest;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import seedcommando.com.yashaswi.pojos.AreaPojo;
import seedcommando.com.yashaswi.pojos.ChangePwdPOJO;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.ManagerPoJo.ClaenderPoJo;
import seedcommando.com.yashaswi.pojos.LoginPOJO;
import seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SubOrdinateSummaryPojo.SubOrdinatePoJo;
import seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SummaryPoJo;
import seedcommando.com.yashaswi.pojos.Userlist;
import seedcommando.com.yashaswi.pojos.WorkFromHome.FinalResponsePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.status.WFHStatus;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomePoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.getreason.ReasonPoJo;
import seedcommando.com.yashaswi.pojos.aprovels.compoff.CompoffAprovel;
import seedcommando.com.yashaswi.pojos.aprovels.leave.LeaveAprove;
import seedcommando.com.yashaswi.pojos.aprovels.od.ODAprovel;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.RegAprove;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.daystatus.DayStatusReg;
import seedcommando.com.yashaswi.pojos.aprovels.wfh.WFHAprovel;
import seedcommando.com.yashaswi.pojos.attendancedetails.AttendanceDetails;
import seedcommando.com.yashaswi.pojos.compoff.compoffagainstdetails.CompOffDetails;
import seedcommando.com.yashaswi.pojos.compoff.status.CompOffStatus;
import seedcommando.com.yashaswi.pojos.config.EmployeeConfig;
import seedcommando.com.yashaswi.pojos.discripanciespojo.Descripancy;

import seedcommando.com.yashaswi.pojos.documentpojo.fileUpload;
import seedcommando.com.yashaswi.pojos.documentpojo.getConfigData;
import seedcommando.com.yashaswi.pojos.leavepojo.Leave;
import seedcommando.com.yashaswi.pojos.leavepojo.leavetypecode.LeaveCode;
import seedcommando.com.yashaswi.pojos.leavepojo.statuspojo.Status;
import seedcommando.com.yashaswi.pojos.loginpojo.generateotp.GenerateOTP;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.Regularization;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.getreasonpojo.Reason;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.realtimedata.RealTime;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.shift.ShiftType;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo.RegStatus;
import seedcommando.com.yashaswi.pojos.shiftallocation.MyShift;
import seedcommando.com.yashaswi.pojos.shiftallocation.shifts.ShiftsData;
import seedcommando.com.yashaswi.pojos.shiftallocation.subshift.SubOrdinateShift;

/**
 * Created by commando4 on 1/2/2018.
 */

public interface ApiInterface {
    //Enterprise_MobileAppService //Empower_License
    /*@FormUrlEncoded
    @POST("/Empower_License/api/User/Registration")
    Call<RegistrationDetails> SendRegistrationData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_License/api/User/VerifyLicenseNumber")
    Call<LicensePOJO> SendLicenseRegData(@FieldMap Map<String, String> fields);
    //Enterprise_MobileApp
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApi/UserLogin")
    Call<LoginPOJO> SendLoginData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/DayStatusForAMonthCalender")
    Call<ClaenderPoJo> getCalenderData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetEmployeeHoursWeekWise")
    Call<SummaryPoJo> getSummaryData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApi/AddMobileSwipes")
    Call<CommanResponsePojo> AddMobileSwipesData(@FieldMap Map<String, String> fields);

    //GetWorkFlowForWorkFromHomeApp
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetWorkFlowForWorkFromHomeApp")
    Call<FinalResponsePoJo> GetWorkFlowForWorkFromHomeApp(@FieldMap Map<String, String> fields);

    //WorkFromHomeApp
    //WorkFromHomeAppAprovel
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApplyWorkFromHomeApp")
    Call<WorkFromHomePoJo> WorkFromHomeApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetWorkFromHomeApplicationList")
    Call<WFHStatus> WorkFromHomeAppStatus(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetLeaveBalance")
    Call<JsonObject> getLeaveBalanceData(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetNoOfWorkFromHomeDays")
    Call<CommanResponsePojo> getNoOfDayWorkFromHomeApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetReasonsForWorkFromHomeApp")
    Call<ReasonPoJo> getReasonWorkFromHomeApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetReasonsForRegularizationApp")
    Call<Reason> getReasonRegApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApplyRegularizationApp")
    Call<Regularization> RegApp(@FieldMap Map<String, String> fields);


    //GetWorkFlowForWorkFromHomeApp //getLeaveCodeData
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetWorkFlowForRegularizationApp")
    Call<FinalResponsePoJo> GetWorkFlowForRegularizationApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetLeaveTypeByEmployeeId")
    Call<LeaveCode> getLeaveCodeData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetReasonsAccordingToLeaveType")
    Call<seedcommando.com.empowerapp.pojos.leavepojo.reasonmaster.Reason> getLeaveReasonData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetWorkFlowForLeaveApp")
    Call<FinalResponsePoJo> getWorkFlowForLeave(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApplyLeaveApp")
    Call<Leave> LeaveApp(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("/empower5_1/app/Api/EmployeeApp/GetDiscrepanciesDetailList")
    Call<Descripancy> getDescripancies(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetLeaveApplicationList")
    Call<Status> getLeaveStatus(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetRegularizationApplicationList")
    Call<RegStatus> getRegStatus(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/empower5_1/app/Api/EmployeeApp/GetAttendanceDetails")
    Call<AttendanceDetails> getAttendanceDetails(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetNoOfLeaveDays")
    Call<CommanResponsePojo> getNoOfDaysLeave(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetShiftForRegularizeApplication")
    Call<ShiftType> getShiftType(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("Empower_Mobile/app/Api/EmployeeApp/GetWorkFlowForOutDutyApp")
    Call<FinalResponsePoJo> GetWorkFlowForOutDutyApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetNoOfOutDutyAppDays")
    Call<CommanResponsePojo> getNoOfDaysOutDuty(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApplyOutDutyApp")
    Call<CommanResponsePojo>OutDutyApplication(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetOutDutyApplicationList")
    Call<seedcommando.com.empowerapp.pojos.outdutypojo.Status> getOutDutyStatus(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("Empower_Mobile/app/Api/EmployeeApp/GetWorkFlowForCompensatoryOffApp")
    Call<FinalResponsePoJo> GetWorkFlowForCompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetReasonsForCompensatoryOffApp")
    Call<Reason> getReasonCompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/empower5_1/app/Api/EmployeeApp/GetEmployeeCompOffDetail")
    Call<CompOffDetails> getAgainstCompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetApplicationCount")
    Call<LoginPOJO> getPendingApprovals(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetRealInOutDateTimeForShiftDate")
    Call<RealTime> getRealTimeData(@FieldMap Map<String, String> fields);
    //@FormUrlEncoded
   // @POST("/Empower_Mobile/app/Api/EmployeeApp/GetLeaveApplicationForApproval")
   // Call<Status> getLeaveApplicationAprovel(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("/empower5_1/app/Api/EmployeeApp/GetCompOffAgainstDetails")
    Call<CommanResponsePojo> getCheckedCompoffDetails(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApplyCompensatoryOffApp")
    Call<CommanResponsePojo> CompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetCompensatoryOffApplicationList")
    Call<CompOffStatus> CompoffAppStatusList(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetLeaveApplicationForApproval")
    Call<LeaveAprove> LeaveAprovel(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetRegularizationApplicationForApproval")
    Call<RegAprove> RegAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetOutDutyApplicationForApproval")
    Call<ODAprovel> ODAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetWorkFromHomeApplicationForApproval")
    Call<WFHAprovel> WFHAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApproveOrRejectLeaveApp")
    Call<CommanResponsePojo> SendLeaveAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApproveOrRejectOutDutyApp")
    Call<CommanResponsePojo> SendODAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApproveOrRejectWorkFromHomeApp")
    Call<CommanResponsePojo> SendWFHAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApproveOrRejectCompensatoryOffApp")
    Call<CommanResponsePojo> SendCompOffAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetCompensatoryOffApplicationForApproval")
    Call<CompoffAprovel> CompOffAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetDayStatusForRegularizationApp")
    Call<DayStatusReg> DayStatusReg1(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/ApproveOrRejectRegularizationApp")
    Call<CommanResponsePojo> SendRegAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Empower_Mobile/app/Api/EmployeeApp/GetDateTimeDifferenceInHoursMinForMultipleDays")
    Call<CommanResponsePojo> WFHMulDayInHrs(@FieldMap Map<String, String> fields);*/
    //cloude

   /* @FormUrlEncoded
    @POST("/Enterprise_MobileAppService/api/User/Registration")
    Call<RegistrationDetails> SendRegistrationData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("/Enterprise_MobileAppService/api/User/VerifyLicenseNumber")
    Call<LicensePOJO> SendLicenseRegData(@FieldMap Map<String, String> fields);*/
    //Enterprise_MobileApp
    @FormUrlEncoded
    @POST("EmployeeApi/UserLogin")
    Call<LoginPOJO> SendLoginData(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApi/GenerateOTPForMobile")
    Call<GenerateOTP> generateOTP(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApi/UserLoginWithOTP")
    Call<LoginPOJO> SendLoginOTPData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/DayStatusForAMonthCalender")
    Call<ClaenderPoJo> getCalenderData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetEmployeeHoursWeekWise")
    Call<SummaryPoJo> getSummaryData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApi/AddMobileSwipes")
    Call<CommanResponsePojo> AddMobileSwipesData(@FieldMap Map<String, String> fields);

    //GetWorkFlowForWorkFromHomeApp
    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFlowForWorkFromHomeApp")
    Call<FinalResponsePoJo> GetWorkFlowForWorkFromHomeApp(@FieldMap Map<String, String> fields);

    //WorkFromHomeApp
    //WorkFromHomeAppAprovel
    @FormUrlEncoded
    @POST("EmployeeApp/ApplyWorkFromHomeApp")
    Call<WorkFromHomePoJo> WorkFromHomeApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFromHomeApplicationList")
    Call<WFHStatus> WorkFromHomeAppStatus(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetLeaveBalance")
    Call<JsonObject> getLeaveBalanceData(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApp/GetNoOfWorkFromHomeDays")
    Call<CommanResponsePojo> getNoOfDayWorkFromHomeApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetReasonsForWorkFromHomeApp")
    Call<ReasonPoJo> getReasonWorkFromHomeApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetReasonsForRegularizationApp")
    Call<Reason> getReasonRegApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApplyRegularizationApp")
    Call<Regularization> RegApp(@FieldMap Map<String, String> fields);


    //GetWorkFlowForWorkFromHomeApp //getLeaveCodeData
    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFlowForRegularizationApp")
    Call<FinalResponsePoJo> GetWorkFlowForRegularizationApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetLeaveTypeByEmployeeId")
    Call<LeaveCode> getLeaveCodeData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetReasonsAccordingToLeaveType")
    Call<seedcommando.com.yashaswi.pojos.leavepojo.reasonmaster.Reason> getLeaveReasonData(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFlowForLeaveApp")
    Call<FinalResponsePoJo> getWorkFlowForLeave(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApplyLeaveApp")
    Call<Leave> LeaveApp(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetDiscrepanciesDetailList")
    Call<Descripancy> getDescripancies(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetLeaveApplicationList")
    Call<Status> getLeaveStatus(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetRegularizationApplicationList")
    Call<RegStatus> getRegStatus(@FieldMap Map<String, String> fields);



    @FormUrlEncoded
    @POST("EmployeeApp/GetAttendanceDetails")
    Call<AttendanceDetails> getAttendanceDetails(@FieldMap Map<String, String> fields);

 @FormUrlEncoded
 @POST("EmployeeApp/GetSubordinates")
 Call<Userlist> GetSubordinates(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetNoOfLeaveDays")
    Call<CommanResponsePojo> getNoOfDaysLeave(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetShiftForRegularizeApplication")
    Call<ShiftType> getShiftType(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFlowForOutDutyApp")
    Call<FinalResponsePoJo> GetWorkFlowForOutDutyApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetNoOfOutDutyAppDays")
    Call<CommanResponsePojo> getNoOfDaysOutDuty(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApplyOutDutyApp")
    Call<CommanResponsePojo>OutDutyApplication(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetOutDutyApplicationList")
    Call<seedcommando.com.yashaswi.pojos.outdutypojo.Status> getOutDutyStatus(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFlowForCompensatoryOffApp")
    Call<FinalResponsePoJo> GetWorkFlowForCompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetReasonsForCompensatoryOffApp")
    Call<Reason> getReasonCompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetEmployeeCompOffDetail")
    Call<CompOffDetails> getAgainstCompoffApp(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApp/GetApplicationCount")
    Call<CommanResponsePojo> getPendingApprovals(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetRealInOutDateTimeForShiftDate")
    Call<RealTime> getRealTimeData(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetLeaveApplicationForApproval")
    Call<Status> getLeaveApplicationAprovel(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetCompOffAgainstDetails")
    Call<CommanResponsePojo> getCheckedCompoffDetails(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApplyCompensatoryOffApp")
    Call<CommanResponsePojo> CompoffApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetCompensatoryOffApplicationList")
    Call<CompOffStatus> CompoffAppStatusList(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetLeaveApplicationForApproval")
    Call<LeaveAprove> LeaveAprovel(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApp/GetRegularizationApplicationForApproval")
    Call<RegAprove> RegAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetOutDutyApplicationForApproval")
    Call<ODAprovel> ODAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetWorkFromHomeApplicationForApproval")
    Call<WFHAprovel> WFHAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApproveOrRejectLeaveApp")
    Call<CommanResponsePojo> SendLeaveAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApproveOrRejectOutDutyApp")
    Call<CommanResponsePojo> SendODAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApproveOrRejectWorkFromHomeApp")
    Call<CommanResponsePojo> SendWFHAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApproveOrRejectCompensatoryOffApp")
    Call<CommanResponsePojo> SendCompOffAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetCompensatoryOffApplicationForApproval")
    Call<CompoffAprovel> CompOffAprovel(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetDayStatusForRegularizationApp")
    Call<DayStatusReg> DayStatusReg1(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/ApproveOrRejectRegularizationApp")
    Call<CommanResponsePojo> SendRegAprovel(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetDateTimeDifferenceInHoursMinForMultipleDays")
    Call<CommanResponsePojo> WFHMulDayInHrs(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/GetMobileApplicationConfiguration")
    Call<EmployeeConfig> getConfigData(@FieldMap Map<String, String> fields);
    //http://192.168.1.54/Empower_Mobile/app/Api/EmployeeApp/GetReasonsForOutDutyApp
    @FormUrlEncoded
    @POST("EmployeeApp/GetReasonsForOutDutyApp")
    Call<ReasonPoJo> getReasonODApp(@FieldMap Map<String, String> fields);
    @FormUrlEncoded
    @POST("EmployeeApp/GetAttendanceDetailsForSubordinate")
    Call<SubOrdinatePoJo> getSubOrdinateSummaryData(@FieldMap Map<String, String> fields);

 @FormUrlEncoded
 @POST("EmployeeApp/GetEmployeeAllocatedShift")
 Call<MyShift> getShiftAllocationData(@FieldMap Map<String, String> fields);

 @FormUrlEncoded
 @POST("EmployeeApp/GetAllocatedShiftDetailsForSubordinate")
 Call<SubOrdinateShift> getSubOrdinateShiftData(@FieldMap Map<String, String> fields);
 @FormUrlEncoded
 @POST("EmployeeApp/GetShifts")
 Call<ShiftsData> getShiftData(@FieldMap Map<String, String> fields);
 @FormUrlEncoded
 @POST("EmployeeApp/AllocateShift")
 Call<ShiftsData> allocateShift(@FieldMap Map<String, String> fields);

 @FormUrlEncoded
 @POST("EmployeeApp/AllocateShift")
 Call<CommanResponsePojo> shiftAllocate(@FieldMap Map<String, String> fields);


 @FormUrlEncoded
 @POST("EmployeeApp/GetLeavePolicyDetail")
 Call<getConfigData> getLeaveConfigData(@FieldMap Map<String, String> fields);

 @Multipart
 @POST("EmployeeApp/UploadFile")
 Call<fileUpload> uploadLeaveDoc(@Part MultipartBody.Part file);


 @FormUrlEncoded
 @POST("EmployeeApp/GetLeavePolicyDetail")
 Call<getConfigData> getLeaveConfigData1(@FieldMap Map<String, String> fields);

 @FormUrlEncoded
 @POST("EmployeeApi/ChangePassword")

 Call<ChangePwdPOJO> UpdatePassword(@FieldMap Map<String, String> fields);

 @FormUrlEncoded
 @POST("EmployeeApi/ForgotPassword")
 Call<ChangePwdPOJO> ForgotPassword(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApp/DeleteRegularizationApp")
    Call<RegStatus> DeleteRegularizationApp(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("EmployeeApp/DeleteWorkFromHomeApp")
    Call<WFHStatus> DeleteWorkFromHomeApp(@FieldMap Map<String, String> fields);

//DeleteLeaveApp
    @FormUrlEncoded
    @POST("EmployeeApp/DeleteLeaveApp")
    Call<Status> DeleteLeaveApp(@FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("EmployeeApp/DeleteCompensatoryOffApp")
    Call<CompOffStatus> DeleteCompensatoryOffApp(@FieldMap Map<String, String> fields);


    //DeleteOutDutyApp
    @FormUrlEncoded
    @POST("EmployeeApp/DeleteOutDutyApp")
    Call<Status> DeleteOutDutyApp(@FieldMap Map<String, String> fields);



    @FormUrlEncoded
    @POST("EmployeeApp/GetEmployeeAreaCoordinates")
    Call<AreaPojo> getEmployeeAreaConfig(@FieldMap Map<String, String> fields);

}

