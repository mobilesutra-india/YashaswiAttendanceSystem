package seedcommando.com.yashaswi.discrepanciesactivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.Config;
import seedcommando.com.yashaswi.ExceptionHandler;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.applicationaprovels.ApplicationAprovelsActivity;
import seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization;
import seedcommando.com.yashaswi.compoffapplication.CompoffActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.leaveapplication.Leave_Application;
import seedcommando.com.yashaswi.Out_Duty_Application;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.discripanciespojo.Descripancy;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.service.MyFirebaseMessagingService;
import seedcommando.com.yashaswi.utilitys.NotificationUtils;
import seedcommando.com.yashaswi.workfromhomeapplication.WorkFromHomeActivity;

/**
 * Created by commando1 on 9/1/2017.
 */

public class DiscrepanciesActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<DiscrepanciesDataPOJO> arrayList;
    private ApiInterface apiService;
    ProgressDialog pd;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.discrepancieslayout);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Discrepancies");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);

     /*   TextView textView = (TextView) findViewById(R.id.title);
        textView.setText("Discrepancies");*/

        listView = findViewById(R.id.list);
        apiService= ApiClient.getClient().create(ApiInterface.class);
        //Log.e("data", MainActivity.arrayList.toString());

       // setUpExpList();
        if(Utilities.isNetworkAvailable(DiscrepanciesActivity.this)) {

           DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        }else {
            Toast.makeText(DiscrepanciesActivity.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(HomeFragment.key.size()>0){
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.application_selection_descripancies);
                Window window = dialog.getWindow();

                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView date_TextView =
                        view.findViewById(R.id.textView_Date);
                TextView day_TextView =
                        view.findViewById(R.id.textView_day);
                final String datee=date_TextView.getText().toString();
                 final TextView textView_date= dialog.findViewById(R.id.textView_date);
                     textView_date.setText(day_TextView.getText().toString()+" "+date_TextView.getText().toString());
                 final  Button apply= dialog.findViewById(R.id.apply);
                 final Button  cancel= dialog.findViewById(R.id.cancel);
                 final RadioGroup rg = dialog.findViewById(R.id.radiogroup);
                for(int i=0;i<HomeFragment.key.size();i++){
                    RadioButton radioButton=new RadioButton(DiscrepanciesActivity.this);
                    //radioButton.getButtonTintList(R.color.radio_button_color);
                    if(HomeFragment.key.get(i).equals("HideLeaveApp")){
                        radioButton.setText("Leave Application");
                        radioButton.setId(0);
                    }
                    if(HomeFragment.key.get(i).equals("HideRegularizationApp")){
                        radioButton.setText("Regularization Application");
                        radioButton.setId(1);
                    }
                    if(HomeFragment.key.get(i).equals("HideOutDutyApp")){
                        radioButton.setText("Out Duty Application");
                        radioButton.setId(2);
                    }
                    if(HomeFragment.key.get(i).equals("HideCompOffApp")){
                        radioButton.setText("CompOff Application");
                        radioButton.setId(3);
                    }
                    if(HomeFragment.key.get(i).equals("HideWFHApp")){
                        radioButton.setText("WFH Application");
                        radioButton.setId(4);
                    }

                    rg.addView(radioButton);
                }
                dialog.show();

                       /* rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                        {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId)
                            {
                                final int checkid= checkedId;*/
                       apply.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if (rg.getCheckedRadioButtonId() != -1) {
                                   final int checkid = rg.getCheckedRadioButtonId();
                                   SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                                   SimpleDateFormat  format1 = new SimpleDateFormat("dd-MMM-yyyy");


                               switch (checkid) {
                                   case 0:
                                       Intent i = new Intent(DiscrepanciesActivity.this, Leave_Application.class);
                                       try {
                                           Date date = format.parse(datee);
                                           i.putExtra("Date",format1.format(date));
                                           startActivity(i);

                                           dialog.dismiss();


                                           // workFromHomeSetDataPoJo.setFromtime("NA");
                                           // workFromHomeSetDataPoJo.setTotime("NA");

                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }

                                       break;
                                   case 1:
                                       // TODO Something
                                       Intent i2 = new Intent(DiscrepanciesActivity.this, Attend_Regularization.class);
                                       try {
                                           Date date = format.parse(datee);
                                           i2.putExtra("Date",format1.format(date));
                                           startActivity(i2);
                                           dialog.dismiss();
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }

                                       break;
                                   case 2:
                                       // TODO Something
                                       Intent i3 = new Intent(DiscrepanciesActivity.this, Out_Duty_Application.class);
                                       try {
                                           Date date = format.parse(datee);
                                           i3.putExtra("Date",format1.format(date));
                                           startActivity(i3);
                                           dialog.dismiss();
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }

                                       break;
                                   case 3:
                                       // TODO Something
                                       Intent i4 = new Intent(DiscrepanciesActivity.this, CompoffActivity.class);
                                       try {
                                           Date date = format.parse(datee);
                                           i4.putExtra("Date",format1.format(date));
                                           startActivity(i4);
                                           dialog.dismiss();
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }

                                       break;
                                   case 4:
                                       // TODO Something
                                       Intent i5 = new Intent(DiscrepanciesActivity.this, WorkFromHomeActivity.class);
                                       try {
                                           Date date = format.parse(datee);
                                           i5.putExtra("Date",format1.format(date));
                                           startActivity(i5);
                                           dialog.dismiss();
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }

                                       break;
                               }
                           }else

                           {
                               EmpowerApplication.alertdialog("Please select Application type",DiscrepanciesActivity.this);
                           }
                       }
                        });
                   /* }
                });*/

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }else {
                EmpowerApplication.alertdialog("No Application available",DiscrepanciesActivity.this);
            }
            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    invalidateOptionsMenu();//Activity method
                    //updateMenuCounts(MyFirebaseMessagingService.count1);

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }

            }
        };
    }
    private void setUpExpList() {

        //listDataMembers= new HashMap<String, ArrayList<WeekReportPojo>>();
        // Adding province names and number of population as groups

        arrayList  = new ArrayList<DiscrepanciesDataPOJO>();
        DiscrepanciesDataPOJO discrepanciesDataPOJO =new  DiscrepanciesDataPOJO();
        discrepanciesDataPOJO.setDate("2 APR 2017");
        discrepanciesDataPOJO.setDay("Tue");
        discrepanciesDataPOJO.setStatus("A");

        arrayList.add(discrepanciesDataPOJO);
        DiscrepanciesDataPOJO discrepanciesDataPOJO1 =new  DiscrepanciesDataPOJO();
        discrepanciesDataPOJO1.setDate("3 APR 2017");
        discrepanciesDataPOJO1.setDay("Mon");
        discrepanciesDataPOJO1.setStatus("A");
        arrayList.add(discrepanciesDataPOJO1);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setVisible(false);
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable,"200");

        return  super.onCreateOptionsMenu(menu);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable   layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable, String.valueOf(MyFirebaseMessagingService.count1));

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Intent intent = new Intent(this, ApplicationAprovelsActivity.class);
            this.startActivity(intent);
            MyFirebaseMessagingService.count1=0;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }


    public void DescripanciesData(String EmployeeId) {

        pd = new ProgressDialog(DiscrepanciesActivity.this);
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> Descripancies=new HashMap<String,String>();
        Descripancies.put("employeeId",EmployeeId);
        arrayList  = new ArrayList<DiscrepanciesDataPOJO>();

        retrofit2.Call<Descripancy> call = apiService. getDescripancies(Descripancies);
        call.enqueue(new Callback<Descripancy>() {
            @Override
            public void onResponse(retrofit2.Call<Descripancy> call, Response<Descripancy> response) {
                pd.dismiss();

                if(response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++) {

                            DiscrepanciesDataPOJO discrepanciesDataPOJO = new DiscrepanciesDataPOJO();
                            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");

                            try {
                                Date date = format.parse(response.body().getData().get(i).getShiftDate());
                                discrepanciesDataPOJO.setDate(format1.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //discrepanciesDataPOJO.setDate("2 APR 2017");
                            discrepanciesDataPOJO.setDay(response.body().getData().get(i).getDay());
                            discrepanciesDataPOJO.setStatus(response.body().getData().get(i).getDayStatusCode());

                            arrayList.add(discrepanciesDataPOJO);
                        }


                        DescrepanciesAdapter adapter = new  DescrepanciesAdapter(DiscrepanciesActivity.this, arrayList);
                        listView.setAdapter(adapter);


                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), DiscrepanciesActivity.this);

                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", DiscrepanciesActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", DiscrepanciesActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", DiscrepanciesActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Descripancy> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(),DiscrepanciesActivity.this);

            }
        });
    }





}
