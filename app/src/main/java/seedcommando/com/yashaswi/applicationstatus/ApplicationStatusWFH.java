package seedcommando.com.yashaswi.applicationstatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomeSetDataPoJo;
import seedcommando.com.yashaswi.pojos.WorkFromHome.status.WFHStatus;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 3/15/2018.
 */

public class ApplicationStatusWFH extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WorkFromHomeSetDataPoJo> mDataSource;
    private ApiInterface apiService;
    ProgressDialog pd;


    public ApplicationStatusWFH(Context context, ArrayList<WorkFromHomeSetDataPoJo> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.application_status_wfh, parent, false);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        // Get title element
        TextView fromdate =
                rowView.findViewById(R.id.fromdate);
       LinearLayout from_to_time =
               rowView.findViewById(R.id.FROM_TO_TIME);
        TextView todate =
                rowView.findViewById(R.id.todate);
        TextView fromtime =
                rowView.findViewById(R.id.fromtime);
        TextView totime =
                rowView.findViewById(R.id.totime);
        TextView days =
                rowView.findViewById(R.id.days);
        TextView hrs =
                rowView.findViewById(R.id.hours);
        TextView two_TextView =
                rowView.findViewById(R.id.textView_statuss);
        ImageButton deleteicon =
                rowView.findViewById(R.id.cancel_request);
       final WorkFromHomeSetDataPoJo applicationStatusPOJO= (WorkFromHomeSetDataPoJo) getItem(position);
        //Log.e("Listview date",applicationStatusPOJO.getFromdate());



        deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setMessage("Are you sure you want to delete? if yes then click on (OK) otherwise click (Cancel)");
                alertDialogBuilder.setIcon(R.drawable.yashaswi_logo);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                mDataSource.remove(position);
                                notifyDataSetChanged();
                                DeleteWorkFromHomeApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),applicationStatusPOJO.getWorkFromHomeAppID());


                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();






            }
        });



        fromdate.setText(applicationStatusPOJO.getFromdate());
        todate.setText(applicationStatusPOJO.getTodate());
       if(applicationStatusPOJO.getFromtime().equals("") && applicationStatusPOJO.getTotime().equals("")){
            from_to_time.removeAllViews();
        }else {
            fromtime.setText(applicationStatusPOJO.getFromtime());
            totime.setText(applicationStatusPOJO.getTotime());
        }
        days.setText(applicationStatusPOJO.getDays());
       hrs.setText(applicationStatusPOJO.getHrs());
        two_TextView.setText(applicationStatusPOJO.getStatus());
        return rowView;
    }


    public void DeleteWorkFromHomeApp(String employeecode, String token, String WorkFromHomeAppID) {
        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        HashMap<String, String> reg = new LinkedHashMap<String, String>();
        reg.put("employeeId", employeecode);
        reg.put("token", token);
        reg.put("WorkFromHomeAppID", WorkFromHomeAppID);

        //   Toast.makeText(this, ""+logindata, Toast.LENGTH_LONG).show();
        Log.d("", "DeleteWorkFromHomeApp: "+reg);
        Call<WFHStatus> call = apiService.DeleteWorkFromHomeApp(reg);

        Log.d("", "DeleteWorkFromHomeApp: "+call);

        call.enqueue(new Callback<WFHStatus>() {
            @Override
            public void onResponse(retrofit2.Call<WFHStatus> call, Response<WFHStatus> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {
                        EmpowerApplication.alertdialog( response.body().getMessage(), mContext);
                        Log.d("", "DeleteWorkFromHomeApp: "+response.body().getMessage());

                    }else{
                        EmpowerApplication.alertdialog( response.body().getMessage(), mContext);

                    }





                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", mContext);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", mContext);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", mContext);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<WFHStatus> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                try {
                    if (t.getMessage().equals("Field map contained null value for key 'employeeCode'") || t.getMessage().equals("Field map contained null value for key 'deviceId'")) {
                        EmpowerApplication.alertdialog("Something went wrong", mContext);
                    } else {
                        // EmpowerApplication.alertdialog(t.getMessage(), mContext);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

}
