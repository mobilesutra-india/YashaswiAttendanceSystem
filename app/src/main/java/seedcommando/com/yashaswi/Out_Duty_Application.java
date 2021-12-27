package seedcommando.com.yashaswi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import seedcommando.com.yashaswi.outdutyapplication.OutDutyDays;
import seedcommando.com.yashaswi.outdutyapplication.OutDutyHours;

/**
 * Created by commando5 on 9/4/2017.
 */

public class Out_Duty_Application extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    //OutDutyDays outDutyDays;
    //OutDutyHours outDutyHours;
    Button apply,cancel;
    Bundle bundle;

    //ArrayList<String> reason=null;
   // ArrayList<String> reasonId=null;
    //ArrayList<String> shift=null;
    //ArrayList<String> shiftId=null;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.out_duty_application);

      Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Out duty Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);
        apply = findViewById(R.id.apply);
        cancel = findViewById(R.id.cancel);
        Bundle  extras = getIntent().getExtras();

        String value;
        if (extras != null) {
            value = extras.getString("Date");
            Log.e("outdutyDate",value);
            bundle = new Bundle();
            bundle.putString("Date", value);


        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        viewPager = findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        OutDutyDays outDutyDays=new OutDutyDays();
        outDutyDays.setArguments(bundle);

        adapter.addFragment(outDutyDays, "Out duty  In Days");
        OutDutyHours outDutyHours=new OutDutyHours();
        outDutyHours.setArguments(bundle);
        /*Bundle args1 = new Bundle();
        args1.putStringArrayList("array1", level1);
        args1.putStringArrayList("array2", level2);
        args1.putStringArrayList("array3", level3);
        args1.putStringArrayList("array4", level4);
        outDutyHours.setArguments(args1);*/
        adapter.addFragment(outDutyHours, "Out duty  In hours");
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
        return  true;

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

    public interface OnFragmentVisibleListener{
        void FragmentVisible(boolean visible);
    }





}