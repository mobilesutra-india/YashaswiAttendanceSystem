package seedcommando.com.yashaswi.attendancedetailsactivity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando1 on 9/1/2017.
 */

public class AttendanceDetailsAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AttendanceDetailsPOJO> mDataSource;
    public AttendanceDetailsAdapter(Context context, ArrayList<AttendanceDetailsPOJO> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.attendance_detail_list_group, parent, false);
        // Get title element
        TextView textView_Date_attendance =
                rowView.findViewById(R.id.textView_Date_attendance);
        TextView textView_in_attendance =
                rowView.findViewById(R.id.textView_in_attendance);
        TextView textView_out_attendance =
                rowView.findViewById(R.id.textView_out_attendance);
        TextView textView_manhrs_attendance =
                rowView.findViewById(R.id.textView_manhrs_attendance);
        TextView textView_status_attendance =
                rowView.findViewById(R.id.textView_status_attendance);

        // 1
       AttendanceDetailsPOJO attendanceDetailsPOJO= (AttendanceDetailsPOJO) getItem(position);

// 2

          if(attendanceDetailsPOJO.getStatus().equals("A")) {
              rowView.setBackgroundColor(Color.parseColor("#ffcccc"));
              textView_Date_attendance.setText(attendanceDetailsPOJO.Date);
              textView_in_attendance.setText(attendanceDetailsPOJO.In);
              Log.e("in adapter", attendanceDetailsPOJO.In);
              textView_out_attendance.setText(attendanceDetailsPOJO.Out);
              Log.e("in adapter", attendanceDetailsPOJO.Out);
              textView_manhrs_attendance.setText(attendanceDetailsPOJO.Manhrs);
              textView_status_attendance.setText(attendanceDetailsPOJO.Status);
          }else {
              textView_Date_attendance.setText(attendanceDetailsPOJO.Date);
              textView_in_attendance.setText(attendanceDetailsPOJO.In);
              Log.e("in adapter", attendanceDetailsPOJO.In);
              textView_out_attendance.setText(attendanceDetailsPOJO.Out);
              Log.e("in adapter", attendanceDetailsPOJO.Out);
              textView_manhrs_attendance.setText(attendanceDetailsPOJO.Manhrs);
              textView_status_attendance.setText(attendanceDetailsPOJO.Status);

          }

        return rowView;
    }
}
