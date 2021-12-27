package seedcommando.com.yashaswi.outdutyapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by commando5 on 9/6/2017.
 */

public class DatePickerFragment extends DialogFragment
{
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;



    public DatePickerFragment() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }



}