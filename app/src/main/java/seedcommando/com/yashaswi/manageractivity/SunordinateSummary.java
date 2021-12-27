package seedcommando.com.yashaswi.manageractivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SubOrdinateSummaryPojo.PresentNamePoJo;
import seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SubOrdinateSummaryPojo.SubOrdinatePoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 6/1/2018.
 */

public class SunordinateSummary extends Fragment {

    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    private ImageView btnPrev;
    private ImageView btnNext;
    TextView txtDate;
    ProgressDialog pd;
    TableLayout tableLayout;
    private ApiInterface apiService;
    ArrayList<String> ss;
    ArrayList<String> hd;
    ArrayList<String> a;
    ArrayList<String> od;
    ArrayList<String> p;
    ArrayList<String> ss1;
    ArrayList<String> hd1;
    ArrayList<String> a1;
    ArrayList<String> od1;
    ArrayList<String> p1;
    ArrayList<String> h;
    ArrayList<String> h1;
    ArrayList<String> name_a;
    ArrayList<PresentNamePoJo> name_all;
    TableRow row,row1,row2,row3,row4,row5,row6;

    // default date format
    private static final String DATE_FORMAT_calender = "dd-MMM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_calender, Locale.US);
    private Calendar currentDate = Calendar.getInstance();

    public SunordinateSummary() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sub_ordinate_day_status, container, false);

        pieChart = rootView. findViewById(R.id.chart1);
        btnPrev = rootView.findViewById(R.id.calendar_prev_button);
        btnNext = rootView.findViewById(R.id.calendar_next_button);
        txtDate = rootView.findViewById(R.id.calendar_date_display);
        tableLayout= rootView.findViewById(R.id.tableLayout12);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();
       // pieChart.setDrawHoleEnabled(true);
        //pieChart.setHoleRadius(0);
        pieChart.setDescription(null);
        pieChart.getLegend().setEnabled(false);
       // pieChart.setContentDescription("");

       // pieChart.getDescription().setEnabled(false);

        assignClickHandlers();
        updateLabel();





        return rootView;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            currentDate.set(Calendar.YEAR, year);
            currentDate.set(Calendar.MONTH, monthOfYear);
            currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void AddValuesToPIEENTRY(){
        if(a.size()>0) {

            entries.add(new BarEntry(a.size(), 0));

        }
        if(p.size()>0) {
            entries.add(new BarEntry( p.size(),1));
        }
        if(ss.size()>0) {
        entries.add(new BarEntry(  ss.size(),2));
        }
        if(od.size()>0) {
        entries.add(new BarEntry(  od.size(),3));
        }
        if(hd.size()>0) {
            entries.add(new BarEntry( hd.size(),4));
        }
        if(h.size()>0) {
            entries.add(new BarEntry( h.size(),5));
        }
        //entries.add(new BarEntry(3f,  p.size()));

    }

    public void AddValuesToPieEntryLabels(){

        if(a.size()>0) {
            PieEntryLabels.add("Absent");
        }
        if(p.size()>0) {
        PieEntryLabels.add("Present");
        }
        if(ss.size()>0) {
        PieEntryLabels.add("Compoff");
        }
        if(od.size()>0) {
        PieEntryLabels.add("Leave");
        }
        if(hd.size()>0) {
            PieEntryLabels.add("Week Off");
        }
        if(h.size()>0) {
            PieEntryLabels.add("Holiday");
        }
        //PieEntryLabels.add("June");

    }
    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.DATE, 1);
                //updateCalendar();
                updateLabel();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.DATE, -1);
                updateLabel();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        //String myFormat = "MMMyy"; //In which you need put here

        txtDate.setText(sdf.format(currentDate.getTime()));
        //if(this.isVisible()) {
       if(HomeFragment.position==2) {

            if (Utilities.isNetworkAvailable(getContext())) {
                getSubOrdinateSummary_Data(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),txtDate.getText().toString(),txtDate.getText().toString());
                // getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
            } else {
                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
            }
        }
      //  }
        //getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {


            if (isVisibleToUser) {
                currentDate = Calendar.getInstance();
                // Load your data here or do network operations here
                assignClickHandlers();
                updateLabel();


            }
        }
    }


    public void getSubOrdinateSummary_Data(String empid, final String date, String date1) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();

        final Map<String,String> summarydata=new HashMap<String,String>();
        summarydata.put("employeeId",empid);
        summarydata.put("fromDate",date);
        summarydata.put("toDate",date1);

        p=new ArrayList<>();
        ss=new ArrayList<>();
        a=new ArrayList<>();
        hd=new ArrayList<>();
        od=new ArrayList<>();
        p1=new ArrayList<>();
        ss1=new ArrayList<>();
        a1=new ArrayList<>();
        hd1=new ArrayList<>();
        od1=new ArrayList<>();
        h=new ArrayList<>();
        h1=new ArrayList<>();
        name_a=new ArrayList<>();
        name_all=new ArrayList<>();
      /*ArrayList  name_p=new ArrayList<>();
        final ArrayList name_co=new ArrayList<>();
        ArrayList name_wo=new ArrayList<>();
        ArrayList name_l=new ArrayList<>();
        ArrayList name_h=new ArrayList<>();*/
        final ArrayList<PresentNamePoJo> presentdata=new ArrayList<>();


        retrofit2.Call<SubOrdinatePoJo> call = apiService.getSubOrdinateSummaryData(summarydata);
        call.enqueue(new Callback<SubOrdinatePoJo>() {
            @Override
            public void onResponse(retrofit2.Call<SubOrdinatePoJo> call, Response<SubOrdinatePoJo> response) {

                pd.dismiss();
                p.clear();
                ss.clear();
                a.clear();
                hd.clear();
                od.clear();
                p1.clear();
                ss1.clear();
                a1.clear();
                hd1.clear();
                od1.clear();
                name_all.clear();
                presentdata.clear();
                entries.clear();
                PieEntryLabels.clear();
                tableLayout.removeView(row);
                tableLayout.removeView(row1);
                tableLayout.removeView(row2);
                tableLayout.removeView(row3);
                tableLayout.removeView(row4);
                tableLayout.removeView(row5);
                tableLayout.removeView(row6);
                //Log.d("User ID: ", response.body().getMessage());
                if(response.isSuccessful()) {

                    if (response.body().getStatus().equals("1")) {
                        Log.e("responsedata", response.body().getStatus());
                        for (seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SubOrdinateSummaryPojo.Data obj:response.body().getData()) {
                            //name_all.add(obj.getEmployeeName());

                            PresentNamePoJo presentNamePoJo1=new PresentNamePoJo();
                            presentNamePoJo1.setName(obj.getEmployeeName());


                            if(obj.getStatus().equals("A")){
                                a.add(obj.getStatus());
                                a1.add(obj.getEmployeeName());
                                name_a.add(obj.getEmployeeName());
                                presentNamePoJo1.setInTime("Absent");
                            }
                            if(obj.getStatus().equals("P") ||obj.getStatus().equals("HD")||obj.getStatus().equals("HD1")||obj.getStatus().equals("HD2")||obj.getStatus().equals("SS")||obj.getStatus().equals("OD")||obj.getStatus().equals("LM")||obj.getStatus().equals("EG")||obj.getStatus().equals("HP")||obj.getStatus().equals("WOP")||obj.getStatus().equals("WFH")){
                                PresentNamePoJo presentNamePoJo=new PresentNamePoJo();
                                presentNamePoJo.setName(obj.getEmployeeName());
                                presentNamePoJo1.setInTime("Present");
                               SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy hh:mmaa");
                              SimpleDateFormat  format1 = new SimpleDateFormat("hh:mmaa");
                                try {
                                    if(obj.getInTime() == null || obj.getInTime() == "")
                                    {
                                        presentNamePoJo.setInTime("-");

                                    }
                                    else
                                    {
                                        Log.e("in time",obj.getInTime());
                                        Date date = format.parse(obj.getInTime());
                                        presentNamePoJo.setInTime(format1.format(date));

                                    }
                                    //obj.getOutTime() == null || obj.getOutTime() == ""
                                    if(obj.getOutTime() == null || obj.getOutTime() == "")
                                    {
                                        presentNamePoJo.setOutTime("-");

                                    }
                                    else
                                    {
                                       // Log.e("in time",obj.getInTime());

                                        Date date1 = format.parse(obj.getOutTime());
                                        presentNamePoJo.setOutTime(format1.format(date1));

                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                presentdata.add(presentNamePoJo);

                                p.add(obj.getStatus());
                                p1.add(obj.getEmployeeName());
                            }
                            if(obj.getStatus().equals("CO")){
                                ss.add(obj.getStatus());
                                ss1.add(obj.getEmployeeName());
                                presentNamePoJo1.setInTime("Compoff");
                                //name_co.add()
                            }
                            if(obj.getStatus().equals("L")||obj.getStatus().equals("HPL")||obj.getStatus().equals("HAL")){
                                od.add(obj.getStatus());
                                od1.add(obj.getEmployeeName());
                                presentNamePoJo1.setInTime("Leave");
                            }
                            if(obj.getStatus().equals("WO")){
                                hd.add(obj.getStatus());
                                hd1.add(obj.getEmployeeName());
                                presentNamePoJo1.setInTime("Week Off");
                            }
                            if(obj.getStatus().equals("H")){
                                h.add(obj.getStatus());
                                h1.add(obj.getEmployeeName());
                                presentNamePoJo1.setInTime("Holiday");
                            }
                            name_all.add(presentNamePoJo1);

                        }
                        Log.e("A", String.valueOf(a.size()));
                        Log.e("P", String.valueOf(p.size()));
                        Log.e("SS", String.valueOf(ss.size()));
                        Log.e("OD", String.valueOf(od.size()));
                        Log.e("HD", String.valueOf(hd.size()));


                        AddValuesToPIEENTRY();

                        AddValuesToPieEntryLabels();

                        pieDataSet = new PieDataSet(entries, "");

                        pieData = new PieData(PieEntryLabels, pieDataSet);

                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        pieChart.setData(pieData);

                        pieChart.animateY(3000);
                        //pieChart.setTransparentCircleColor(Color.WHITE);



                        if(a.size()>0) {

                             row= new TableRow(getContext());
                            row.setBackgroundResource(R.drawable.border);

                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(18);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("ABSENT");
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setTextSize(18);
                            qty.setBackgroundResource(R.drawable.border);
                            int count=a.size();
                             Log.e("size", String.valueOf(a.size()));
                             qty.setText(String.valueOf(count));
                            row.addView(tv);

                            row.addView(qty);

                            tableLayout.addView(row);

                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView2.setText(String.valueOf(a.size()));

                                    textView1.setText(txtDate.getText().toString());
                                    // setUpExpList();

                                    textView.setText("ABSENT Team Members");
                                     Collections.sort(name_a);
                                    final NameListAdapter adapter = new NameListAdapter(getContext(), name_a);
                                    listView.setAdapter(adapter);
                                    dialog.show();
                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                        if(p.size()>0) {

                             row1= new TableRow(getContext());
                            row1.setBackgroundResource(R.drawable.border);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setTextSize(18);
                            tv.setGravity(Gravity.CENTER);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("PRESENT");
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setBackgroundResource(R.drawable.border);
                            qty.setTextSize(18);
                            int count=p.size();
                            Log.e("size", String.valueOf(a.size()));
                            qty.setText(String.valueOf(count));
                            row1.addView(tv);

                            row1.addView(qty);

                            tableLayout.addView(row1);
                            row1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());

                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    Window window = dialog.getWindow();
                                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    // setUpExpList();
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView2.setText(String.valueOf(p.size()));
                                    textView1.setText(txtDate.getText().toString());
                                    textView.setText("PRESENT Team Members");
                                    //Collections.sort(presentdata);
                                   // ArrayList<PresentNamePoJo> infos = new ArrayList<StudentInformation>();
// fill array
                                    //Collections.sort(presentdata, Comparator.comparing((PresentNamePoJo::getName));
                                    Collections.sort(presentdata, new Comparator<PresentNamePoJo>() {
                                        @Override
                                        public int compare(PresentNamePoJo o1, PresentNamePoJo o2) {
                                            return o1.getName().compareTo(o2.getName());
                                        }
                                    });
                                    final PresentNameListAdapter adapter = new PresentNameListAdapter(getContext(), presentdata);
                                    listView.setAdapter(adapter);
                                    dialog.show();
                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                        if(od.size()>0) {

                             row2= new TableRow(getContext());
                            row2.setBackgroundResource(R.drawable.border);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row2.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(18);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("LEAVE");
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setTextSize(18);
                            qty.setBackgroundResource(R.drawable.border);
                            int count=od.size();
                            Log.e("size", String.valueOf(a.size()));
                            qty.setText(String.valueOf(count));
                            row2.addView(tv);

                            row2.addView(qty);

                            tableLayout.addView(row2);

                            row2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    // setUpExpList();
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView2.setText(String.valueOf(od.size()));
                                    textView1.setText(txtDate.getText().toString());
                                    textView.setText("LEAVE Team Members");
                                    Collections.sort(od1);
                                    final NameListAdapter adapter = new NameListAdapter(getContext(), od1);
                                    listView.setAdapter(adapter);
                                    dialog.show();
                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                        if(hd.size()>0) {

                             row3= new TableRow(getContext());
                            row3.setBackgroundResource(R.drawable.border);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row3.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("WEEK OFF");
                            tv.setTextSize(18);
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setTextSize(18);
                            qty.setBackgroundResource(R.drawable.border);
                            int count=hd.size();
                            Log.e("size", String.valueOf(a.size()));
                            qty.setText(String.valueOf(count));
                            row3.addView(tv);

                            row3.addView(qty);

                            tableLayout.addView(row3);

                            row3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    // setUpExpList();
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView2.setText(String.valueOf(hd.size()));
                                    textView1.setText(txtDate.getText().toString());
                                    textView.setText("WEEK OFF Team Members");
                                    Collections.sort(hd1);
                                    final NameListAdapter adapter = new NameListAdapter(getContext(), hd1);
                                    listView.setAdapter(adapter);
                                    dialog.show();
                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                        if(ss.size()>0) {

                             row4= new TableRow(getContext());
                            row4.setBackgroundResource(R.drawable.border);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row4.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(18);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("COMP OFF");
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setTextSize(18);
                            qty.setBackgroundResource(R.drawable.border);
                            int count=ss.size();
                            Log.e("size", String.valueOf(a.size()));
                            qty.setText(String.valueOf(count));
                            row4.addView(tv);

                            row4.addView(qty);

                            tableLayout.addView(row4);
                            row4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    // setUpExpList();
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView2.setText(String.valueOf(ss.size()));
                                    textView1.setText(txtDate.getText().toString());
                                    textView.setText("COMP OFF Team Members");
                                    Collections.sort(ss1);
                                    final NameListAdapter adapter = new NameListAdapter(getContext(), ss1);
                                    listView.setAdapter(adapter);
                                    dialog.show();
                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }

                        if(h.size()>0) {

                            row5= new TableRow(getContext());
                            row5.setBackgroundResource(R.drawable.border);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row5.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("HOLIDAY");
                            tv.setTextSize(18);
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setBackgroundResource(R.drawable.border);
                            int count=h.size();
                            Log.e("size", String.valueOf(h.size()));
                            qty.setText(String.valueOf(count));
                            qty.setTextSize(18);
                            row5.addView(tv);

                            row5.addView(qty);

                            tableLayout.addView(row5);
                            row5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    // setUpExpList();
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView2.setText(String.valueOf(h.size()));
                                    textView1.setText(txtDate.getText().toString());
                                    textView.setText("HOLIDAY Team Members");
                                    Collections.sort(h1);
                                    final NameListAdapter adapter = new NameListAdapter(getContext(), h1);
                                    listView.setAdapter(adapter);
                                    dialog.show();
                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }

                        if(h.size()>0 ||ss.size()>0||od.size()>0 ||hd.size()>0||a.size()>0|| p.size()>0) {

                            row6= new TableRow(getContext());
                            row6.setBackgroundResource(R.drawable.border);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row6.setLayoutParams(lp);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER);
                            tv.setBackgroundResource(R.drawable.border);
                            tv.setText("Total");
                           tv.setTextSize(18);
                            tv.setTextColor(Color.BLACK);
                            tv.setTypeface(null, Typeface.BOLD);
                            TextView  qty = new TextView(getContext());
                            qty.setGravity(Gravity.CENTER);
                            qty.setBackgroundResource(R.drawable.border);
                            int count= h.size()+ss.size()+od.size()+hd.size()+a.size()+ p.size();
                            Log.e("size", String.valueOf(h.size()+ss.size()+od.size()>+hd.size()+a.size()+ p.size()));
                            qty.setText(String.valueOf(count));
                            qty.setTypeface(null, Typeface.BOLD);
                            qty.setTextColor(Color.BLACK);
                            qty.setTextSize(18);
                            row6.addView(tv);

                            row6.addView(qty);

                            tableLayout.addView(row6);
                            row6.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setContentView(R.layout.list_member_of_subordinate);
                                    Window window = dialog.getWindow();
                                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    final ListView listView = dialog.findViewById(R.id.list);
                                    final TextView textView = dialog.findViewById(R.id.textView_namelist);
                                    // setUpExpList();
                                    final TextView textView1 = dialog.findViewById(R.id.textView_date);
                                    final TextView textView2 = dialog.findViewById(R.id.show_count);
                                    textView1.setText(txtDate.getText().toString());
                                    textView.setText("Total Team Members");
                                    textView2.setText(String.valueOf(name_all.size()));
                                    final TotalEmployeeAdapter adapter = new TotalEmployeeAdapter(getContext(), name_all);
                                    listView.setAdapter(adapter);
                                    dialog.show();

                                    Button declineButton = dialog.findViewById(R.id.button_ok);
                                    declineButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }






                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();


                    } else {
                       // if(!response.body().getMessage().equals(EmpowerApplication.ForSessionExpire)) {

                            EmpowerApplication.alertdialog(response.body().getMessage(), getContext());
                       // }

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
            public void onFailure(retrofit2.Call<SubOrdinatePoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                //pd.dismiss();

            }
        });
    }







}
