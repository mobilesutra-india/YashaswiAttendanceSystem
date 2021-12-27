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
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.leaveapplication.Leave_Application;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.leavepojo.statuspojo.Status;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 9/20/2017.
 */

public class ApplicationStatusFragment extends Fragment {

    ArrayList<ApplicationStatusPOJO> arrayList;
    ListView listView;
    Button leave_apply;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;

    public ApplicationStatusFragment() {
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
        if(ApplicationStatusActivity.position == HomeFragment.key.indexOf("HideLeaveApp")) {
        //if(this.isVisible()) {
            if (Utilities.isNetworkAvailable(this.getContext())) {
                LeaveAppStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
       }
       // }


        // So, this footer is non selectable
        listView.addFooterView(footer,null,false);
       // setUpExpList();




        leave_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("position", String.valueOf(HomeFragment.key.indexOf("HideLeaveApp")));
                Log.e("position1", String.valueOf(ApplicationStatusActivity.position));
                if(ApplicationStatusActivity.position== HomeFragment.key.indexOf("HideLeaveApp")) {
                    startActivity(new Intent(getContext(), Leave_Application.class));
                }
            }
        });

        return rootView;
    }


    public void LeaveAppStatus(String id) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();


        WorkFlowdataapp.put("employeeID",id);

        arrayList = new ArrayList<ApplicationStatusPOJO>();

        Call<Status> call = apiService.getLeaveStatus(WorkFlowdataapp);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            ApplicationStatusPOJO applicationStatusPOJO = new ApplicationStatusPOJO();
                            applicationStatusPOJO.setLeavetype(response.body().getData().get(i).getLeaveTypeCode());
                            applicationStatusPOJO.setStatus(response.body().getData().get(i).getApplicationStatus());
                            applicationStatusPOJO.setLeaveAppID(response.body().getData().get(i).getLeaveAppID());

                            arrayList.add(applicationStatusPOJO);
                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                Date date = format.parse(response.body().getData().get(i).getFromDate());
                                Date date1 = format.parse(response.body().getData().get(i).getToDate());
                                applicationStatusPOJO.setFromdate(format1.format(date));
                                applicationStatusPOJO.setTodate(format1.format(date1));


                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }




                        }
                        ApplicationStatusAdapter adapter = new ApplicationStatusAdapter(getActivity(), arrayList);
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
            public void onFailure(retrofit2.Call<Status> call, Throwable t) {
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

            if(ApplicationStatusActivity.position == HomeFragment.key.indexOf("HideLeaveApp")) {
                if (Utilities.isNetworkAvailable(this.getContext())) {
                    LeaveAppStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

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

    }
*/
   /* @Override
    public void onResume() {
        super.onResume();

        if(ApplicationStatusActivity.position==0) {
            if (Utilities.isNetworkAvailable(this.getContext())) {
                LeaveAppStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
