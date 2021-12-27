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
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.aprovels.od.ODAprovel;
import seedcommando.com.yashaswi.pojos.aprovels.od.ODAprovelSetData;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 4/11/2018.
 */

public class AprovelForOutDuty extends Fragment {

    ArrayList< ODAprovelSetData> arrayList;
    ListView listView;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;

    public AprovelForOutDuty() {
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
        if(ApplicationAprovelsActivity.position==2) {

            if (Utilities.isNetworkAvailable(this.getContext())) {
                ODAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }


        return rootView;
    }


    public void ODAppAprovals(String id,String token) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> aprovelFlow=new HashMap<String,String>();


        aprovelFlow.put("employeeID",id);
        aprovelFlow.put("token",token);

        arrayList = new ArrayList< ODAprovelSetData>();

        Call<ODAprovel> call = apiService.ODAprovel(aprovelFlow);
        call.enqueue(new Callback<ODAprovel>() {
            @Override
            public void onResponse(retrofit2.Call<ODAprovel> call, Response<ODAprovel> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;//,format12,format21;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            ODAprovelSetData leaveAprovelSetData = new  ODAprovelSetData();

                            leaveAprovelSetData.setStatus(response.body().getData().get(i).getFinalApplicationStatus());
                            leaveAprovelSetData.setFname(response.body().getData().get(i).getFirstName());
                            leaveAprovelSetData.setLname(response.body().getData().get(i).getLastName());
                            leaveAprovelSetData.setLevel(response.body().getData().get(i).getLevelNo());
                            leaveAprovelSetData.setNoOfOutDutyDays(response.body().getData().get(i).getNoOfOutDutyDays());
                            leaveAprovelSetData.setNoOfMinutes(response.body().getData().get(i).getNoOfMinutes());
                            leaveAprovelSetData.setMaxLevelCount(response.body().getData().get(i).getMaxLevelCount());
                            leaveAprovelSetData.setOutDutyIDMaster(response.body().getData().get(i).getOutDutyIDMaster());
                            leaveAprovelSetData.setOutDutyAppLevelDetailID(response.body().getData().get(i).getOutDutyAppLevelDetailID());
                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa", Locale.getDefault());
                            format1 = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
                            //format2 = new SimpleDateFormat("HH:mm aa");
                            try {
                                Date date = format.parse(response.body().getData().get(i).getFromDate());
                                Date date1 = format.parse(response.body().getData().get(i).getToDate());
                                leaveAprovelSetData.setFromDate(format1.format(date));
                                leaveAprovelSetData.setToDate(format1.format(date1));
                               java.text.DateFormat format12 = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa",Locale.getDefault());
                                Date date2 = format12.parse(response.body().getData().get(i).getFromTime());
                                format2 = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
                                Log.e("time1", response.body().getData().get(i).getFromTime());
                                Log.e("time1", String.valueOf(date2));
                                leaveAprovelSetData.setFromTime(format2.format(date2));
                                Log.e("time1",format2.format(date2));
                               java.text.DateFormat format21 = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa",Locale.getDefault());
                                Date date3 = format21.parse(response.body().getData().get(i).getToTime());
                                Log.e("time1", String.valueOf(date3));
                                format2 = new SimpleDateFormat("hh:mm aa",Locale.getDefault());


                                leaveAprovelSetData.setToTime(format2.format(date3));
                                Log.e("time2",format2.format(date3));



                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            arrayList.add(leaveAprovelSetData);



                        }
                        ODAprovelAdapter adapter = new ODAprovelAdapter(getActivity(), arrayList,AprovelForOutDuty.this);
                        listView.setAdapter(adapter);
                    }else {
                        ODAprovelAdapter adapter = new ODAprovelAdapter(getActivity(), arrayList,AprovelForOutDuty.this);
                        listView.setAdapter(adapter);
                        //listView.invalidateViews();
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
            public void onFailure(retrofit2.Call<ODAprovel> call, Throwable t) {
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
                ODAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
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

}
