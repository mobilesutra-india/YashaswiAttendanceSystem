package seedcommando.com.yashaswi.applicationaprovels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import seedcommando.com.yashaswi.ExceptionHandler;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.WorkFromHome.WorkFromHomeSetDataPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 4/11/2018.
 */

public class ApplicationAprovelsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static int position;
    ProgressDialog pd;
    private ApiInterface apiService;
    ArrayList<WorkFromHomeSetDataPoJo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.aprovelflow_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Application Approvals");
        toolbar.setLogo(R.drawable.yashaswi_logo);
        /*TextView textView = (TextView) findViewById(R.id.title);
        textView.setText("Application Status");*/
        apiService = ApiClient.getClient().create(ApiInterface.class);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        /*OUTDUTY
                    REGULARIZATION
            COMPENSATORYOFF

            WORKFROMHOME*/


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
        Bundle  extras = getIntent().getExtras();
        try {
            if (extras != null) {
                //String page = getIntent().getIntExtra("ARG_PAGE", "Leave Application");
                String page = getIntent().getStringExtra("notificationType");
                if (page.equals("LEAVE")) {
                    viewPager.setCurrentItem(0);
                }
                if (page.equals("REGULARIZATION")) {
                    viewPager.setCurrentItem(1);
                }
                if (page.equals("OUTDUTY") || page.equals("OUTDUTY")) {
                    viewPager.setCurrentItem(2);
                }
                if (page.equals("COMPENSATORYOFF")) {
                    viewPager.setCurrentItem(3);
                }
                if (page.equals("WORKFROMHOME") || page.equals("WORKFROMHOME")) {
                    viewPager.setCurrentItem(4);
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

       /* Bundle  extras = getIntent().getExtras();
        if (extras != null) {
            int defaultValue = 0;
            int page = getIntent().getIntExtra("ARG_PAGE", defaultValue);
            viewPager.setCurrentItem(page);

        }*/


    }
    private void setupViewPager(ViewPager viewPager) {
       ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AprovelForLeave(), "LEAVE");
       adapter.addFragment(new AprovelForReg(), "REGULARIZATION");
        adapter.addFragment(new AprovelForOutDuty(), "OUT DUTY");
        adapter.addFragment(new AprovelForCompoff(), "COMP OFF");
        adapter.addFragment(new AprovelFroWHF(), "WORK FROM HOME");
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


}
