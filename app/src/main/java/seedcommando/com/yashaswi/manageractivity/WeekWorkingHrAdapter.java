package seedcommando.com.yashaswi.manageractivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando1 on 8/28/2017.
 */

public class WeekWorkingHrAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;


        private ArrayList<seedcommando.com.yashaswi.manageractivity.WeekReportPojo> mDataSource;

    public WeekWorkingHrAdapter(Context context, ArrayList<seedcommando.com.yashaswi.manageractivity.WeekReportPojo> items) {
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
            convertView = mInflater.inflate(R.layout.list_items_weeks, parent, false);

            // Get title element
            TextView week_TextView =
                    convertView.findViewById(R.id.textView_week);
            TextView workinghr_TextView =
                    convertView.findViewById(R.id.textView_working_hrs);
            TextView actualworkinghr_TextView =
                    convertView.findViewById(R.id.textView_actual_hrs);
            TextView extrahr_TextView =
                    convertView.findViewById(R.id.textView_extra_hrs);
            // 1
            seedcommando.com.yashaswi.manageractivity.WeekReportPojo weekReportPojo= (WeekReportPojo) getItem(position);

                week_TextView.setText(weekReportPojo.weeks);
                workinghr_TextView.setText(weekReportPojo.workinghr);
                 actualworkinghr_TextView.setText(weekReportPojo.actualworkinghr);
                extrahr_TextView.setText(weekReportPojo.extrahr);


            return convertView;
        }
}
