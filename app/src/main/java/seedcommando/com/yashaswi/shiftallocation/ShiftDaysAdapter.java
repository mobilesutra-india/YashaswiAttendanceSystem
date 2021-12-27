package seedcommando.com.yashaswi.shiftallocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando1 on 9/19/2017.
 */

public class ShiftDaysAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ShiftDaysPOJO> mDataSource;

    public ShiftDaysAdapter(Context context, ArrayList<ShiftDaysPOJO> items) {
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
        View rowView = mInflater.inflate(R.layout.list_member_shift_days, parent, false);

        // Get title element
        TextView name =
                rowView.findViewById(R.id.textView_name_shift);
        TextView one_TextView =
                rowView.findViewById(R.id.button1);
        TextView two_TextView =
                rowView.findViewById(R.id.button2);
        TextView three_TextView =
                rowView.findViewById(R.id.button3);
        TextView four_TextView =
                rowView.findViewById(R.id.button4);
        TextView five_TextView =
                rowView.findViewById(R.id.button5);
        TextView six_TextView =
                rowView.findViewById(R.id.button6);
        // 1
        ShiftDaysPOJO shiftdaysPOJO= (ShiftDaysPOJO) getItem(position);

// 2

        name.setText(shiftdaysPOJO.shifttype);
        one_TextView.setText(String.valueOf(shiftdaysPOJO.getShiftone()));
        two_TextView.setText(String.valueOf(shiftdaysPOJO.getShifttwo()));
        three_TextView.setText(String.valueOf(shiftdaysPOJO.getShiftthree()));
        four_TextView.setText(String.valueOf(shiftdaysPOJO.getShiftfour()));
        five_TextView.setText(String.valueOf(shiftdaysPOJO.getShiftfive()));
        six_TextView.setText(String.valueOf(shiftdaysPOJO.getShiftsix()));



        return rowView;
    }
}
