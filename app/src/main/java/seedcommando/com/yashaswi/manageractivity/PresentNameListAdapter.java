package seedcommando.com.yashaswi.manageractivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.pojos.ManagerSummaryPoJo.SubOrdinateSummaryPojo.PresentNamePoJo;

/**
 * Created by commando4 on 6/2/2018.
 */

public class PresentNameListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;


    private ArrayList<PresentNamePoJo> mDataSource;

    public PresentNameListAdapter(Context context, ArrayList<PresentNamePoJo> items) {
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
        convertView = mInflater.inflate(R.layout.present_name_list, parent, false);

        TextView name=convertView.findViewById(R.id.textView_name);
        TextView in=convertView.findViewById(R.id.textView_intime);
        TextView out=convertView.findViewById(R.id.textView_outtime);

        PresentNamePoJo presentNamePoJo= (PresentNamePoJo) getItem(position);
       name.setText( presentNamePoJo.getName());
        in.setText( presentNamePoJo.getInTime());
        out.setText( presentNamePoJo.getOutTime());

        //name.setText(mDataSource.get(position));

        // Get title element



        return convertView;
    }
}
