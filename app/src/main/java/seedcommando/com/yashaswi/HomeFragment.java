package seedcommando.com.yashaswi;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.applicationstatus.ApplicationStatusActivity;
import seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization;
import seedcommando.com.yashaswi.compoffapplication.CompoffActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.leaveapplication.Leave_Application;
import seedcommando.com.yashaswi.manageractivity.AttendanceSummaryFragment;
import seedcommando.com.yashaswi.manageractivity.CalendarAdapter;
import seedcommando.com.yashaswi.manageractivity.CalendarView;
import seedcommando.com.yashaswi.manageractivity.CalenderFragment;
import seedcommando.com.yashaswi.manageractivity.SunordinateSummary;
import seedcommando.com.yashaswi.manageractivity.WeekReportPojo;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.ManagerPoJo.ClaenderPoJo;
import seedcommando.com.yashaswi.pojos.config.EmployeeConfig;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;
import seedcommando.com.yashaswi.workfromhomeapplication.WorkFromHomeActivity;

import static seedcommando.com.yashaswi.manageractivity.CalendarView.cells;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    ProgressDialog pd;
   public static String show;
   // private Calendar currentDate = CalenderFragment.currentDate;
    Calendar currentDate = Calendar.getInstance();
    private static final String DATE_FORMAT_calender = "dd-MMM-yyyy";



    private TextView pendingAproval;
    private ViewPager viewPager;
    private Button button_mark_attendance,leave_app_btn,attd_reg_btn,out_duty_btn, app_comp_off,app_wfh;
    LinearLayout bottom_menu;
    ArrayList<WeekReportPojo> arrayList;
    public    static ArrayList<String> key,key1,hideCameraKey;
    public   static ArrayList<String> value,value1,hideCameravalue;
    private ApiInterface apiService;
    boolean camera12=true;
    public static int position;
    ViewStub stub;
    View view;
    Toolbar toolbar ;
    private DrawerLayout drawer;
    NavigationView navigationView;
    SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_calender);

    static ArrayList<String> daystatus;




    public HomeFragment() {
        // Required empty public constructor
    }


    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }*/

    LinearLayout PendingApprovl12;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.home_fragment, container, false);
        setHasOptionsMenu(true);

        Log.d("", "AllowAttendancefromMobileApp_values: "+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendancefromMobileApp")));

        button_mark_attendance= view.findViewById(R.id.button_mark_Attendance);
        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowAttendancefromMobileApp")).equals("False")) {
            button_mark_attendance.setVisibility(View.GONE);
        }


        // lout=view.findViewById(R.id.bottom_menu);
       // stub = (ViewStub)view.findViewById(R.id.layout_stub);
        //leave_app_btn=(Button)view.findViewById(R.id.btn_leave_app);
       //attd_reg_btn=(Button)view.findViewById(R.id.btn_attd_reg);
       // out_duty_btn=(Button)view.findViewById(R.id.btn_out_duty);
       //app_comp_off=(Button)view.findViewById(R.id.btn_comp_off);
        //app_wfh=(Button)view.findViewById(R.id.btn_wfh);
        PendingApprovl12= view.findViewById(R.id.pending_event);
        pendingAproval= view.findViewById(R.id.button_pending_leaves);
        bottom_menu= view.findViewById(R.id.bottom_menu);



        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        apiService= ApiClient.getClient().create(ApiInterface.class);
        drawer = getActivity().findViewById(R.id.drawer_layout);
        navigationView = getActivity().findViewById(R.id.nav_view);

        if(Utilities.isNetworkAvailable(this.getContext())) {
            getEmployeeConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

            getPendingAprovalData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        }else {
            Toast.makeText(this.getContext(),"No Internet Connection...",Toast.LENGTH_LONG).show();
        }



        button_mark_attendance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                  //  Log.e("key",key1.get(0));
                   // Log.e("value",value1.get(0));



                        if (key1.contains("HideCameraForMarkAttendance")) {
                        //button_mark_attendance=(Button)view.findViewById(R.id.button_mark_Attendance);
                        if (value1.get(key1.indexOf("HideCameraForMarkAttendance")).equals("0")) {



                            // returns true if Developer Option  enabled, false if not enabled.
                            ContentResolver cr =getActivity().getContentResolver();
                            String setting =Settings.Global.DEVELOPMENT_SETTINGS_ENABLED;
                            int r = Settings.Secure.getInt(cr,setting,0);



                            if (r == 0) {
                                Intent i = new Intent(getContext(), Attendance_cameraActivity.class);
                                getContext().startActivity(i);

                            } else{

                                EmpowerApplication.alertdialog("Fake location app is installed in mobile or Developer/Debug option is enabled.\n" +
                                        "Please disable it and Mark your attendance" , getContext());

                            }
                            }

                        }
                        if (value1.get(key1.indexOf("HideCameraForMarkAttendance")).equals("1")) {

                         //   Intent i = new Intent(getContext(), MapsActivity.class);
                         //   getContext().startActivity(i);


                            ContentResolver cr =getActivity().getContentResolver();

                            String setting =Settings.Global.DEVELOPMENT_SETTINGS_ENABLED;
                            int r = Settings.Secure.getInt(cr,setting,0);



                            if (r == 0) {


                                Intent i = new Intent(getContext(), MapsActivity.class);
                                   getContext().startActivity(i);

                            } else{

                                EmpowerApplication.alertdialog("Fake location app is installed in mobile or Developer/Debug option is enabled.\n" +
                                        "Please disable it and Mark your attendance" , getContext());

                            }


                        }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        PendingApprovl12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ApplicationStatusActivity.class);
                getContext().startActivity(i);

            }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new CalenderFragment(), "CALENDAR");
        adapter.addFragment(new AttendanceSummaryFragment(), "SUMMARY");
        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("IsManager")).equals("1")) {
            adapter.addFragment(new SunordinateSummary(), "SUBORDINATE SUMMARY");
        }
        viewPager.setAdapter(adapter);
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

    public void getEmployeeConfig(String employeeid) {

      /*  pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
*/
        Map<String,String> configdata=new HashMap<String,String>();

            configdata.put("employeeId",employeeid);


            key=new ArrayList<>();
            value=new ArrayList<>();
            key1=new ArrayList<>();
            value1=new ArrayList<>();
        hideCameraKey=new ArrayList<>();
        hideCameravalue=new ArrayList<>();

        retrofit2.Call<EmployeeConfig> call = apiService.getConfigData(configdata);
        call.enqueue(new Callback<EmployeeConfig>() {
            @Override
            public void onResponse(retrofit2.Call<EmployeeConfig> call, Response<EmployeeConfig> response) {

                key.clear();
                key1.clear();
                value.clear();
                value1.clear();
                hideCameraKey.clear();
                hideCameravalue.clear();
                bottom_menu.removeAllViews();

                stub=new ViewStub(getContext());
                bottom_menu.addView(stub);
              //stub = (ViewStub)view.findViewById(R.id.layout_stub);
                if(response.isSuccessful()) {
              //      pd.dismiss();

                    Log.d("User ID1: ", new Gson().toJson(response.body()));
                    if (response.body().getStatus().equals("1")) {
                   //     pd.dismiss();
                    //    Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        for(int i=0;i<response.body().getData().size();i++) {
                            if(response.body().getData().get(i).getValue().equals("0")) {

                                if(response.body().getData().get(i).getKey().equals("HideLeaveApp") ||response.body().getData().get(i).getKey().equals("HideCompOffApp")||response.body().getData().get(i).getKey().equals("HideRegularizationApp")||response.body().getData().get(i).getKey().equals("HideWFHApp")||response.body().getData().get(i).getKey().equals("HideOutDutyApp")) {

                                    key.add(response.body().getData().get(i).getKey());
                                    value.add(response.body().getData().get(i).getValue());
                                }

                            }
                            if(response.body().getData().get(i).getKey().equals("HideCameraForMarkAttendance") || response.body().getData().get(i).getKey().equals("Remark")){
                                key1.add(response.body().getData().get(i).getKey());
                               // Log.e("key",response.body().getData().get(i).getKey());
                                value1.add(response.body().getData().get(i).getValue());
                               // Log.e("value",response.body().getData().get(i).getValue());

                            }
                            if(response.body().getData().get(i).getKey().equals("HideDiscrepancies") || response.body().getData().get(i).getKey().equals("HideApplicationStatus")|| response.body().getData().get(i).getKey().equals("HideShiftAllocation")|| response.body().getData().get(i).getKey().equals("HideAttendanceDetails")){
                                if(response.body().getData().get(i).getValue().equals("0")) {
                                    hideCameraKey.add(response.body().getData().get(i).getKey());
                                   // Log.e("key", response.body().getData().get(i).getKey());
                                    hideCameravalue.add(response.body().getData().get(i).getValue());
                                   // Log.e("value", response.body().getData().get(i).getValue());
                                }

                            }
                            if(response.body().getData().get(i).getKey().equals("HideMarkAttendance") ){
                                if(response.body().getData().get(i).getValue().equals("1")){
                                    button_mark_attendance.setVisibility(View.GONE);
                                }


                            }

                            if(response.body().getData().get(i).getKey().equals("AllowLoginUsingOTP")) {
                               // Log.e("getEmployeeCalled",response.body().getData().get(i).getValue());

                                EmpowerApplication.set_session("AllowLoginUsingOTP",response.body().getData().get(i).getValue());
                            }
                            if(response.body().getData().get(i).getKey().equals("AllowShifSelectionInAttendanceRegularizationApp")) {
                                // Log.e("getEmployeeCalled",response.body().getData().get(i).getValue());

                                EmpowerApplication.set_session("AllowShifSelectionInAttendanceRegularizationApp",response.body().getData().get(i).getValue());
                            }
                            if(response.body().getData().get(i).getKey().equals("RegularizationAppDayStatusAsPerShiftPolicy")) {
                                // Log.e("getEmployeeCalled",response.body().getData().get(i).getValue());

                                EmpowerApplication.set_session("RegularizationAppDayStatusAsPerShiftPolicy",response.body().getData().get(i).getValue());
                            }



                        }
                        setUpNavigationView();


                        if(value.size()==1){

                            //for(int i=0;i<key.size();i++) {

                            //if(key.get(i).equals("HideLeaveApp"))

                            stub.setLayoutResource(R.layout.dynamic_application_layout_one);
                            View inflated = stub.inflate();
                            //TextView textView = inflated.findViewById(R.id.te_xt);
                            if(key.get(0).equals("HideLeaveApp")) {
                                TextView textView = inflated.findViewById(R.id.te_xt);
                                textView.setText("Leave Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideCompOffApp")) {
                                TextView textView1 = inflated.findViewById(R.id.te_xt);
                                textView1.setText("CompOff Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideRegularizationApp")) {
                                TextView textView2 = inflated.findViewById(R.id.te_xt);
                                textView2.setText("Regularization Application");
                                textView2.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideWFHApp")) {
                                TextView textView3 = inflated.findViewById(R.id.te_xt);
                                textView3.setText("WFH Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideOutDutyApp")) {
                                TextView textView4 = inflated.findViewById(R.id.te_xt);
                                textView4.setText("OutDuty Application");
                                textView4.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }




                        }
                        if(value.size()==2){

                            stub.setLayoutResource(R.layout.dynamic_application_layout_two);
                            View inflated = stub.inflate();
                            TextView textView=inflated.findViewById(R.id.te_xt);
                            //textView.setText("Leave Application");
                            if(key.get(0).equals("HideLeaveApp")) {
                                textView.setText("Leave Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideCompOffApp")) {
                                textView.setText("CompOff Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideRegularizationApp")) {
                                textView.setText("Regularization Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideWFHApp")) {
                                textView.setText("WFH Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideOutDutyApp")) {
                                textView.setText("OutDuty Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            //textView1.setText("Regularization Application");
                            if(key.get(1).equals("HideLeaveApp")) {
                                textView1.setText("Leave Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideCompOffApp")) {
                                textView1.setText("CompOff Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideRegularizationApp")) {
                                textView1.setText("Regularization Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideWFHApp")) {
                                textView1.setText("WFH Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideOutDutyApp")) {
                                textView1.setText("OutDuty Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }


                        }
                        if(value.size()==3){

                            stub.setLayoutResource(R.layout.daynamic_application_three);
                            View inflated = stub.inflate();
                            TextView textView=inflated.findViewById(R.id.te_xt);
                           /* textView.setText("Leave Application");
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            textView1.setText("Regularization Application");*/
                            if(key.get(0).equals("HideLeaveApp")) {
                                textView.setText("Leave Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideCompOffApp")) {
                                textView.setText("CompOff Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideRegularizationApp")) {
                                textView.setText("Regularization Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideWFHApp")) {
                                textView.setText("WFH Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideOutDutyApp")) {
                                textView.setText("OutDuty Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            //textView1.setText("Regularization Application");
                            if(key.get(1).equals("HideLeaveApp")) {
                                textView1.setText("Leave Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideCompOffApp")) {
                                textView1.setText("CompOff Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideRegularizationApp")) {
                                textView1.setText("Regularization Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideWFHApp")) {
                                textView1.setText("WFH Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideOutDutyApp")) {
                                textView1.setText("OutDuty Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            TextView textView3=inflated.findViewById(R.id.te_xt2);
                            //textView3.setText("OutDuty Application");
                            if(key.get(2).equals("HideLeaveApp")) {
                                textView3.setText("Leave Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideCompOffApp")) {
                                textView3.setText("CompOff Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideRegularizationApp")) {
                                textView3.setText("Regularization Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideWFHApp")) {
                                textView3.setText("WFH Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideOutDutyApp")) {
                                textView3.setText("OutDuty Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }


                        }
                        if(value.size()==4){

                            stub.setLayoutResource(R.layout.dynamic_application_layout);
                            View inflated = stub.inflate();
                            TextView textView=inflated.findViewById(R.id.te_xt);
                            /*textView.setText("Leave Application");
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            textView1.setText("Regularization Application");
                            TextView textView3=inflated.findViewById(R.id.te_xt2);
                            textView3.setText("OutDuty Application");*/

                            if(key.get(0).equals("HideLeaveApp")) {
                                textView.setText("Leave Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideCompOffApp")) {
                                textView.setText("CompOff Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideRegularizationApp")) {
                                textView.setText("Regularization Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideWFHApp")) {
                                textView.setText("WFH Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(0).equals("HideOutDutyApp")) {
                                textView.setText("OutDuty Application");
                                textView.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            //textView1.setText("Regularization Application");
                            if(key.get(1).equals("HideLeaveApp")) {
                                textView1.setText("Leave Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideCompOffApp")) {
                                textView1.setText("CompOff Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideRegularizationApp")) {
                                textView1.setText("Regularization Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideWFHApp")) {
                                textView1.setText("WFH Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(1).equals("HideOutDutyApp")) {
                                textView1.setText("OutDuty Application");
                                textView1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            TextView textView3=inflated.findViewById(R.id.te_xt2);
                            //textView3.setText("OutDuty Application");
                            if(key.get(2).equals("HideLeaveApp")) {
                                textView3.setText("Leave Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideCompOffApp")) {
                                textView3.setText("CompOff Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideRegularizationApp")) {
                                textView3.setText("Regularization Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideWFHApp")) {
                                textView3.setText("WFH Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(2).equals("HideOutDutyApp")) {
                                textView3.setText("OutDuty Application");
                                textView3.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }

                            TextView textView4=inflated.findViewById(R.id.te_xt3);
                            //textView4.setText("CompOff Application");
                            if(key.get(3).equals("HideLeaveApp")) {
                                textView4.setText("Leave Application");
                                textView4.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Leave_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(3).equals("HideCompOffApp")) {
                                textView4.setText("CompOff Application");
                                textView4.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), CompoffActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(3).equals("HideRegularizationApp")) {
                                textView4.setText("Regularization Application");
                                textView4.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Attend_Regularization.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(3).equals("HideWFHApp")) {
                                textView4.setText("WFH Application");
                                textView4.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }
                            if(key.get(3).equals("HideOutDutyApp")) {
                                textView4.setText("OutDuty Application");
                                textView4.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                        getContext().startActivity(i);
                                    }

                                });
                            }


                        }

                        if(value.size()==5) {

                            stub.setLayoutResource(R.layout.applicationfooter_wfh);
                            View inflated = stub.inflate();
                            leave_app_btn= inflated.findViewById(R.id.btn_leave_app);
                            attd_reg_btn= inflated.findViewById(R.id.btn_attd_reg);
                            out_duty_btn= inflated.findViewById(R.id.btn_out_duty);
                            app_comp_off= inflated.findViewById(R.id.btn_comp_off);
                            app_wfh= inflated.findViewById(R.id.btn_wfh);


                            leave_app_btn.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i=new Intent(getContext(), Leave_Application.class);
                                    getContext().startActivity(i);
                                }
                            });

                            attd_reg_btn.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i=new Intent(getContext(), Attend_Regularization.class);
                                    getContext().startActivity(i);
                                }
                            });


                            out_duty_btn.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i=new Intent(getContext(), Out_Duty_Application.class);
                                    getContext().startActivity(i);
                                }

                            });

                            app_comp_off.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i=new Intent(getContext(), CompoffActivity.class);
                                    getContext().startActivity(i);
                                }

                            });
       /* button_mark_attendance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getContext(), Attendance_cameraActivity.class);
                getContext().startActivity(i);
            }

        });*/
                            app_wfh.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i=new Intent(getContext(), WorkFromHomeActivity.class);
                                    getContext().startActivity(i);
                                }

                            });
                            //TextView textView = inflated.findViewById(R.id.te_xt);
                            // TextView textView1 = inflated.findViewById(R.id.te_xt1);
                            // TextView textView3 = inflated.findViewById(R.id.te_xt2);
                            // TextView textView4 = inflated.findViewById(R.id.te_xt3);


                            /*textView.setText("Leave Application");
                            TextView textView1=inflated.findViewById(R.id.te_xt1);
                            textView1.setText("Regularization Application");
                            TextView textView3=inflated.findViewById(R.id.te_xt2);
                            textView3.setText("OutDuty Application");*/

                           /* if (key.contains("HideLeaveApp")) {
                                textView.setText("Leave Application");
                            }
                            if (key.contains("HideCompOffApp")) {
                                textView1.setText("CompOff Application");
                            }
                            if (key.contains("HideRegularizationApp")) {
                                textView3.setText("Regularization Application");
                            }
                            if (key.contains("HideWFHApp")) {
                                textView4.setText("WFH Application");
                            }
                            if (key.contains("HideOutDutyApp")) {
                                textView.setText("OutDuty Application");
                            }*/

                        }

                    } else {
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
            public void onFailure(retrofit2.Call<EmployeeConfig> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());


                        EmpowerApplication.alertdialog(t.getMessage(), getContext());



            }
        });
    }


    public void getPendingAprovalData(String employeeid) {


        Map<String,String> pendingdata=new HashMap<String,String>();

        pendingdata.put("employeeId",employeeid);

        retrofit2.Call<CommanResponsePojo> call = apiService.getPendingApprovals(pendingdata);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {


                if(response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {


                        pendingAproval.setText(response.body().getData());

                    } else {
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
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());


                EmpowerApplication.alertdialog(t.getMessage(), getContext());



            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            // stub = (ViewStub)view.findViewById(R.id.layout_stub);

            return false;
        }
        if (id == R.id.action_sync) {
           // stub = (ViewStub)view.findViewById(R.id.layout_stub);

            currentDate = Calendar.getInstance();
            getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), sdf1.format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));;



            if(Utilities.isNetworkAvailable(this.getContext())) {
                getEmployeeConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

                getPendingAprovalData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            }else {
                Toast.makeText(this.getContext(),"No Internet Connection...",Toast.LENGTH_LONG).show();
            }


            return true;
        }
        return false;
    }

    private void setUpNavigationView() {
        final Menu menu = navigationView.getMenu();
        //menu.removeGroup();
        if (hideCameraKey.contains("HideApplicationStatus"))

        {

            menu.removeItem(1);
            MenuItem item = menu.add(R.id.group1, 1, 1, "APPLICATION STATUS");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource

        }

        if (hideCameraKey.contains("HideAttendanceDetails"))

        {
            menu.removeItem(2);
            MenuItem item = menu.add(R.id.group1, 2, 2, "ATTENDANCE DETAILS");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource

        }
        if (hideCameraKey.contains("HideShiftAllocation"))

        {
            menu.removeItem(3);
            MenuItem item = menu.add(R.id.group1, 3, 3, "SHIFT ALLOCATION");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource
            //menu.add("SHIFT ALLOCATION").setIcon(R.drawable.ic_action_clock);
        }
        if (hideCameraKey.contains("HideDiscrepancies"))

        {
            menu.removeItem(4);
            MenuItem item = menu.add(R.id.group1, 4, 4, "DISCREPANCIES");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource
            //menu.add("SHIFT ALLOCATION").setIcon(R.drawable.ic_action_clock);
        }
        /*if (true)

        {
            menu.removeItem(5);
            MenuItem item = menu.add(R.id.group1, 5, 5, "ABOUT US");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource
            //menu.add("SHIFT ALLOCATION").setIcon(R.drawable.ic_action_clock);
        }*/
        if (true)

        {
            menu.removeItem(6);
            MenuItem item = menu.add(R.id.group1, 6, 6, "LOGOUT");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource
            //menu.add("SHIFT ALLOCATION").setIcon(R.drawable.ic_action_clock);
        }


        if (true)

        {
            menu.removeItem(5);
            MenuItem item = menu.add(R.id.group1, 5, 5, "CHANGE PASSWORD");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource
            //menu.add("SHIFT ALLOCATION").setIcon(R.drawable.ic_action_clock);
        }

        /*if (true)

        {
            menu.removeItem(7);
            MenuItem item = menu.add(R.id.group1, 7, 7, "EMPLOYEE SELECTION");
            item.setIcon(R.drawable.ic_action_clock); // add icon with drawable resource
            //menu.add("SHIFT ALLOCATION").setIcon(R.drawable.ic_action_clock);
        }*/
    }



    public void getCalenderData(String empid, final String date, String token) {


        pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();


        final Map<String,String> calenderdata=new HashMap<String,String>();
        calenderdata.put("employeeId",empid);
        calenderdata.put("date",date);
        calenderdata.put("token",token);
        Log.e("DateString_token",token);
        final ArrayList<String> calenderdate=new ArrayList<>();
        daystatus=new ArrayList<>();

        retrofit2.Call<ClaenderPoJo> call = apiService.getCalenderData(calenderdata);
        call.enqueue(new Callback<ClaenderPoJo>() {
            @Override
            public void onResponse(retrofit2.Call<ClaenderPoJo> call, Response<ClaenderPoJo> response) {

                pd.dismiss();

                if(response.isSuccessful()) {
                    //Log.d("User ID: ", response.body().getMessage());

                    if (response.body().getStatus().equals("1")) {
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                        Log.e("responsedata", response.body().getStatus());
                        int count = response.body().getData().size();
                        for (int i = 0; i < count; i++) {
                            Log.e("arraydata1", String.valueOf(response.body().getData().get(i).getDayStatusCode()));
                            Log.e("arraydata2", String.valueOf(response.body().getData().get(i).getShiftDate()));
                            calenderdate.add(response.body().getData().get(i).getShiftDate());
                            daystatus.add(response.body().getData().get(i).getDayStatusCode());


                        }

                       CalendarView.grid.setAdapter(new CalendarAdapter(getContext(), currentDate, cells, daystatus));




                        Toast.makeText(getContext(),"Refreshing Calender "+ response.body().getMessage(), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(RegistrationActivity.this,LicenseActivity.class));

                    } else {
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
            public void onFailure(retrofit2.Call<ClaenderPoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();

            }
        });
    }


}


