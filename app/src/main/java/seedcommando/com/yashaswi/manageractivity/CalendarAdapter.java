package seedcommando.com.yashaswi.manageractivity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando4 on 2/7/2018.
 */

public class CalendarAdapter extends BaseAdapter {
    static final int FIRST_DAY_OF_WEEK =0; // Sunday = 0, Monday = 1


    private Context mContext;

    private Calendar month;
    Calendar selectedDate;
    private ArrayList<String> items;
    private ArrayList<Date> dates=new ArrayList<Date>();
    private ArrayList<String> status1=new ArrayList<String>();

    public CalendarAdapter(Context c, Calendar monthCalendar,ArrayList<Date>days,ArrayList<String>daystatus) {

        //super(c, R.layout.control_calendar_day, days);
        month = monthCalendar;
        selectedDate = (Calendar)monthCalendar.clone();
        mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        this.dates =days;
        this.status1=daystatus;
        refreshDays();

    }

    public void setItems(ArrayList<String> items) {
        for(int i = 0;i != items.size();i++){
            if(items.get(i).length()==1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }


    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return dates.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView,status;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.control_calendar_day, null);

        }
        dayView = v.findViewById(R.id.day_date);
        status = v.findViewById(R.id.textView);
        //int pos=dates.indexOf()
        Date date1 = selectedDate.getTime();
        int day = date1.getDate();
        // today
        Date today = new Date();


        // disable empty days from the beginning
        if(days[position].equals("")) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
        }
        else  {
            // mark current day as focused
            //month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) && days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))
             //boolean tata=dates.contains(today.getDate());
            if(dates.contains(today.getDate())) {
                //dayView.setTextColor(Color.GREEN);


            }
            else {
                //dayView.setTextColor(Color.GREEN);
               // v.setBackgroundResource(R.drawable.list_item_background);
            }
        }
        dayView.setText(days[position]);

        Log.e("arry data",status123[6]);
        Log.e("arry data1",days[6]);
        //int read_position=
        status.setText(status123[position]);

        // create date string for comparison
        String date = days[position];

        if(date.length()==1) {
            date = "0"+date;
        }
        String monthStr = ""+(month.get(Calendar.MONTH)+1);
        if(monthStr.length()==1) {
            monthStr = "0"+monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        //ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        if(date.length()>0 && items!=null && items.contains(date)) {
           // iw.setVisibility(View.VISIBLE);
            dayView.setTextColor(Color.GREEN);
        }

        return v;
    }

    private void refreshDays()
    {
        // clear items
        items.clear();

        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = month.get(Calendar.DAY_OF_WEEK);

        // figure size of the array
        if(firstDay==1){
            days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
            status123 = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
        }
        else {
            days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
           status123= new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }

        int j=FIRST_DAY_OF_WEEK;

        // populate empty days before first real day
        if(firstDay>1) {
            for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
                days[j] = "";
                status123[j]="";
               // dates.set(j, null);
            }
        }
        else {
            for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
                days[j] = "";
                status123[j]="";

            }
            j=FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
        }

        // populate days
        int dayNumber = 1;
        int daystatus= 0;

            for (int i = j - 1; i < days.length; i++) {
                // dates.add(month.getTime());
                // dates.set(i, dayNumber);
               if(dayNumber<=status1.size()) {
                   days[i] = "" + dayNumber;
                   status123[i] = "" + status1.get(daystatus);
                   daystatus++;
                   dayNumber++;
               }else {
                   days[i] = "" + dayNumber;
                   status123[i] = "";
                   daystatus++;
                   dayNumber++;

               }
            }


    }

    // references to our items
    public String[] days;
    public String[] status123;





    }


