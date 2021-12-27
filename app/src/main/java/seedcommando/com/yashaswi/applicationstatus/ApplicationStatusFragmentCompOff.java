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
import seedcommando.com.yashaswi.compoffapplication.CompoffActivity;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.compoff.CompOffStatusSetData;
import seedcommando.com.yashaswi.pojos.compoff.status.CompOffStatus;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 10/13/2017.
 */

public class ApplicationStatusFragmentCompOff extends Fragment {

    ArrayList<CompOffStatusSetData> arrayList;
    ListView listView;
    Button leave_apply;
    boolean isFragmentLoaded=false;
    ProgressDialog pd;
    private ApiInterface apiService;

    public ApplicationStatusFragmentCompOff() {
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
        leave_apply= rootView.findViewById(R.id.button_mark_Attendance);
        //Log.e("data", MainActivity.arrayList.toString());
       // setUpExpList();
       // ApplicationStatusAdapter adapter = new ApplicationStatusAdapter(getActivity(), arrayList);
        //listView.setAdapter(adapter);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        //Log.e("data", MainActivity.arrayList.toString());
        //setUpExpList();

        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.buttonforleavestatus,listView,false);
        leave_apply= footer.findViewById(R.id.button_mark_Attendance);
        leave_apply.setText("Apply New CompOff");


        // So, this footer is non selectable
        listView.addFooterView(footer,null,false);
        // setUpExpList();
       if(ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideCompOffApp")) {
        //if(this.isVisible()) {
            if (Utilities.isNetworkAvailable(this.getContext())) {
                CompOffStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }
       //}


        leave_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationStatusActivity.position== HomeFragment.key.indexOf("HideCompOffApp")) {
                    startActivity(new Intent(getContext(), CompoffActivity.class));
                }
            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.isVisible()){
        if (isVisibleToUser ) {
            // Load your data here or do network operations here
          if (ApplicationStatusActivity.position==HomeFragment.key.indexOf("HideCompOffApp")) {

                if (Utilities.isNetworkAvailable(getContext())) {
                    CompOffStatus(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

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


    public void CompOffStatus(String id) {

        pd = new ProgressDialog(this.getActivity());
        pd.setMessage("Loading....");

        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();


        WorkFlowdataapp.put("employeeId",id);

        arrayList = new ArrayList<CompOffStatusSetData>();

        Call<CompOffStatus> call = apiService.CompoffAppStatusList(WorkFlowdataapp);
        call.enqueue(new Callback<CompOffStatus>() {
            @Override
            public void onResponse(retrofit2.Call<CompOffStatus> call, Response<CompOffStatus> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            CompOffStatusSetData compOffStatusSetData=new CompOffStatusSetData();
                            compOffStatusSetData.setNoOfCompOffDays(response.body().getData().get(i).getNoOfCompOffDays());
                            // Log.e("data added",response.body().getData().get(i).getNoOfWorkFromHomeDays());
                            compOffStatusSetData.setApplicationStatus(response.body().getData().get(i).getApplicationStatus());
                            // Log.e("data added",response.body().getData().get(i).getNoOfMinutes());
                            // if(response.body().getData().get(i).getFromTime().length()==0) {
                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            format2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

                            try {
                                Date date = format.parse(response.body().getData().get(i).getTakenCompOffDate());
                                //Date date1 = format.parse(response.body().getData().get(i).getToDate());
                                compOffStatusSetData.setTakenCompOffDate(format1.format(date));
                               // compOffStatusSetData.setTodate(format1.format(date1));
                               // compOffStatusSetData.setFromtime(response.body().getData().get(i).getFromTime());
                               compOffStatusSetData.setCompensatoryOffAppID(response.body().getData().get(i).getCompensatoryOffAppID());

                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            compOffStatusSetData.setReasonDescription(response.body().getData().get(i).getReasonDescription());
                            compOffStatusSetData.setTakenAgainst(response.body().getData().get(i).getTakenAgainst());
                            arrayList.add(compOffStatusSetData);
                            //Log.e("data added",arrayList.get(i).toString());
                        }
                        ApplicationStatusCompoffAdapter adapter = new ApplicationStatusCompoffAdapter(getActivity(), arrayList);
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
            public void onFailure(retrofit2.Call<CompOffStatus> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), getContext());

                Log.e("TAG", t.toString());

            }
        });
    }


}
