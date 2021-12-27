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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.leavepojo.statuspojo.Status;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 9/20/2017.
 */

public class ApplicationStatusAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ApplicationStatusPOJO> mDataSource;
    private ApiInterface apiService;
    ProgressDialog pd;


    public ApplicationStatusAdapter(Context context, ArrayList<ApplicationStatusPOJO> items) {
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
        View rowView = mInflater.inflate(R.layout.application_status_list_member, parent, false);

        apiService = ApiClient.getClient().create(ApiInterface.class);


        // Get title element
        TextView name =
                rowView.findViewById(R.id.textView_leave_type);
        TextView one_TextView =
                rowView.findViewById(R.id.textView_date);
        TextView two_TextView =
                rowView.findViewById(R.id.textView_statuss);
        ImageButton deleteItem =
                rowView.findViewById(R.id.cancel_request);
        final ApplicationStatusPOJO applicationStatusPOJO= (ApplicationStatusPOJO) getItem(position);

        deleteItem.setOnClickListener(new View.OnClickListener() {
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
                                DeleteLeaveApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")),applicationStatusPOJO.getLeaveAppID());


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

        name.setText(applicationStatusPOJO.leavetype);
        one_TextView.setText("FROM "+applicationStatusPOJO.fromdate+" TO "+applicationStatusPOJO.todate);
        two_TextView.setText(applicationStatusPOJO.status);
        return rowView;
    }
    public void DeleteLeaveApp(String employeecode, String token, String LeaveAppID) {
        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        HashMap<String, String> reg = new LinkedHashMap<String, String>();
        reg.put("employeeId", employeecode);
        reg.put("token", token);
        reg.put("LeaveAppID", LeaveAppID);

        //   Toast.makeText(this, ""+logindata, Toast.LENGTH_LONG).show();
        Log.d("", "DeleteWorkFromHomeApp: "+reg);
        Call<Status> call = apiService.DeleteLeaveApp(reg);

        Log.d("", "DeleteWorkFromHomeApp: "+call);

        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {
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
            public void onFailure(retrofit2.Call<Status> call, Throwable t) {
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
