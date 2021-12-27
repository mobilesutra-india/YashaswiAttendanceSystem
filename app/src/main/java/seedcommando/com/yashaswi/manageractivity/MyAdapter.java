package seedcommando.com.yashaswi.manageractivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando4 on 4/10/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;

    /*static ArrayList<Integer> arrayList1=new ArrayList<>();
    static ArrayList<Integer> arrayList2=new ArrayList<>();
    static  ArrayList<Integer> arrayList3=new ArrayList<>();*/
    private ArrayList<WeekReportPojo> mDataSource;

    static int workinghr=0,actualhr=0,extrahr=0;
    static String working_hr=null,actual_hr=null,extra_hr=null;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView week_TextView,workinghr_TextView,actualworkinghr_TextView,extrahr_TextView;
        public ViewHolder(View v) {
            super(v);
            // Get title element
            week_TextView =
                    v.findViewById(R.id.textView_week);
           workinghr_TextView =
                   v.findViewById(R.id.textView_working_hrs);
             actualworkinghr_TextView =
                     v.findViewById(R.id.textView_actual_hrs);
             extrahr_TextView =
                     v.findViewById(R.id.textView_extra_hrs);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, ArrayList<seedcommando.com.yashaswi.manageractivity.WeekReportPojo> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
       /* TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);*/
        View rowView = mInflater.inflate(R.layout.list_items_weeks, parent, false);


        //ViewHolder vh = new ViewHolder(v);
        return  new ViewHolder(rowView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        //holder.mTextView.setText(mDataset[position]);

        seedcommando.com.yashaswi.manageractivity.WeekReportPojo weekReportPojo= mDataSource.get(position);




       holder.week_TextView.setText(weekReportPojo.weeks);
        holder. workinghr_TextView.setText(weekReportPojo.workinghr);

        holder.actualworkinghr_TextView.setText(weekReportPojo.actualworkinghr);

        holder.extrahr_TextView.setText(weekReportPojo.extrahr);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}