package seedcommando.com.yashaswi.applicationaprovels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
import seedcommando.com.yashaswi.pojos.aprovels.wfh.WFHAprovel;
import seedcommando.com.yashaswi.pojos.aprovels.wfh.WFHAprovelSetData;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 4/11/2018.
 */

public class AprovelFroWHF extends Fragment {

    ArrayList<WFHAprovelSetData> arrayList;
    ListView listView;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;

    public AprovelFroWHF() {
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
         if(ApplicationAprovelsActivity.position==4) {

             if (Utilities.isNetworkAvailable(this.getContext())) {
                 WFHAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                 // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
             } else {
                 Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
             }
         }


        return rootView;
    }


    public void WFHAppAprovals(String id,String token) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> aprovelFlow=new HashMap<String,String>();


        aprovelFlow.put("employeeID",id);
        aprovelFlow.put("token",token);

        arrayList = new ArrayList<WFHAprovelSetData>();

        Call<WFHAprovel> call = apiService.WFHAprovel(aprovelFlow);
        call.enqueue(new Callback<WFHAprovel>() {
            @Override
            public void onResponse(retrofit2.Call<WFHAprovel> call, Response<WFHAprovel> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            WFHAprovelSetData leaveAprovelSetData = new WFHAprovelSetData();
                            leaveAprovelSetData.setStatus(response.body().getData().get(i).getFinalApplicationStatus());
                            leaveAprovelSetData.setFname(response.body().getData().get(i).getFirstName());
                            leaveAprovelSetData.setLname(response.body().getData().get(i).getLastName());
                            leaveAprovelSetData.setLevel(response.body().getData().get(i).getLevelNo());
                            leaveAprovelSetData.setNoOfWorkFromHomeDays(response.body().getData().get(i).getNoOfWorkFromHomeDays());
                            leaveAprovelSetData.setNoOfMinutes(response.body().getData().get(i).getNoOfMinutes());
                            leaveAprovelSetData.setMaxLevelCount(response.body().getData().get(i).getMaxLevelCount());
                            leaveAprovelSetData.setWorkFromHomeAppIDMaster(response.body().getData().get(i).getWorkFromHomeAppIDMaster());
                            leaveAprovelSetData.setWorkFromHomeAppLevelDetailID(response.body().getData().get(i).getWorkFromHomeAppLevelDetailID());
                            format = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            format2 = new SimpleDateFormat("hh:mm aa");
                            try {
                                Date date = format.parse(response.body().getData().get(i).getFromDate());
                                Date date1 = format.parse(response.body().getData().get(i).getToDate());
                                leaveAprovelSetData.setFromDate(format1.format(date));
                                leaveAprovelSetData.setToDate(format1.format(date1));
                                Date date2 = format.parse(response.body().getData().get(i).getFromTime());
                                Date date3 = format.parse(response.body().getData().get(i).getToTime());

                                leaveAprovelSetData.setFromTime(format2.format(date2));
                                leaveAprovelSetData.setToTime(format2.format(date3));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            arrayList.add(leaveAprovelSetData);



                        }
                        WFHAprovelAdapter adapter = new  WFHAprovelAdapter(getActivity(), arrayList,AprovelFroWHF.this);
                        listView.setAdapter(adapter);
                    }else {
                        WFHAprovelAdapter adapter = new  WFHAprovelAdapter(getActivity(), arrayList,AprovelFroWHF.this);
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
            public void onFailure(retrofit2.Call<WFHAprovel> call, Throwable t) {
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
        if (isVisibleToUser ) {
            // Load your data here or do network operations here
            if (Utilities.isNetworkAvailable(this.getContext())) {
                WFHAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }


        }
    }

    /*@Override
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
}

