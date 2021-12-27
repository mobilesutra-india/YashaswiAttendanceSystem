package seedcommando.com.yashaswi.shiftallocation;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;

/**
 * Created by commando1 on 9/18/2017.
 */

public class ShiftAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ShiftPOJO> mDataSource;
    ArrayList<String> options;


    public ShiftAdapter(Context context, ArrayList<ShiftPOJO> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         options=new ArrayList<>();
        options.add("G");
        options.add("M");
        options.add("E");
        options.add("N");

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
        View rowView = mInflater.inflate(R.layout.list_member_my_shift, parent, false);

        // Get title element
        TextView name =
                rowView.findViewById(R.id.textView_name_shift);
        final TextView one_TextView =
                rowView.findViewById(R.id.button1);
        final TextView two_TextView =
                rowView.findViewById(R.id.button2);
        final TextView three_TextView =
                rowView.findViewById(R.id.button3);
        final TextView four_TextView =
                rowView.findViewById(R.id.button4);
        final TextView five_TextView =
                rowView.findViewById(R.id.button5);
        final TextView six_TextView =
                rowView.findViewById(R.id.button6);
        // 1
        ShiftPOJO shiftPOJO= (ShiftPOJO) getItem(position);

// 2

        name.setText(shiftPOJO.name);
        one_TextView.setText(shiftPOJO.dayone);
        one_TextView.setTag(R.string.EmpCode,shiftPOJO.getEmpcode());
        one_TextView.setTag(R.string.EmpDate,shiftPOJO.getDate1());//Empname
        one_TextView.setTag(R.string.Empname,shiftPOJO.name);

       // Log.e("data=-0-=",shiftPOJO.getDayone());
        two_TextView.setText(shiftPOJO.daytwo);
        two_TextView.setTag(R.string.EmpCode,shiftPOJO.getEmpcode());
        two_TextView.setTag(R.string.EmpDate,shiftPOJO.getDate2());
        two_TextView.setTag(R.string.Empname,shiftPOJO.name);

        three_TextView.setText(shiftPOJO.daythree);
        three_TextView.setTag(R.string.EmpCode,shiftPOJO.getEmpcode());
        three_TextView.setTag(R.string.EmpDate,shiftPOJO.getDate3());
        three_TextView.setTag(R.string.Empname,shiftPOJO.name);

        four_TextView.setText(shiftPOJO.dayfour);
        four_TextView.setTag(R.string.EmpCode,shiftPOJO.getEmpcode());
        four_TextView.setTag(R.string.EmpDate,shiftPOJO.getDate4());
        four_TextView.setTag(R.string.Empname,shiftPOJO.name);

        five_TextView.setText(shiftPOJO.dayfive);
        five_TextView.setTag(R.string.EmpCode,shiftPOJO.getEmpcode());
        five_TextView.setTag(R.string.EmpDate,shiftPOJO.getDate5());
        five_TextView.setTag(R.string.Empname,shiftPOJO.name);

        six_TextView.setText(shiftPOJO.daysix);
        six_TextView.setTag(R.string.EmpCode,shiftPOJO.getEmpcode());
        six_TextView.setTag(R.string.EmpDate,shiftPOJO.getDate6());
        six_TextView.setTag(R.string.Empname,shiftPOJO.name);


        /*one_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSpineer( one_TextView.getTag(R.string.EmpCode).toString(),one_TextView.getTag(R.string.EmpDate).toString(),one_TextView.getTag(R.string.Empname).toString());

            }
        });
        two_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSpineer(two_TextView.getTag(R.string.EmpCode).toString(),two_TextView.getTag(R.string.EmpDate).toString(),two_TextView.getTag(R.string.Empname).toString());

            }
        });
       three_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSpineer(three_TextView.getTag(R.string.EmpCode).toString(),three_TextView.getTag(R.string.EmpDate).toString(),three_TextView.getTag(R.string.Empname).toString());

            }
        });
        four_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSpineer(four_TextView.getTag(R.string.EmpCode).toString(),four_TextView.getTag(R.string.EmpDate).toString(),four_TextView.getTag(R.string.Empname).toString());

            }
        });
        five_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSpineer(five_TextView.getTag(R.string.EmpCode).toString(),five_TextView.getTag(R.string.EmpDate).toString(),five_TextView.getTag(R.string.Empname).toString());

            }
        });
       six_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForSpineer(six_TextView.getTag(R.string.EmpCode).toString(),six_TextView.getTag(R.string.EmpDate).toString(),six_TextView.getTag(R.string.Empname).toString());

            }
        });*/


        return rowView;
    }

    public void dialogForSpineer(final String empcode, final String date,String name){

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_for_shift);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView=dialog.findViewById(R.id.textView_date);
        textView.setText(date);
        TextView textView_name=dialog.findViewById(R.id.textView_shift_nm);
        textView_name.setText(name);
        final Spinner spinner=dialog.findViewById(R.id.spinner1);
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,SubordinateShiftAllocationFragment.arrayList_shiftName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter); // this will set list of values to spinner
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Button apply= dialog.findViewById(R.id.button_apply);
        Button cancel= dialog.findViewById(R.id.button_cancel);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DataString",spinner.getSelectedItem().toString()+"Empid:"+ EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId"))+"token:"+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data"))+"empcode:"+empcode+"date:"+date);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();




    }


}
