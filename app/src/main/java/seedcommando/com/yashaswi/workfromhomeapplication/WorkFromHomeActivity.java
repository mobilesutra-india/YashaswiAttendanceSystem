package seedcommando.com.yashaswi.workfromhomeapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import seedcommando.com.yashaswi.ExceptionHandler;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 1/31/2018.
 */

public class WorkFromHomeActivity extends AppCompatActivity  {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //WorkFromHomeInDays workFromHomeInDays;
    //WorkFromHomeInHrs workFromHomeInHrs;
    static int position;
    ProgressDialog pd;
    Bundle bundle;
    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.out_duty_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Work From Home Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);
        Bundle  extras = getIntent().getExtras();
        String value;
        if (extras != null) {
            value = extras.getString("Date");
             bundle = new Bundle();
            bundle.putString("Date", value);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

    }


    private void setupViewPager(ViewPager viewPager) {
     ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        WorkFromHomeInDays workFromHomeInDays=new WorkFromHomeInDays();
        workFromHomeInDays.setArguments(bundle);
        adapter.addFragment(workFromHomeInDays, "WFH  In Days");
        WorkFromHomeInHrs workFromHomeInHrs=new WorkFromHomeInHrs();
        workFromHomeInDays.setArguments(bundle);
        adapter.addFragment(workFromHomeInHrs, "WFH  In hours");
        //viewPager.setOffscreenPageLimit(1);

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

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment


                    /*outDutyDays=new OutDutyDays();
                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           outDutyDays.submitCheck();
                            if(!outDutyDays.getInDate().isEmpty()&&!outDutyDays.getOutDate().isEmpty()&&outDutyDays.isVisible()) {
                                outDutyDays.caldays();
                                if(OutDutyDays.outdutyflag){

                                }
                            }else {
                                Toast.makeText(Out_Duty_Application.this,"please select in_date",Toast.LENGTH_LONG).show();
                            }



                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

*/
                    return mFragmentList.get(position);


                case 1: // Fragment # 0 - This will show FirstFragment different title

                    /*outDutyHours=new OutDutyHours();
                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            outDutyHours.submitCheck();
                            if (!outDutyHours.getFromTime().isEmpty() && !outDutyHours.getToTime().isEmpty())
                            {


                                String resultcampare = outDutyHours.CompareTime(outDutyHours.getFromTime(), outDutyHours.getToTime());
                                if (!resultcampare.equals("1")) {


                                    EmpowerApplication.alertdialog(outDutyHours.for_out_time, Out_Duty_Application.this);

                                }else {
                                    outDutyHours.calhrs();

                                }


                            }


                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });*/

                    return mFragmentList.get(position);

                default:
                    return null;
            }
            //return mFragmentList.get(position);
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return super.isViewFromObject(view, object);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setVisible(false);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   /* public interface OnFragmentVisibleListener {
        public void FragmentVisible(boolean visible);
    }*/




}



