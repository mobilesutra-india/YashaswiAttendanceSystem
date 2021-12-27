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

import com.google.gson.Gson;

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
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.WorkFromHome.status.WFHStatus;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomeSetDataPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.workfromhomeapplication.WorkFromHomeActivity;

/**
 * Created by commando4 on 3/15/2018.
 */

public class ApplicationStatusFragmentWFH extends Fragment {

    ArrayList<WorkFromHomeSetDataPoJo> arrayList;
    ListView listView;
    Button leave_apply;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;


    public ApplicationStatusFragmentWFH () {
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
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //Log.e("data", MainActivity.arrayList.toString());
        //setUpExpList();

        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.buttonforleavestatus,listView,false);
        leave_apply= footer.findViewById(R.id.button_mark_Attendance);
        leave_apply.setText("Apply New WFH");

        // So, this footer is non selectable
        listView.addFooterView(footer,null,false);



        if(ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideWFHApp")) {

          //  if(this.isVisible()) {


                if (Utilities.isNetworkAvailable(this.getContext())) {
                    WorkFromHomeAppArovel(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                    // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                } else {
                    Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                }
            }
       // }
       // }
        //WorkFromHomeAppArovel(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),"3027");


        //WorkFromHomeAppArovel(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),"3027");

        leave_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationStatusActivity.position== HomeFragment.key.indexOf("HideWFHApp")) {
                    startActivity(new Intent(getContext(), WorkFromHomeActivity.class));
                }
            }
        });
        return rootView;
    }

    public void WorkFromHomeAppArovel(String id) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();


        WorkFlowdataapp.put("employeeID",id);

        arrayList = new ArrayList<WorkFromHomeSetDataPoJo>();

        Call<WFHStatus> call = apiService.WorkFromHomeAppStatus(WorkFlowdataapp);
        call.enqueue(new Callback<WFHStatus>() {
            @Override
            public void onResponse(retrofit2.Call<WFHStatus> call, Response<WFHStatus> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    SimpleDateFormat format,format1,format2;

                    Log.d("WorkFromValues: ", new Gson().toJson(response.body()));


                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            WorkFromHomeSetDataPoJo workFromHomeSetDataPoJo=new WorkFromHomeSetDataPoJo();
                            workFromHomeSetDataPoJo.setDays(response.body().getData().get(i).getNoOfWorkFromHomeDays());

                            workFromHomeSetDataPoJo.setWorkFromHomeAppID(response.body().getData().get(i).getWorkFromHomeAppID());


                            Log.e("data added",response.body().getData().get(i).getNoOfWorkFromHomeDays());
                           // workFromHomeSetDataPoJo.setHrs(response.body().getData().get(i).getNoOfMinutes());
                            Log.e("data added",response.body().getData().get(i).getNoOfMinutes());
                           // if(response.body().getData().get(i).getFromTime().length()==0) {
                                 format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                                //format2 = new SimpleDateFormat("dd-MMMyyyy HH:mm");

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
                               if(response.body().getData().get(i).getToTime()==null) {

                                    workFromHomeSetDataPoJo.setTotime("");
                                }else {
                                    workFromHomeSetDataPoJo.setTotime(response.body().getData().get(i).getToTime());


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
                        ApplicationStatusWFH adapter = new ApplicationStatusWFH(getActivity(), arrayList);
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
            public void onFailure(retrofit2.Call<WFHStatus> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.isVisible()){
        if (isVisibleToUser ) {
            // Load your data here or do network operations here
            if (ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideWFHApp")) {

                if (Utilities.isNetworkAvailable(getContext())) {
                    WorkFromHomeAppArovel(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                    // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                } else {
                    Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                }
            }
        }

        }
    }



}