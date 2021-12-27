package seedcommando.com.yashaswi.markhistory;

/**
 * Created by commando1 on 8/9/2017.
 */

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seedcommando.com.yashaswi.Attendance_cameraActivity;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.database.EmpowerDatabase;
import seedcommando.com.yashaswi.pojos.markattendance.OffLineAttendancePoJo;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    int offlineRecordCount = 0;
    private Context _context;
    EmpowerDatabase db;
    List<OffLineAttendancePoJo> empList;

    private List<HeaderData> _listGroupTitle; // header titles

    private HashMap<String, ArrayList<EmployeeMarkHistory>> _listDataMembers;

    Attendance_cameraActivity hm;



    public ExpandableListAdapter(Context context, ArrayList<HeaderData> listGroupTitle,
                                 HashMap<String, ArrayList<EmployeeMarkHistory>> listDataMembers) {
        this._context = context;
        this._listGroupTitle = listGroupTitle;
        this._listDataMembers = listDataMembers;

        EmpowerDatabase db = new EmpowerDatabase(_context);
        offlineRecordCount = db.getCountOfOfflineRecords();
         empList = db.getAttendenceList();


    }



    @Override

    public Object getChild(int groupPosition, int childPosititon) {

        return this._listDataMembers.get(this._listGroupTitle.get(groupPosition).getDate())

                .get(childPosititon);


    }



    @Override

    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;

    }



    @Override

    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        EmployeeMarkHistory memData=(EmployeeMarkHistory) getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this._context

                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_member, null);
        }
        TextView txtDisName= convertView.findViewById(R.id.txtdistrict);
        txtDisName.setText(Html.fromHtml(memData.getDate()));
        TextView txtNum= convertView.findViewById(R.id.txtdnum);
        txtNum.setText(Html.fromHtml(memData.getData()));

        ImageView img =convertView.findViewById(R.id.loadingimg);
        TextView txtstatus =convertView.findViewById(R.id.txtstatus);

        txtstatus.setVisibility(View.GONE);
        Log.d("valuessss", "getChildView: "+  childPosition +"dadabaseVAlue"+  offlineRecordCount);

        Log.d("valueDataBase", "DataBaseCount: "+empList.size());


        if(childPosition  <= empList.size() ) {
                txtstatus.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    img.setVisibility(View.VISIBLE);
                    img.setImageDrawable(ContextCompat.getDrawable(_context, R.drawable.notsend));
                }

            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    img.setVisibility(View.VISIBLE);
                    img.setImageDrawable(ContextCompat.getDrawable(_context, R.drawable.sent));
                }

            }

        if (childPosition == 0) {

            img.setVisibility(View.GONE);
            txtstatus.setText(Html.fromHtml(memData.getStatus()));

            txtstatus.setVisibility(View.VISIBLE);

        }

        return convertView;

    }



    @Override

    public int getChildrenCount(int groupPosition) {

        return this._listDataMembers.get(this._listGroupTitle.get(groupPosition).getDate()).size();

    }



    @Override

    public Object getGroup(int groupPosition) {

        return this._listGroupTitle.get(groupPosition);

    }



    @Override

    public int getGroupCount() {

        return this._listGroupTitle.size();

    }



    @Override

    public long getGroupId(int groupPosition) {

        return groupPosition;

    }



    @Override

    public View getGroupView(int groupPosition, boolean isExpanded,

                             View convertView, ViewGroup parent) {

        HeaderData gData= (HeaderData) getGroup(groupPosition);



        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this._context

                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_group, null);

        }


        TextView txtProName= convertView.findViewById(R.id.date);

        txtProName.setText(Html.fromHtml(gData.getDate()));

        TextView txtNum= convertView.findViewById(R.id.txt_history);

        txtNum.setText(Html.fromHtml(gData.getPunch()));



        //icon in right of header....


        //...........................


        return convertView;

    }



    @Override

    public boolean hasStableIds() {

        return false;

    }



    @Override

    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;

    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);


    }
}
