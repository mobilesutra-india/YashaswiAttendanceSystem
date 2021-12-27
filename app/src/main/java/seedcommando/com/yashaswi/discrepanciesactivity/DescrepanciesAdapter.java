package seedcommando.com.yashaswi.discrepanciesactivity;

import android.content.Context;
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

public class DescrepanciesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DiscrepanciesDataPOJO> mDataSource;
    public DescrepanciesAdapter(Context context, ArrayList<DiscrepanciesDataPOJO> items) {
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
        View rowView = mInflater.inflate(R.layout.discrepancies_list_group, parent, false);

        // Get title element
        TextView date_TextView =
                rowView.findViewById(R.id.textView_Date);
        TextView day_TextView =
                rowView.findViewById(R.id.textView_day);
        TextView status_TextView =
                rowView.findViewById(R.id.textView_status);

        // 1
             DiscrepanciesDataPOJO discrepanciesDataPOJO= (DiscrepanciesDataPOJO) getItem(position);

// 2


        date_TextView.setText(discrepanciesDataPOJO.Date);
        day_TextView.setText(discrepanciesDataPOJO.Day);
        status_TextView.setText(discrepanciesDataPOJO.Status);



        return rowView;
    }


}
