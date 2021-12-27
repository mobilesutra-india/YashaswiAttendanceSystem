package seedcommando.com.yashaswi.manageractivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.ManagerPoJo.ClaenderPoJo;
import seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SummaryPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 8/28/2017.
 */

public class AttendanceSummaryFragment extends Fragment {
    ListView listView;

    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate,txt_working_hrs_cal,txt_actual_hrs_cal,txt_extra_hrs_cal;
    Button p,a,l,od,ss,wop,co,lm,wfh ,h;
    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";
    // default date format
    private static final String DATE_FORMAT_calender = "dd-MMM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    // current displayed month
    private Calendar currentDate = Calendar.getInstance();
    ArrayList<WeekReportPojo> arrayList;
    float whr=0,ahr=0,ehr=0;
    ProgressDialog pd;
    boolean isFragmentLoaded=false;
    DateFormat format = new SimpleDateFormat("HH:mm",Locale.US);
    private ApiInterface apiService;
    private DatePickerDialog toDatePickerDialog;
   // static ArrayList<Integer> arrayList1=new ArrayList<>();
    //static ArrayList<Integer> arrayList2=new ArrayList<>();
    //static  ArrayList<Integer> arrayList3=new ArrayList<>();
    public AttendanceSummaryFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance_summary, container, false);
        listView = rootView.findViewById(R.id.list);
        btnPrev = rootView.findViewById(R.id.calendar_prev_button);
        btnNext = rootView.findViewById(R.id.calendar_next_button);
        txtDate = rootView.findViewById(R.id.calendar_date_display);
        p = rootView.findViewById(R.id.button2);
        a = rootView.findViewById(R.id.button3);
        l = rootView.findViewById(R.id.button4);
        od = rootView.findViewById(R.id.button5);
        wop = rootView.findViewById(R.id.button_wo);
        ss = rootView.findViewById(R.id.button_ss);
       co = rootView.findViewById(R.id.button_co);
       lm = rootView.findViewById(R.id.button_lm);
        wfh = rootView.findViewById(R.id.button_wfh);
        h = rootView.findViewById(R.id.button_h);





        //txtDate.setText(sdf.format(currentDate.getTime()));
        arrayList  = new ArrayList<WeekReportPojo>();
        arrayList.clear();
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

           //updateLabel();

        // Add a footer to the ListView
        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.list_view_footer,listView,false);
        txt_working_hrs_cal = footer.findViewById(R.id.textView_working_hrs_cal);
         txt_actual_hrs_cal = footer.findViewById(R.id.textView_actual_hrs_cal);
         txt_extra_hrs_cal = footer.findViewById(R.id.textView_extra_hrs_cal);

        // So, this footer is non selectable
        //listView.addFooterView(footer,null,false);
        listView.addFooterView(footer);


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
    private void updateLabel() {
        //String myFormat = "MMMyy"; //In which you need put here

        txtDate.setText(sdf.format(currentDate.getTime()));
        //if(this.isVisible()) {
            if(HomeFragment.position==1) {

                if (Utilities.isNetworkAvailable(getContext())) {
                    getSummary_Data(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                   // getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                } else {
                    Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                }
            }
       // }
        //getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()),EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
    }
    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                //updateCalendar();
                updateLabel();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
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

        /*txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                // TODO Auto-generated method stub
                *//*new DatePickerDialog(getContext(), date, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)).show();*//*

                toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        currentDate.add(newDate.MONTH, -1);
                        updateLabel();


                    }


                }
                        , newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


            }
        });*/
    }

    public void getSummary_Data(String empid, final String date, String token) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();

        final Map<String,String> summarydata=new HashMap<String,String>();
        summarydata.put("employeeId",empid);
        summarydata.put("token",token);
        summarydata.put("monthDate",date);
        final ArrayList<String> calenderdate=new ArrayList<>();
        //daystatus=new ArrayList<>();
        arrayList.clear();

        retrofit2.Call<SummaryPoJo> call = apiService.getSummaryData(summarydata);
        call.enqueue(new Callback<SummaryPoJo>() {
            @Override
            public void onResponse(retrofit2.Call<SummaryPoJo> call, Response<SummaryPoJo> response) {

                pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());
                if(response.isSuccessful()) {

                    Log.d("Sammary: ", new Gson().toJson(response.body()));


                    if (response.body().getStatus().equals("1")) {
                       // Log.e("responsedata", response.body().getStatus());
                        int count = response.body().getData().size();
                        for (int i = 0; i < count; i++) {
                            WeekReportPojo weekReportPojo = new WeekReportPojo();
                            //Log.e("arraydata1", String.valueOf(response.body().getData().get(i).getWeekTitle()));
                            //Log.e("arraydata2", String.valueOf(response.body().getData().get(i).getManHours()));
                            weekReportPojo.setWeeks(response.body().getData().get(i).getWeekTitle());
                            //Log.e("weekdata", weekReportPojo.getWeeks());
                            weekReportPojo.setWorkinghr(response.body().getData().get(i).getAvgHoursPerDay());
                           // Log.e("weekdata1", weekReportPojo.getWorkinghr());
                            weekReportPojo.setActualworkinghr(response.body().getData().get(i).getManHours());
                            //Log.e("weekdata2", weekReportPojo.getActualworkinghr());
                            weekReportPojo.setExtrahr(response.body().getData().get(i).getOverTime());
                            //Log.e("weekdata3", weekReportPojo.getExtrahr());
                            arrayList.add(weekReportPojo);

                        }
                        WeekWorkingHrAdapter adapter = new WeekWorkingHrAdapter(getActivity(), arrayList);
                        listView.setAdapter(adapter);

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(RegistrationActivity.this,LicenseActivity.class));
                        Date localTime = null;
                        int Totalmins=0;
                        int Totalmins1=0;
                        int Totalmins2=0;

                       /* long time1 = 0;
                        long time2 = 0;
                        long time3 = 0;
                        long time4 = 0;
                        long time5 = 0;
                        long time6 = 0;*/
                        for (int i = 0; i < arrayList.size(); i++) {
                            WeekReportPojo jsonObject = arrayList.get(i);
                            try {
                                //localTime = new SimpleDateFormat(" hh:mm",Locale.US).parse(jsonObject.getWorkinghr());
                                String[] srcarr1=jsonObject.getWorkinghr().split(":");
                                int Hrs=0;
                                int Min=0;
                                Hrs=Integer.parseInt(srcarr1[0]);
                                Min=Integer.parseInt(srcarr1[1]);
                                Totalmins=Totalmins+(Hrs*60)+Min;

                                //formattime1(Totalmins);

                                //localTime = format.parse(jsonObject.getWorkinghr());
                                //System.out.println("TimeStamp is " + localTime.getTime());
                                //time1 = time1 + localTime.getHours();
                               // Log.e("time1", String.valueOf(localTime.getHours()));
                               // time2 = time2 + localTime.getMinutes();
                               // Log.e("time2", String.valueOf(localTime.getMinutes()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                //localTime = new SimpleDateFormat(" hh:mm",Locale.US).parse(jsonObject.getWorkinghr());
                                String[] srcarr2=jsonObject.getActualworkinghr().split(":");
                                int Hrs=0;
                                int Min=0;
                                Hrs=Integer.parseInt(srcarr2[0]);
                                Min=Integer.parseInt(srcarr2[1]);
                                Totalmins1=Totalmins1+(Hrs*60)+Min;
                                /*localTime = format.parse(jsonObject.getActualworkinghr());
                                System.out.println("TimeStamp is " + localTime.getTime());
                                time3 = time3 + localTime.getHours();
                                time4 = time4 + localTime.getMinutes();*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                //localTime = new SimpleDateFormat(" hh:mm",Locale.US).parse(jsonObject.getWorkinghr());
                                String[] srcarr3=jsonObject.getExtrahr().split(":");
                                int Hrs=0;
                                int Min=0;
                                Hrs=Integer.parseInt(srcarr3[0]);
                                Min=Integer.parseInt(srcarr3[1]);
                                Totalmins2=Totalmins2+(Hrs*60)+Min;
                                /*localTime = format.parse(jsonObject.getExtrahr());

                                System.out.println("TimeStamp is " + localTime.getTime());
                                time5 = time5 + localTime.getHours();
                                time6 = time6 + localTime.getMinutes();*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        // listView.setAdapter(adapter);
                        txt_working_hrs_cal.setText(formattime1(Totalmins));
                        //Log.e("wr",formattime(time1, time2));
                        txt_actual_hrs_cal.setText(formattime1(Totalmins1));
                        //Log.e("ar",formattime(time3, time4));
                        txt_extra_hrs_cal.setText(formattime1(Totalmins2));
                       // Log.e("er",formattime(time5, time6));
                        if (Utilities.isNetworkAvailable(getContext())) {
                           // getSummary_Data(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                             getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                        } else {
                            Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        if(!response.body().getMessage().equals(EmpowerApplication.ForSessionExpire)) {
                            if (Utilities.isNetworkAvailable(getContext())) {
                                // getSummary_Data(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                                getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), new SimpleDateFormat(DATE_FORMAT_calender).format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
                            } else {
                                Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                            }

                            EmpowerApplication.alertdialog(response.body().getMessage(), getContext());
                        }

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
            public void onFailure(retrofit2.Call<SummaryPoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                //pd.dismiss();

            }
        });
    }

    private String getTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date123 = android.text.format.DateFormat.format("hh:mm", cal).toString();
         //String data=format.
                //("HH:mm", cal).toString();
        return date123;
    }

    public void getCalenderData(String empid, final String date, String token) {

        //pd = new ProgressDialog(getContext());
        //pd.setMessage("loading");
       // pd.show();

        final Map<String,String> calenderdata=new HashMap<String,String>();
        calenderdata.put("employeeId",empid);
        calenderdata.put("date",date);
        calenderdata.put("token",token);

     final List<String> daystatus=new ArrayList<>();

        retrofit2.Call<ClaenderPoJo> call = apiService.getCalenderData(calenderdata);
        call.enqueue(new Callback<ClaenderPoJo>() {
            @Override
            public void onResponse(retrofit2.Call<ClaenderPoJo> call, Response<ClaenderPoJo> response) {

               // pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());
                daystatus.clear();
                if(response.isSuccessful()) {

                    Log.d("Calender: ", new Gson().toJson(response.body()));


                    if (response.body().getStatus().equals("1")) {
                       // Log.e("responsedata", response.body().getStatus());
                        int count = response.body().getData().size();
                        for (int i = 0; i < count; i++) {
                           // Log.e("arraydata1", String.valueOf(response.body().getData().get(i).getDayStatusCode()));
                           // Log.e("arraydata2", String.valueOf(response.body().getData().get(i).getShiftDate()));

                            daystatus.add(response.body().getData().get(i).getDayStatusCode());


                        }

                        Log.d("", "onResponsedaystatus: "+daystatus);

                        p.setText(String.valueOf(Collections.frequency(daystatus, "P")));
                        a.setText(String.valueOf(Collections.frequency(daystatus, "A")));
                        l.setText(String.valueOf(Collections.frequency(daystatus, "L")));
                        od.setText(String.valueOf(Collections.frequency(daystatus, "OD")));
                        wop.setText(String.valueOf(Collections.frequency(daystatus, "WO")));//WOP
                        ss.setText(String.valueOf(Collections.frequency(daystatus, "SS")));
                        co.setText(String.valueOf(Collections.frequency(daystatus, "CO")));
                        lm.setText(String.valueOf(Collections.frequency(daystatus, "LM"))); //WFH //AAPA //HD2 // EP
                        wfh.setText(String.valueOf(Collections.frequency(daystatus, "WFH")));
                        h.setText(String.valueOf(Collections.frequency(daystatus, "H")));


                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    public String formattime(long hourOfDay,long minute){

        String hourString;
        if (hourOfDay < 10)
            hourString = "0" + hourOfDay;
        else
            hourString = "" +hourOfDay;

        String minuteSting;
        if (minute < 10) {
            minuteSting = "0" + minute;
        }
        else {
            minuteSting = "" + minute;
            if(minute>59){
                minuteSting = String.valueOf(minute - 60);
                hourString = String.valueOf(Long.parseLong(hourString) + 1);
                for(;minute%60>59;) {
                    minuteSting = String.valueOf((minute%60) - 60);
                    hourString = String.valueOf(Long.parseLong(hourString) + 1);
                }
            }
        }

        //txtTime1.setText(hourString + ":" + minuteSting);
      return hourString + ":" + minuteSting;
    }
    public String formattime1(int minutes){
        int hourOfDay=minutes/60;
        int minuteOfDay=minutes%60;

        String hourString;
        if (hourOfDay < 10)
            hourString = "0" + hourOfDay;
        else
            hourString = "" +hourOfDay;

        String minuteSting;
        if (minuteOfDay < 10) {
            minuteSting = "0" + minuteOfDay;
        }
        else {
            minuteSting = "" + minuteOfDay;
           /* if(minuteOfDay>59){
                minuteSting = String.valueOf(minuteOfDay - 60);
                hourString = String.valueOf(Long.parseLong(hourString) + 1);
                for(;minuteOfDay%60>59;) {
                    minuteSting = String.valueOf((minuteOfDay%60) - 60);
                    hourString = String.valueOf(Long.parseLong(hourString) + 1);
                }
            }*/
        }

        //txtTime1.setText(hourString + ":" + minuteSting);
        return hourString + ":" + minuteSting;
    }


   /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateLabel();
    }*/

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


}
