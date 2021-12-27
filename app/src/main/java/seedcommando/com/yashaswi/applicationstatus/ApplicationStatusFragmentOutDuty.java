package seedcommando.com.yashaswi.applicationstatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.Out_Duty_Application;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomeSetDataPoJo;
import seedcommando.com.yashaswi.pojos.outdutypojo.Status;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 10/13/2017.
 */

public class ApplicationStatusFragmentOutDuty extends Fragment {

    //ArrayList<ApplicationStatusPOJO> arrayList;
   public static ListView listView;
    Button leave_apply;
    ProgressDialog pd;
    private ApiInterface apiService;
    ArrayList<WorkFromHomeSetDataPoJo> arrayList;
    boolean isFragmentLoaded=false;

    public ApplicationStatusFragmentOutDuty () {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.application_status_fragment, container, false);
        listView = rootView.findViewById(R.id.list_allication_status);
        //leave_apply=(Button)rootView.findViewById(R.id.button_mark_Attendance);
        Log.e("onCreateView", "onCreateviewCalled");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.buttonforleavestatus,listView,false);
        leave_apply= footer.findViewById(R.id.button_mark_Attendance);
        leave_apply.setText("Apply New Out Duty");

        // So, this footer is non selectable
        listView.addFooterView(footer,null,false);
        //setUpExpList();
       // ApplicationStatusAdapter adapter = new ApplicationStatusAdapter(getActivity(), arrayList);
        //listView.setAdapter(adapter);
       // ApplicationStatusActivity.position==2
        //if(ApplicationStatusActivity.position==2) {
            Log.e("intwo22", "onCreateviewCalled");

       // if(this.isVisible()) {
      if( ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideOutDutyApp")){

            if (Utilities.isNetworkAvailable(getContext())) {
                Log.e("service called", "onCreateviewCalled");
                OutDutyStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
       }
       // }


        leave_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationStatusActivity.position== HomeFragment.key.indexOf("HideOutDutyApp")) {
                    startActivity(new Intent(getContext(), Out_Duty_Application.class));
                }
            }
        });
        return rootView;
    }
    private void setUpExpList() {

        //listDataMembers= new HashMap<String, ArrayList<WeekReportPojo>>();
        // Adding province names and number of population as groups

       /* arrayList = new ArrayList<ApplicationStatusPOJO>();
        ApplicationStatusPOJO applicationStatusPOJO = new ApplicationStatusPOJO();
        applicationStatusPOJO.setLeavetype("PL");
        applicationStatusPOJO.setFromdate("15 MAY 17");
        applicationStatusPOJO.setTodate("15 MAY 17");
        applicationStatusPOJO.setStatus("Pending");
        arrayList.add(applicationStatusPOJO);
        ApplicationStatusPOJO applicationStatusPOJO1 = new ApplicationStatusPOJO();
        applicationStatusPOJO1.setLeavetype("SL");
        applicationStatusPOJO1.setFromdate("15 MAY 17");
        applicationStatusPOJO1.setTodate("17 MAY 17");
        applicationStatusPOJO1.setStatus("Pending");
        arrayList.add(applicationStatusPOJO1);*/
    }

    public void OutDutyStatus(String id) {

        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading....");

        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();


        WorkFlowdataapp.put("employeeId",id);

        arrayList = new ArrayList<WorkFromHomeSetDataPoJo>();

        Call<Status> call = apiService.getOutDutyStatus(WorkFlowdataapp);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            WorkFromHomeSetDataPoJo workFromHomeSetDataPoJo=new WorkFromHomeSetDataPoJo();
                            workFromHomeSetDataPoJo.setDays(response.body().getData().get(i).getNoOfOutDutyDays());
                            workFromHomeSetDataPoJo.setOutDutyAppID(response.body().getData().get(i).getOutDutyAppID());

                            // Log.e("data added",response.body().getData().get(i).getNoOfWorkFromHomeDays());
                           // Log.e("data added",response.body().getData().get(i).getNoOfMinutes());
                            // if(response.body().getData().get(i).getFromTime().length()==0) {
                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            format2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

                            try {
                                Date date = format.parse(response.body().getData().get(i).getFromDate());
                                Date date1 = format.parse(response.body().getData().get(i).getToDate());
                                workFromHomeSetDataPoJo.setFromdate(format1.format(date));
                                workFromHomeSetDataPoJo.setTodate(format1.format(date1));

                                if(response.body().getData().get(i).getFromTime()!=null) {
                                    workFromHomeSetDataPoJo.setFromtime(response.body().getData().get(i).getFromTime());
                                }else {
                                    workFromHomeSetDataPoJo.setFromtime("");
                                }
                                if(response.body().getData().get(i).getFromTime()!=null) {
                                    workFromHomeSetDataPoJo.setTotime(response.body().getData().get(i).getToTime());
                                }else {
                                    workFromHomeSetDataPoJo.setTotime("");

                                }
                                if(!response.body().getData().get(i).getNoOfMinutes().isEmpty()) {
                                    workFromHomeSetDataPoJo.setHrs(String.valueOf(Long.parseLong(response.body().getData().get(i).getNoOfMinutes())/60)+":"+String.valueOf(Long.parseLong(response.body().getData().get(i).getNoOfMinutes())%60));
                                }else {

                                    workFromHomeSetDataPoJo.setHrs("");
                                }


                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            workFromHomeSetDataPoJo.setStatus(response.body().getData().get(i).getApplicationStatus());
                            arrayList.add(workFromHomeSetDataPoJo);
                            //Log.e("data added",arrayList.get(i).toString());
                        }
                        ApplicationStatusODAdapter adapter = new ApplicationStatusODAdapter(getActivity(), arrayList);
                        listView.setAdapter(adapter);

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
            public void onFailure(retrofit2.Call<Status> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), getContext());

                Log.e("TAG", t.toString());

            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.isVisible()){
        if (isVisibleToUser) {
            // Load your data here or do network operations here
           if (ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideOutDutyApp")) {


                if (Utilities.isNetworkAvailable(getContext())) {
                    OutDutyStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                    // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                } else {
                    Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                }
           }
        }


        }
    }

   /* @Override
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

    }*/

   /* @Override
    public void onResume() {
        super.onResume();
        if(ApplicationStatusActivity.position==2) {

            if (Utilities.isNetworkAvailable(getContext())) {
                OutDutyStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
