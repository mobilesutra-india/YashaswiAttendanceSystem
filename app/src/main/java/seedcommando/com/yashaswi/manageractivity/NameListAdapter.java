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
 * Created by commando4 on 6/1/2018.
 */

public class NameListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;


    private ArrayList<String> mDataSource;

    public NameListAdapter(Context context, ArrayList<String> items) {
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
        convertView = mInflater.inflate(R.layout.name_list, parent, false);

          TextView name=convertView.findViewById(R.id.textView_name1);

          name.setText(mDataSource.get(position));

        // Get title element



        return convertView;
    }
}
