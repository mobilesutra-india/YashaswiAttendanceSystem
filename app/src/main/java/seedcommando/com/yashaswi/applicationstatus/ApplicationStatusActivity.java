package seedcommando.com.yashaswi.applicationstatus;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.Config;
import seedcommando.com.yashaswi.ExceptionHandler;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.applicationaprovels.ApplicationAprovelsActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomeSetDataPoJo;
import seedcommando.com.yashaswi.pojos.outdutypojo.Status;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.service.MyFirebaseMessagingService;
import seedcommando.com.yashaswi.utilitys.NotificationUtils;

/**
 * Created by commando1 on 9/20/2017.
 */

public class ApplicationStatusActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
   static int position;
    ProgressDialog pd;
  // static CharSequence tabname;
    private ApiInterface apiService;
  static   TabLayout.Tab tab1;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ArrayList<WorkFromHomeSetDataPoJo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.application_status_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Application Status");
        toolbar.setLogo(R.drawable.yashaswi_logo);
        /*TextView textView = (TextView) findViewById(R.id.title);
        textView.setText("Application Status");*/

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
        apiService = ApiClient.getClient().create(ApiInterface.class);



        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener(viewPager));

        Bundle  extras = getIntent().getExtras();
        if (extras != null) {
            int defaultValue = 0;
            int page = getIntent().getIntExtra("ARG_PAGE", defaultValue);
            viewPager.setCurrentItem(page);

        }


    }
    private void setupViewPager(ViewPager viewPager) {
        try {


            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            for (int i = 0; i < HomeFragment.key.size(); i++) {


                if (HomeFragment.key.get(i).equals("HideLeaveApp")) {
                    adapter.addFragment(new ApplicationStatusFragment(), "LEAVE");
                }
                if (HomeFragment.key.get(i).equals("HideRegularizationApp")) {

                    adapter.addFragment(new ApplicationStatusFragmentReg(), "REGULARIZATION");
                }
                if (HomeFragment.key.get(i).equals("HideOutDutyApp")) {
                    adapter.addFragment(new ApplicationStatusFragmentOutDuty(), "OUT DUTY");
                }
                if (HomeFragment.key.get(i).equals("HideCompOffApp")) {
                    adapter.addFragment(new ApplicationStatusFragmentCompOff(), "COMP OFF");
                }
                if (HomeFragment.key.get(i).equals("HideWFHApp")) {
                    adapter.addFragment(new ApplicationStatusFragmentWFH(), "WORK FROM HOME");
                }
            }
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            onTabSelectedListener(viewPager);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
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

    public void OutDutyStatus(String id) {

        pd = new ProgressDialog(ApplicationStatusActivity.this);
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> WorkFlowdataapp=new HashMap<String,String>();


        WorkFlowdataapp.put("employeeId",id);

        arrayList = new ArrayList<WorkFromHomeSetDataPoJo>();

        Call<Status> call = apiService.getOutDutyStatus(WorkFlowdataapp);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(retrofit2.Call<Status> call, Response<Status> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    SimpleDateFormat format,format1,format2;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            WorkFromHomeSetDataPoJo workFromHomeSetDataPoJo=new WorkFromHomeSetDataPoJo();
                            workFromHomeSetDataPoJo.setDays(response.body().getData().get(i).getNoOfOutDutyDays());
                            // Log.e("data added",response.body().getData().get(i).getNoOfWorkFromHomeDays());
                            workFromHomeSetDataPoJo.setHrs(response.body().getData().get(i).getNoOfMinutes());
                            // Log.e("data added",response.body().getData().get(i).getNoOfMinutes());
                            // if(response.body().getData().get(i).getFromTime().length()==0) {
                            format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                            format1 = new SimpleDateFormat("dd MMM yyyy");
                            format2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

                            try {
                                Date date = format.parse(response.body().getData().get(i).getFromDate());
                                Date date1 = format.parse(response.body().getData().get(i).getToDate());
                                workFromHomeSetDataPoJo.setFromdate(format1.format(date));
                                workFromHomeSetDataPoJo.setTodate(format1.format(date1));
                                workFromHomeSetDataPoJo.setFromtime(response.body().getData().get(i).getFromTime());
                                workFromHomeSetDataPoJo.setTotime(response.body().getData().get(i).getToTime());

                                // workFromHomeSetDataPoJo.setFromtime("NA");
                                // workFromHomeSetDataPoJo.setTotime("NA");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            workFromHomeSetDataPoJo.setStatus(response.body().getData().get(i).getApplicationStatus());
                            arrayList.add(workFromHomeSetDataPoJo);
                            //Log.e("data added",arrayList.get(i).toString());
                        }
                        ApplicationStatusWFH adapter = new ApplicationStatusWFH(ApplicationStatusActivity.this, arrayList);
                       ApplicationStatusFragmentOutDuty.listView.setAdapter(adapter);

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), ApplicationStatusActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", ApplicationStatusActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", ApplicationStatusActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", ApplicationStatusActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<Status> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }


}
