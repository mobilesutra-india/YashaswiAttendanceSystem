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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo.ApplicationSetData;
import seedcommando.com.yashaswi.pojos.regularizationpoJo.statuspojo.RegStatus;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 10/13/2017.
 */

public class ApplicationStatusFragmentReg extends Fragment {

    ArrayList<ApplicationSetData> arrayList;
    ListView listView;
    Button leave_apply;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;


    public ApplicationStatusFragmentReg() {
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
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //Log.e("data", MainActivity.arrayList.toString());
        //setUpExpList();

        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.buttonforleavestatus,listView,false);
        leave_apply= footer.findViewById(R.id.button_mark_Attendance);
        leave_apply.setText("Apply New Regularization");

        if(ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideRegularizationApp")) {
      //  if(this.isVisible()) {
            if (Utilities.isNetworkAvailable(getContext())) {
                RegAppStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
       }
       // }


        // So, this footer is non selectable
        listView.addFooterView(footer,null,false);
        // setUpExpList();




        leave_apply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Log.e("position", String.valueOf(HomeFragment.key.indexOf("HideRegularizationApp")));
                if(ApplicationStatusActivity.position== HomeFragment.key.indexOf("HideRegularizationApp")) {
                    startActivity(new Intent(getContext(), Attend_Regularization.class));
                }
            }
        });
        return rootView;
    }

    public void RegAppStatus(String id) {

        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading....");
        pd.show();
        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("employeeID",id);
        arrayList = new ArrayList<ApplicationSetData>();

        Call<RegStatus> call = apiService.getRegStatus(WorkFlowdataapp);
        call.enqueue(new Callback<RegStatus>() {
            @Override
            public void onResponse(retrofit2.Call<RegStatus> call, Response<RegStatus> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {
                        Log.d("RegValues: ", new Gson().toJson(response.body()));


                        for(int i=0;i<response.body().getData().size();i++){
                            ApplicationSetData applicationSetData = new ApplicationSetData();

                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                Date date = format.parse(response.body().getData().get(i).getShiftDateFrom());
                                Date date1 = format.parse(response.body().getData().get(i).getShiftDateTo());
                                applicationSetData.setFromDate(format1.format(date));
                                applicationSetData.setToDate(format1.format(date1));


                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           // applicationSetData.setFromDate(response.body().getData().get(i).getShiftDateFrom());
                            //applicationSetData.setToDate(response.body().getData().get(i).getShiftDateTo());
                            applicationSetData.setFromtime(response.body().getData().get(i).getInTime());
                            applicationSetData.setStatus(response.body().getData().get(i).getApplicationStatus());
                            applicationSetData.setTotime(response.body().getData().get(i).getOutTime());
                            applicationSetData.setShift(response.body().getData().get(i).getShiftName());
                            applicationSetData.setReason(response.body().getData().get(i).getReasonTitle());

                            applicationSetData.setRegularizationAppId(response.body().getData().get(i).getRegularizationAppId());
                            applicationSetData.setAppFromEmployeeId(response.body().getData().get(i).getAppFromEmployeeId());



                            arrayList.add(applicationSetData);

                        }
                        ApplicationStatusRegAdapter adapter = new ApplicationStatusRegAdapter(getActivity(), arrayList);
                        listView.setAdapter(adapter);



                        // listView.setScrollContainer(false);

                        //EmpowerApplication.alertdialog(response.body().getMessage(), getContext());

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
            public void onFailure(retrofit2.Call<RegStatus> call, Throwable t) {
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
            // Load your data here or do network operations here

            if (ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideRegularizationApp")) {
                if (Utilities.isNetworkAvailable(getContext())) {
                    RegAppStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

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
        if(ApplicationStatusActivity.position==1) {
            if (Utilities.isNetworkAvailable(getContext())) {
                RegAppStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}

