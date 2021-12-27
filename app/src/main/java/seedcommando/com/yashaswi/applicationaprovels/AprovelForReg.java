package seedcommando.com.yashaswi.applicationaprovels;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.RegAprove;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.RegAprovelSetData;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.daystatus.DayStatusReg;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 4/11/2018.
 */

public class AprovelForReg extends Fragment {

    ArrayList<RegAprovelSetData> arrayList;
    ListView listView;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;
   static ArrayList<String> daystatus=null;
  static   ArrayList<String> daystatusId=null;

    public AprovelForReg() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        listView = rootView.findViewById(R.id.list_notification);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        //Log.e("data", MainActivity.arrayList.toString());
        //setUpExpList();

        if(ApplicationAprovelsActivity.position==1) {
            if (Utilities.isNetworkAvailable(this.getContext())) {
               // RegAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                DayStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }


        return rootView;
    }
    private void setUpExpList() {

        //listDataMembers= new HashMap<String, ArrayList<WeekReportPojo>>();
        // Adding province names and number of population as groups

        /*arrayList = new ArrayList<NotificationPOJO>();
        NotificationPOJO notificationPOJO = new NotificationPOJO();
        notificationPOJO.setLeavetype("PL");
        notificationPOJO.setTodate("21 MAY 17");
        notificationPOJO.setFromdate("21 MAY 17");
        notificationPOJO.setTime("10:00 AM");
        notificationPOJO.setBalance("03");
        notificationPOJO.setReason("Personal Work");
        notificationPOJO.setRemark("NA");

        arrayList.add(notificationPOJO);

        NotificationPOJO notificationPOJO1 = new NotificationPOJO();
        notificationPOJO1.setLeavetype("SL");
        notificationPOJO1.setTodate("24 MAY 17");
        notificationPOJO1.setFromdate("21 MAY 17");
        notificationPOJO1.setTime("09:00 PM");
        notificationPOJO1.setBalance("09");
        notificationPOJO1.setReason("Work");
        notificationPOJO1.setRemark("NA");

        arrayList.add(notificationPOJO1);*/
    }

    public void RegAppAprovals(String id,String token) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> aprovelFlow=new HashMap<String,String>();


        aprovelFlow.put("employeeID",id);
        aprovelFlow.put("token",token);

        arrayList = new ArrayList<RegAprovelSetData>();

        Call<RegAprove> call = apiService.RegAprovel(aprovelFlow);
        call.enqueue(new Callback<RegAprove>() {
            @Override
            public void onResponse(retrofit2.Call<RegAprove> call, Response<RegAprove> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            RegAprovelSetData leaveAprovelSetData = new  RegAprovelSetData();
                            leaveAprovelSetData.setInTime(response.body().getData().get(i).getInTime());
                            leaveAprovelSetData.setStatus(response.body().getData().get(i).getFinalApplicationStatus());
                            leaveAprovelSetData.setFname(response.body().getData().get(i).getFirstName());
                            leaveAprovelSetData.setLname(response.body().getData().get(i).getLastName());
                            leaveAprovelSetData.setLevel(response.body().getData().get(i).getLevelNo());
                            leaveAprovelSetData.setOutTime(response.body().getData().get(i).getOutTime());
                            leaveAprovelSetData.setReason(response.body().getData().get(i).getReasonTitle());
                            leaveAprovelSetData.setMaxlevel(response.body().getData().get(i).getMaxLevelCount());
                            leaveAprovelSetData.setRegularizationAppIDMaster(response.body().getData().get(i).getRegularizationAppIDMaster());
                            leaveAprovelSetData.setRegularizationAppLevelDetailID(response.body().getData().get(i).getRegularizationAppLevelDetailID());
                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                Date date = format.parse(response.body().getData().get(i).getShiftDateFrom());
                                Date date1 = format.parse(response.body().getData().get(i).getShiftDateTo());
                                leaveAprovelSetData.setShiftDateFrom(format1.format(date));
                                leaveAprovelSetData.setShiftDateTo(format1.format(date1));


                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            arrayList.add(leaveAprovelSetData);



                        }
                        RegAprovelAdapter adapter = new RegAprovelAdapter(getActivity(), arrayList,daystatus,daystatusId,AprovelForReg.this);
                        listView.setAdapter(adapter);
                    }else {
                        RegAprovelAdapter adapter = new RegAprovelAdapter(getActivity(), arrayList,daystatus,daystatusId,AprovelForReg.this);
                        listView.setAdapter(adapter);
                        EmpowerApplication.alertdialog(response.body().getMessage(), getContext());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getContext());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<RegAprove> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), getContext());


            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.isVisible()){
        if (isVisibleToUser) {
            //&& isFragmentLoaded
            // Load your data here or do network operations here


            if (Utilities.isNetworkAvailable(this.getContext())) {
               // RegAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                DayStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));


                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        isFragmentLoaded=true;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentLoaded=false;



    }


    @Override
    public void onStop() {
        super.onStop();
        isFragmentLoaded=false;

    }


    public void DayStatus(String employeeId) {

        // pd = new ProgressDialog(Attend_Regularization.this);
        // pd.setMessage("Loading....");
        // pd.show();

        Map<String,String> shiftdata=new HashMap<String,String>();
        shiftdata.put("employeeId",employeeId);


        daystatus=new ArrayList<>();
        daystatusId=new ArrayList<String>();

        retrofit2.Call<DayStatusReg> call = apiService.DayStatusReg1(shiftdata);
        call.enqueue(new Callback<DayStatusReg>() {
            @Override
            public void onResponse(retrofit2.Call<DayStatusReg> call, Response<DayStatusReg> response) {

                daystatus.clear();
                daystatusId.clear();

                if (response.isSuccessful()) {
                    // pd.dismiss();
                    Log.d("status ID1 respo: ", new Gson().toJson(response.body()));
                    daystatusId.add("0");
                    daystatus.add("Select");

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            daystatusId.add(response.body().getData().get(i).getDayStatusID());
                            daystatus.add(response.body().getData().get(i).getDisplayCode());
                        }

                        if(ApplicationAprovelsActivity.position==1) {
                            if (Utilities.isNetworkAvailable(getContext())) {
                                RegAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                               // DayStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                            } else {
                                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                            }
                        }



                    }else {

                        EmpowerApplication.alertdialog(response.body().getMessage(), getContext());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getContext());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getContext());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<DayStatusReg> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                //pd.dismiss();

                EmpowerApplication.alertdialog(t.getMessage(),getContext());

            }
        });
    }

}
