package seedcommando.com.yashaswi.compoffapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 9/14/2017.
 */

public class CompoffAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CompoffPOJO> mDataSource;
    ArrayList<String> selectedStrings = new ArrayList<String>();
    ArrayList<String> compoffid=new ArrayList<>();
    private ApiInterface apiService;
    EditText editText_date;
    public CompoffAdapter(Context context, ArrayList<CompoffPOJO> items) {
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
   public ArrayList<String> getSelectedString(){
        return selectedStrings;
    }


    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        final View rowView = mInflater.inflate(R.layout.com_off_list_member, parent, false);

        apiService = ApiClient.getClient().create(ApiInterface.class);


        // Get title element
        final     TextView textView_Date_attendance =
                rowView.findViewById(R.id.textView_Date_comp_off);
        final  TextView textView_in_attendance =
                rowView.findViewById(R.id.textView_available_comp_off);
        TextView textView_out_attendance =
                rowView.findViewById(R.id.textView_expiry);
        TextView textView_manhrs_attendance =
                rowView.findViewById(R.id.textView_work_hrs);
        TextView textView_status_attendance =
                rowView.findViewById(R.id.textView_used_comp_off);
        final   CheckBox checkBox =
                rowView.findViewById(R.id.select_against_date);
        editText_date = parent.findViewById(R.id.Comp_off_date);

        // 1
        CompoffPOJO compoffPOJO= (CompoffPOJO) getItem(position);

        checkBox.setTag(compoffPOJO.compoffid);

        if(compoffPOJO.pendingunit.equals(compoffPOJO.earnunit))
        {
            Log.e("punit",compoffPOJO.pendingunit);
            Log.e("Eunit",compoffPOJO.earnunit);
            checkBox.setEnabled(false);
        }
        Log.e("opunit",compoffPOJO.pendingunit);
        Log.e("oEunit",compoffPOJO.earnunit);

// 2


        textView_Date_attendance.setText(compoffPOJO.date);
        textView_in_attendance.setText(compoffPOJO.available_units);
        textView_out_attendance.setText(compoffPOJO.expiry_date);
        textView_manhrs_attendance.setText(compoffPOJO.work_hrs);
        textView_status_attendance.setText(compoffPOJO.used_compoff);

       checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String data1="";
                String  data="";
                if (isChecked) {

                    compoffid.add(String.valueOf(buttonView.getTag()));

                    for(int i=0;i<compoffid.size();i++) {
                       data=compoffid.get(i);

                        Log.e("CompoffID", compoffid.get(i));

                        if (data1.equals("")) {
                            data1 = data;

                        } else {
                            data1 = data1 + "," + data;
                        }
                        //Log.e("CompoffID1", compoffid.get(1));
                    }
                    if (Utilities.isNetworkAvailable(mContext)) {
                        String ishalfday;
                        if(CompoffActivity.firsthalf||CompoffActivity.secondhalf){
                            ishalfday="1";

                        }
                        else {
                            ishalfday="0";
                        }
                        if(!data1.isEmpty()&&!data1.equals("")) {
                            CheckCompOffDetails(data1,((CompoffActivity)mContext).editText_date.getText().toString(),"1",ishalfday);
                            // ReasonForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                        }
                    }else {
                        Toast.makeText(mContext,"No Internet Connection...",Toast.LENGTH_LONG).show();

                    }
                    if(CompoffActivity.firsthalf||CompoffActivity.secondhalf) {
                        selectedStrings.add(textView_Date_attendance.getText().toString() + "(" + "0.5"+ ")");
                    }else {
                        if(Float.parseFloat(textView_in_attendance.getText().toString())< 1) {
                            selectedStrings.add(textView_Date_attendance.getText().toString() + "(" + "0.5" + ")");
                        }else {
                            selectedStrings.add(textView_Date_attendance.getText().toString() + "(" + "1" + ")");
                        }

                    }
                }else{
                    compoffid.remove(String.valueOf(buttonView.getTag()));
                    //Log.e("CompoffID",compoffid.get(0));
                    for(int i=0;i<compoffid.size();i++) {

                        data=compoffid.get(i);

                        Log.e("CompoffID", compoffid.get(i));

                        if (data1.equals("")) {
                            data1 = data;

                        } else {
                            data1 = data1 + "," + data;
                        }
                    }
                    if (Utilities.isNetworkAvailable(mContext)) {
                        String ishalfday;
                        if(CompoffActivity.firsthalf||CompoffActivity.secondhalf){
                            ishalfday="1";

                        }
                        else {
                            ishalfday="0";
                        }
                        if(!data1.isEmpty()&&!data1.equals("")) {
                            CheckCompOffDetails(data1,((CompoffActivity)mContext).editText_date.getText().toString(),"1",ishalfday);
                            // ReasonForCompoffApp(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                        }
                    }else {
                        Toast.makeText(mContext,"No Internet Connection...",Toast.LENGTH_LONG).show();

                    }
                    if(CompoffActivity.firsthalf||CompoffActivity.secondhalf) {
                        selectedStrings.remove(textView_Date_attendance.getText().toString() + "(" + "0.5"+ ")");
                    }else {
                        if(Float.parseFloat(textView_in_attendance.getText().toString())< 1) {
                            selectedStrings.remove(textView_Date_attendance.getText().toString() + "(" + "0.5" + ")");
                        }else {
                            selectedStrings.remove(textView_Date_attendance.getText().toString() + "(" + "1" + ")");
                        }

                    }
                   // selectedStrings.remove(textView_Date_attendance.getText().toString()+"("+textView_in_attendance.getText().toString()+")");
                }

            }
        });


        return rowView;
    }

    public void CheckCompOffDetails(String compoffid,String date,String ishrorday,String isHalfDay) {

       /* pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();*/

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();
        WorkFlowdataapp.put("compOffID",compoffid);
        WorkFlowdataapp.put("takenCODate",date);
        WorkFlowdataapp.put("isHrsOrdays",ishrorday);
        WorkFlowdataapp.put("isHalfDay",isHalfDay);



        retrofit2.Call<CommanResponsePojo> call = apiService.getCheckedCompoffDetails(WorkFlowdataapp);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                // pd.dismiss();


                if (response.isSuccessful()) {
                    EmpowerApplication.set_session("compOffDates","0");
                    CompoffActivity.flag=true;



                    if (response.body().getStatus().equals("1")) {

                        Log.e("data143",response.body().getData());

                        EmpowerApplication.set_session("compOffAgainstDetails",response.body().getData());
                        /*for(int i=0;i<response.body().getData().size();i++) {

                        }*/


                    }else {
                        // Creating adapter for spinner

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        String msg=response.body().getMessage();
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
                                        //editText.setText("");
                                        EmpowerApplication.set_session("compOffDates","1");

                                        //Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        //EmpowerApplication.alertdialog(response.body().getMessage(), mContext);
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
                // Log error here since request faile
                Log.e("TAG", t.toString());
                EmpowerApplication.alertdialog(t.getMessage(), mContext);


            }
        });
    }
}
