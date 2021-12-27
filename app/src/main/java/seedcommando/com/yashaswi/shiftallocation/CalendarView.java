package seedcommando.com.yashaswi.shiftallocation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;


import seedcommando.com.yashaswi.pojos.shiftallocation.MyShift;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando4 on 6/8/2018.
 */

public class CalendarView extends LinearLayout
{
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // default date format
    private static final String DATE_FORMAT_calender = "dd-MMM-yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private CalendarView.EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    ProgressDialog pd;
    Date firstDateOfPreviousMonth,lastDateOfPreviousMonth;
    private ApiInterface apiService;
    static ArrayList<String> shiftstatus;
    ArrayList<Date> cells;

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CalendarView(Context context)
    {
        super(context);
        //currentDate = Calendar.getInstance();
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
       // currentDate = Calendar.getInstance();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
        //currentDate = Calendar.getInstance();
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        //currentDate.add(Calendar.MONTH, );
        //Calendar aCalendar = Calendar.getInstance();
        //aCalendar.add(Calendar.MONTH, +1);
        currentDate.set(Calendar.DATE, 1);
        firstDateOfPreviousMonth = currentDate.getTime();
        currentDate.set(Calendar.DATE,     currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        lastDateOfPreviousMonth = currentDate.getTime();
       /* currentDate = Calendar.getInstance();
        currentDate.set(Calendar.DATE,    currentDate. getActualMinimum(Calendar.DAY_OF_MONTH));
        firstDateOfPreviousMonth = currentDate.getTime();
        // set actual maximum date of previous month
        currentDate.set(Calendar.DATE,     currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        lastDateOfPreviousMonth = currentDate.getTime();*/
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();
        //updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try
        {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        }
        finally
        {
            ta.recycle();
        }
    }
    private void assignUiElements()
    {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDate = findViewById(R.id.calendar_date_display);
        grid = findViewById(R.id.calendar_grid);
        grid.setBackgroundColor(Color.parseColor("#d3d3d3"));
        grid.setVerticalSpacing(2);
        grid.setHorizontalSpacing(2);
    }

    private void assignClickHandlers()
    {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, 1);
               // Calendar aCalendar = Calendar.getInstance();
                //aCalendar.add(Calendar.MONTH, +1);
                currentDate.set(Calendar.DATE, 1);
                firstDateOfPreviousMonth = currentDate.getTime();
                currentDate.set(Calendar.DATE,     currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDateOfPreviousMonth = currentDate.getTime();
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, -1);
                //Calendar aCalendar = Calendar.getInstance();
               // aCalendar.add(Calendar.MONTH, -1);
                currentDate.set(Calendar.DATE, 1);
                firstDateOfPreviousMonth = currentDate.getTime();
                currentDate.set(Calendar.DATE,    currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDateOfPreviousMonth = currentDate.getTime();
                updateCalendar();
            }
        });


       /* grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventHandler.onDayClick((Date) parent.getItemAtPosition(position));
            }
        });*/
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

        Log.e("fdate", String.valueOf(firstDateOfPreviousMonth.getTime()));
        Log.e("ldate", String.valueOf(lastDateOfPreviousMonth.getTime()));


        //String myFormat = "MMMyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        txtDate.setText(sdf.format(currentDate.getTime()));
    }


    private void setDateTimeField() {
        //Calendar dateSelected = Calendar.getInstance();
        DatePickerDialog datePickerDialog;
        Calendar newCalendar = currentDate;
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //currentDate.set(year, monthOfYear, dayOfMonth, 0, 0);
                currentDate.set(year, monthOfYear, dayOfMonth, 0, 0);
                updateCalendar();
                //dateEditText.setText(dateFormatter.format(dateSelected.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

        //dateEditText.setText(dateFormatter.format(dateSelected.getTime()));
    }


    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events)
    {
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_calender);
        //if(HomeFragment.position==0) {
        if (Utilities.isNetworkAvailable(getContext())) {

            getMyShiftData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), sdf1.format(firstDateOfPreviousMonth.getTime()), sdf1.format(lastDateOfPreviousMonth.getTime()));
        } else {
            Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
        }
        //}
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));
        cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


    }


    public void getMyShiftData(String empid, final String date1, final String date2) {

        pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        final Map<String,String> myShiftdata=new HashMap<String,String>();
        myShiftdata.put("employeeId",empid);
        myShiftdata.put("fromDate",date1);
        myShiftdata.put("toDate",date2);
        Log.e("date123",date1+date2);
        final ArrayList<String> calenderdate=new ArrayList<>();
        shiftstatus=new ArrayList<>();

        retrofit2.Call<MyShift> call = apiService.getShiftAllocationData(myShiftdata);
        call.enqueue(new Callback<MyShift>() {
            @Override
            public void onResponse(retrofit2.Call<MyShift> call, Response<MyShift> response) {

                pd.dismiss();
                shiftstatus.clear();
                calenderdate.clear();

                if(response.isSuccessful()) {
                    //Log.d("User ID: ", response.body().getMessage());

                    if (response.body().getStatus().equals("1")) {
                        Log.e("responsedata", response.body().getStatus());
                        int count = response.body().getData().size();
                        for (int i = 0; i < count; i++) {
                            Log.e("arraydata1", String.valueOf(response.body().getData().get(i).getShift()));
                            Log.e("arraydata2", String.valueOf(response.body().getData().get(i).getShiftDate()));
                            calenderdate.add(response.body().getData().get(i).getShiftDate());
                            shiftstatus.add(response.body().getData().get(i).getShift());


                        }

                        grid.setAdapter(new CalenderAdapter(getContext(), currentDate, cells, shiftstatus));



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
            public void onFailure(retrofit2.Call<MyShift> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();

            }
        });
    }




    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(CalendarView.EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayClick(Date date);
    }

}
