package seedcommando.com.yashaswi.shiftallocation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import seedcommando.com.yashaswi.R;


/**
 * Created by commando1 on 9/18/2017.
 */

public class MyShiftAllocationFragment extends Fragment {

    static final int FIRST_DAY_OF_WEEK =0; // Sunday = 0, Monday = 1
    View rootView;
    static Calendar currentDate = Calendar.getInstance();

    public MyShiftAllocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.shiftfragment_calendar, container, false);

        return rootView;
    }

    public String DateFormat(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("EEEE,dd MMM yyyy");
        Log.e("date",fmtOut.toString());
        return fmtOut.format(date);
    }
    public String DateFormat1(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
        Log.e("date",fmtOut.toString());
        return fmtOut.format(date);
    }

 @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);

        //CalendarView1 cv = (CalendarView1) rootView.findViewById(R.id.calendar_view);
        //ShiftCalendarView cv1=((ShiftCalendarView) rootView.findViewById(R.id.calendar_view));
        seedcommando.com.yashaswi.shiftallocation.CalendarView cv = rootView.findViewById(R.id.calendar_view);
        cv.updateCalendar();


    }
    // references to our items
    public String[] days;



    }