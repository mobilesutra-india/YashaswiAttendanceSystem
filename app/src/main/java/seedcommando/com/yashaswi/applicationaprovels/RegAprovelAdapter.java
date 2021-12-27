package seedcommando.com.yashaswi.applicationaprovels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.aprovels.regularization.RegAprovelSetData;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 4/13/2018.
 */

public class RegAprovelAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<RegAprovelSetData> mDataSource;
   // private ArrayList<String> daystatus;
   // private ArrayList<String> daystatusid;
    ProgressDialog pd;
    boolean checkboxstatus=false;
    private ApiInterface apiService;
    AprovelForReg aprovelForReg1;


    public RegAprovelAdapter(Context context, ArrayList<RegAprovelSetData> items,ArrayList<String> daystatus1,ArrayList<String> daystatusid1,AprovelForReg aprovelForReg) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ///daystatus=daystatus1;
        //daystatusid=daystatusid1;
        aprovelForReg1=aprovelForReg;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item

        final View rowView = mInflater.inflate(R.layout.regaprovel, parent, false);

        // Get title element
        apiService = ApiClient.getClient().create(ApiInterface.class);

        final TextView inTime =
                rowView.findViewById(R.id.textView_in_time);
        final TextView outTime =
                rowView.findViewById(R.id.textViewout_time);
        TextView name =
                rowView.findViewById(R.id.textView_name);
        TextView reason =
                rowView.findViewById(R.id.textView_reason);
        TextView fromdate =
                rowView.findViewById(R.id.textView_from_date);
        TextView todate =
                rowView.findViewById(R.id.textViewto_date);
        TextView leavel =
                rowView.findViewById(R.id.textView_levelno);
        TextView status =
                rowView.findViewById(R.id.textView_app_status);
        final Spinner spinner =
                rowView.findViewById(R.id.day_status_spinner);
        CheckBox checkBox1=
                rowView.findViewById(R.id.Checkbox1);

        Button Aprove =
                rowView.findViewById(R.id.aprove);
        Button reject =
                rowView.findViewById(R.id.reject);

        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("RegularizationAppDayStatusAsPerShiftPolicy")).equals("1")){
            checkBox1.setEnabled(false);
            spinner.setEnabled(false);
        }else {
            checkBox1.setEnabled(true);
            spinner.setEnabled(true);
        }

        RegAprovelSetData leaveAprovelSetData = (RegAprovelSetData) getItem(position);
        if (leaveAprovelSetData.getMaxlevel().equals(leaveAprovelSetData.getLevel())) {
            Aprove.setText("Approve");
            reject.setText("Reject");

            inTime.setText(leaveAprovelSetData.getInTime());
            outTime.setText(leaveAprovelSetData.getOutTime());
            inTime.setTag(leaveAprovelSetData.getRegularizationAppIDMaster());
            outTime.setTag(leaveAprovelSetData.getRegularizationAppLevelDetailID());
            name.setText(leaveAprovelSetData.getFname() + " " + leaveAprovelSetData.getLname());
            reason.setText(leaveAprovelSetData.getReason());
            fromdate.setText(leaveAprovelSetData.getShiftDateFrom());
            todate.setText(leaveAprovelSetData.getShiftDateTo());
            leavel.setText(leaveAprovelSetData.getLevel());
            status.setText(leaveAprovelSetData.getStatus());
            // Creating adapter for spinner
            //if(!daystatus.isEmpty()) {
                @SuppressWarnings({"unchecked", "rawtypes"})
                ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, AprovelForReg.daystatus);

                // Drop down layout style - list view with radio button
               // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(arrayAdapter);
           // }
        } else {
            Aprove.setText("Recommend");
            reject.setText("Not Recommend");
            inTime.setText(leaveAprovelSetData.getInTime());
            outTime.setText(leaveAprovelSetData.getOutTime());
            inTime.setTag(leaveAprovelSetData.getRegularizationAppIDMaster());
            outTime.setTag(leaveAprovelSetData.getRegularizationAppLevelDetailID());
            name.setText(leaveAprovelSetData.getFname() + " " + leaveAprovelSetData.getLname());
            reason.setText(leaveAprovelSetData.getReason());
            fromdate.setText(leaveAprovelSetData.getShiftDateFrom());
            todate.setText(leaveAprovelSetData.getShiftDateTo());
            leavel.setText(leaveAprovelSetData.getLevel());
            status.setText(leaveAprovelSetData.getStatus());
            // Creating adapter for spinner
            @SuppressWarnings({ "unchecked", "rawtypes" })
            ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item,AprovelForReg.daystatus);

            // Drop down layout style - list view with radio button
           // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(arrayAdapter);

        }

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkboxstatus = isChecked;

            }
        });
        Aprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("one")).equals("1")) {
                    final Dialog dialogRemark = new Dialog(mContext);
                dialogRemark.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRemark.setContentView(R.layout.application_aprove_reason_dialog);
                Window window = dialogRemark.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogRemark.show();
                dialogRemark.setCancelable(true);
                dialogRemark.setCanceledOnTouchOutside(false);

                final EditText remark = dialogRemark
                        .findViewById(R.id.edit_reason);

                Button btn_submit = dialogRemark
                        .findViewById(R.id.submit1);

                Button btn_cancel = dialogRemark
                        .findViewById(R.id.cancel1);
                btn_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogRemark.dismiss();
                    }
                });

                //dialogue code...

                btn_submit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        if (!remark.getText().toString().isEmpty()) {
                            dialogRemark.dismiss();
                            if(Utilities.isNetworkAvailable(mContext)) {
                                Toast.makeText(mContext,inTime.getTag().toString()+""+outTime.getTag().toString(),Toast.LENGTH_LONG).show();

                                sendAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),remark.getText().toString(),inTime.getTag().toString(),outTime.getTag().toString(),"0",AprovelForReg.daystatusId.get(spinner.getSelectedItemPosition()),String.valueOf(checkboxstatus));



                            }else {
                                Toast.makeText(mContext,"No Internet Connection...",Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(mContext,  remark.getText().toString(), Toast.LENGTH_LONG).show();

                        } else {
                            EmpowerApplication.dialog("Please Enter Remark", mContext);


                        }
                    }
                });

                Toast.makeText(mContext, "Application Aproved", Toast.LENGTH_LONG).show();


                }else{

                    sendAppAprovalsForApprove(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),inTime.getTag().toString(),outTime.getTag().toString(),"0",AprovelForReg.daystatusId.get(spinner.getSelectedItemPosition()),String.valueOf(checkboxstatus));

                }



            }

        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogRemark = new Dialog(mContext);
                dialogRemark.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRemark.setContentView(R.layout.application_aprove_reason_dialog);
                Window window = dialogRemark.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogRemark.show();
                dialogRemark.setCancelable(true);
                dialogRemark.setCanceledOnTouchOutside(false);

                final EditText remark = dialogRemark
                        .findViewById(R.id.edit_reason);

                Button btn_submit = dialogRemark
                        .findViewById(R.id.submit1);

                Button btn_cancel = dialogRemark
                        .findViewById(R.id.cancel1);
                btn_cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogRemark.dismiss();
                    }
                });

                //dialogue code...

                btn_submit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        if (!remark.getText().toString().isEmpty()) {
                            dialogRemark.dismiss();
                            if(Utilities.isNetworkAvailable(mContext)) {
                                Toast.makeText(mContext,inTime.getTag().toString()+""+outTime.getTag().toString(),Toast.LENGTH_LONG).show();
                                sendAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),remark.getText().toString(),inTime.getTag().toString(),outTime.getTag().toString(),"1",AprovelForReg.daystatusId.get(spinner.getSelectedItemPosition()),String.valueOf(checkboxstatus));


                                //sendAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),remark.getText().toString(),leavetype.getTag().toString(),balance.getTag().toString(),"1");

                                // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                            }else {
                                Toast.makeText(mContext,"No Internet Connection...",Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(mContext,  remark.getText().toString(), Toast.LENGTH_LONG).show();

                        } else {
                            EmpowerApplication.dialog("Please Enter Remark", mContext);


                        }
                    }
                });
                Toast.makeText(mContext, "Application Rejected", Toast.LENGTH_LONG).show();

            }
        });
        return rowView;

    }


    public void sendAppAprovals(String id,String reason,String appmasterid,String appleveldetailid,String status,String statusid,String isoverridable) {

        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> aprovelFlow=new HashMap<String,String>();


        aprovelFlow.put("ByEmployeeID",id);
        aprovelFlow.put("rejectRemark",reason);
        aprovelFlow.put("RegularizationAppID",appmasterid);
        aprovelFlow.put("RegularizationAppLevelDetailID",appleveldetailid);
        aprovelFlow.put("statusID",status);
        aprovelFlow.put("DayStatusID",statusid);
        aprovelFlow.put("IsDayStatusOverride",isoverridable);

        Log.e("TAG", statusid +" s "+status +" cd "+isoverridable);
        Call<CommanResponsePojo> call = apiService.SendRegAprovel(aprovelFlow);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    if (response.body().getStatus().equals("1")) {
                        String msg=response.body().getMessage();
                        // EmpowerApplication.alertdialog(response.body().getMessage(),mContext);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        //String str1=msg+";";
                        // msg = msg.Replace("\r", " ").Replace("\n", " ");
                        msg=msg.replace("\\r",".").replace("\\n","");
                        // String newstr= msg.replace("[\r\n]", ".");
                        // newstr =  newstr.replace("\r", ".").replace("\r", "");
                        //msg.replace("\r\n", ".");
                        alertDialogBuilder.setMessage(msg);
                        alertDialogBuilder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if(Utilities.isNetworkAvailable(mContext)) {
                                            aprovelForReg1.RegAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                                            // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                                        }else {
                                            Toast.makeText(mContext,"No Internet Connection...",Toast.LENGTH_LONG).show();
                                        }
                                        //Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), mContext);
                    }
                }else {
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
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), mContext);


            }
        });
    }

    public void sendAppAprovalsForApprove(String id,String appmasterid,String appleveldetailid,String status,String statusid,String isoverridable) {

        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String,String> aprovelFlow=new HashMap<String,String>();


        aprovelFlow.put("ByEmployeeID",id);
        aprovelFlow.put("rejectRemark","");
        aprovelFlow.put("RegularizationAppID",appmasterid);
        aprovelFlow.put("RegularizationAppLevelDetailID",appleveldetailid);
        aprovelFlow.put("statusID",status);
        aprovelFlow.put("DayStatusID",statusid);
        aprovelFlow.put("IsDayStatusOverride",isoverridable);

        Log.e("TAG", statusid +" s "+status +" cd "+isoverridable);
        Call<CommanResponsePojo> call = apiService.SendRegAprovel(aprovelFlow);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {


                if (response.isSuccessful()) {
                    pd.dismiss();
                    if (response.body().getStatus().equals("1")) {
                        String msg=response.body().getMessage();
                        // EmpowerApplication.alertdialog(response.body().getMessage(),mContext);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        //String str1=msg+";";
                        // msg = msg.Replace("\r", " ").Replace("\n", " ");
                        msg=msg.replace("\\r",".").replace("\\n","");
                        // String newstr= msg.replace("[\r\n]", ".");
                        // newstr =  newstr.replace("\r", ".").replace("\r", "");
                        //msg.replace("\r\n", ".");
                        alertDialogBuilder.setMessage(msg);
                        alertDialogBuilder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if(Utilities.isNetworkAvailable(mContext)) {
                                            aprovelForReg1.RegAppAprovals(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

                                            // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                                        }else {
                                            Toast.makeText(mContext,"No Internet Connection...",Toast.LENGTH_LONG).show();
                                        }
                                        //Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), mContext);
                    }
                }else {
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
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), mContext);


            }
        });
    }


}



