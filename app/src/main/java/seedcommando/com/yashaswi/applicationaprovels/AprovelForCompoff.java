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
import seedcommando.com.yashaswi.pojos.aprovels.compoff.CompoffAprovel;
import seedcommando.com.yashaswi.pojos.aprovels.compoff.CompoffAprovelSetData;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 4/11/2018.
 */

public class AprovelForCompoff extends Fragment {

    ArrayList<CompoffAprovelSetData> arrayList;
    ListView listView;
    ProgressDialog pd;
    private ApiInterface apiService;
    boolean isFragmentLoaded=false;

    public AprovelForCompoff() {
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

        if(ApplicationAprovelsActivity.position==3) {
            if (Utilities.isNetworkAvailable(this.getContext())) {
                CompOffAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }


        return rootView;
    }


    public void CompOffAppAprovals(String id,String token) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> aprovelFlow=new HashMap<String,String>();


        aprovelFlow.put("employeeID",id);
        aprovelFlow.put("token",token);

        arrayList = new ArrayList<CompoffAprovelSetData>();

        Call<CompoffAprovel> call = apiService.CompOffAprovel(aprovelFlow);
        call.enqueue(new Callback<CompoffAprovel>() {
            @Override
            public void onResponse(retrofit2.Call<CompoffAprovel> call, Response<CompoffAprovel> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            CompoffAprovelSetData leaveAprovelSetData = new CompoffAprovelSetData();
                            leaveAprovelSetData.setStatus(response.body().getData().get(i).getFinalApplicationStatus());
                            leaveAprovelSetData.setFname(response.body().getData().get(i).getFirstName());
                            leaveAprovelSetData.setLname(response.body().getData().get(i).getLastName());
                            leaveAprovelSetData.setLevel(response.body().getData().get(i).getLevelNo());
                            leaveAprovelSetData.setReasonDescription(response.body().getData().get(i).getReasonDescription());
                            leaveAprovelSetData.setNoOfCompOffDays(response.body().getData().get(i).getNoOfCompOffDays());
                            leaveAprovelSetData.setMaxLevelCount(response.body().getData().get(i).getMaxLevelCount());
                            leaveAprovelSetData.setCompensatoryOffAppIDMaster(response.body().getData().get(i).getCompensatoryOffAppIDMaster());
                            leaveAprovelSetData.setCompensatoryOffAppLevelDetailID(response.body().getData().get(i).getCompensatoryOffAppLevelDetailID());
                            format = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                Date date = format.parse(response.body().getData().get(i).getTakenCompOffDate());

                                leaveAprovelSetData.setTakenCompOffDate(format1.format(date));
                                Log.e("compOffTaken",format1.format(date));



                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            arrayList.add(leaveAprovelSetData);



                        }
                       CompOffAprovelAdapter adapter = new CompOffAprovelAdapter(getActivity(), arrayList,AprovelForCompoff.this);
                        listView.setAdapter(adapter);
                    }else {
                        CompOffAprovelAdapter adapter = new CompOffAprovelAdapter(getActivity(), arrayList,AprovelForCompoff.this);
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
            public void onFailure(retrofit2.Call<CompoffAprovel> call, Throwable t) {
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
                CompOffAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

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

    }
*/
}

