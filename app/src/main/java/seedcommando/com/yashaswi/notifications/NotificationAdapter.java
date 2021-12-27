package seedcommando.com.yashaswi.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando1 on 9/21/2017.
 */

public class NotificationAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NotificationPOJO> mDataSource;

    public NotificationAdapter(Context context, ArrayList<NotificationPOJO> items) {
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
        View rowView = mInflater.inflate(R.layout.noti, parent, false);

        // Get title element
        TextView name =
                rowView.findViewById(R.id.textView_leave_type);
        TextView one_TextView =
                rowView.findViewById(R.id.textView_balance_text);
        TextView two_TextView =
                rowView.findViewById(R.id.textView_reamark_text);
        TextView three_TextView =
                rowView.findViewById(R.id.textView_leave_reason);
        TextView four_TextView =
                rowView.findViewById(R.id.textView_from_to_date);
        TextView five_TextView =
                rowView.findViewById(R.id.textView_time);

        NotificationPOJO notificationPOJO= (NotificationPOJO) getItem(position);

        name.setText(notificationPOJO.leavetype);
        one_TextView.setText(notificationPOJO.balance);
        two_TextView.setText(notificationPOJO.remark);
        three_TextView.setText(notificationPOJO.reason);
        four_TextView.setText("FROM "+notificationPOJO.fromdate+"" +
                "  TO  "+notificationPOJO.todate);
        five_TextView.setText(notificationPOJO.time);
        return rowView;
    }
}
