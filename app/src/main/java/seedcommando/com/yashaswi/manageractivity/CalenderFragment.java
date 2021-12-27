package seedcommando.com.yashaswi.manageractivity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import seedcommando.com.yashaswi.HomeFragment;
import seedcommando.com.yashaswi.attendanceregularization.Attend_Regularization;
import seedcommando.com.yashaswi.compoffapplication.CompoffActivity;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.leaveapplication.Leave_Application;
import seedcommando.com.yashaswi.Out_Duty_Application;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.workfromhomeapplication.WorkFromHomeActivity;

/**
 * Created by commando1 on 8/28/2017.
 */

public class CalenderFragment extends Fragment {

    static final int FIRST_DAY_OF_WEEK =0; // Sunday = 0, Monday = 1
    View rootView;
  static Calendar currentDate = Calendar.getInstance();

    public CalenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calender, container, false);
        //HashSet<Date> events = new HashSet<>();
       // events.add(new Date());




        // Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();


        return rootView;
    }

    public String DateFormat(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("EEEE,dd MMM yyyy");
       // Log.e("date",fmtOut.toString());
        return fmtOut.format(date);
    }
    public String DateFormat1(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
       // Log.e("date",fmtOut.toString());
        return fmtOut.format(date);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        seedcommando.com.yashaswi.manageractivity.CalendarView cv = rootView.findViewById(R.id.calendar_view);
        cv.updateCalendar();

        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @SuppressLint("ResourceType")
            @Override
            public void onDayClick(final Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                //View rootView = inflater.inflate(R.layout.application_selection_layout, container, false);
                // custom dialog
                if (HomeFragment.key.size() > 0) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.application_selection_layout);
                    Window window = dialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    // set the custom dialog components - text, image and button
                    TextView text = dialog.findViewById(R.id.textView_date);
                   // Log.e("date", date.toString());
                    //  String data=DateFormat(date.toString());
                    text.setText(DateFormat(date.toString()));
                    final Button applyButton = dialog.findViewById(R.id.button_apply);
                    Button cancelButton = dialog.findViewById(R.id.button_cancel);
                    // if button is clicked, close the custom dialog
                    final RadioGroup rg = dialog.findViewById(R.id.radiogroup);

                    for (int i = 0; i < HomeFragment.key.size(); i++) {
                        RadioButton radioButton = new RadioButton(getContext());
                        //radioButton.getButtonTintList(R.color.radio_button_color);
                        if (HomeFragment.key.get(i).equals("HideLeaveApp")) {
                            radioButton.setText("Leave Application");
                            radioButton.setId(0);
                        }
                        if (HomeFragment.key.get(i).equals("HideRegularizationApp")) {
                            radioButton.setText("Regularization Application");
                            radioButton.setId(1);
                        }
                        if (HomeFragment.key.get(i).equals("HideOutDutyApp")) {
                            radioButton.setText("Out Duty Application");
                            radioButton.setId(2);
                        }
                        if (HomeFragment.key.get(i).equals("HideCompOffApp")) {
                            radioButton.setText("CompOff Application");
                            radioButton.setId(3);
                        }
                        if (HomeFragment.key.get(i).equals("HideWFHApp")) {
                            radioButton.setText("WFH Application");
                            radioButton.setId(4);
                        }

                        rg.addView(radioButton);

                       /* radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int checkid = rg.getCheckedRadioButtonId();


                                switch (checkid) {
                                    case 0:
                                        Intent i = new Intent(getContext(), Leave_Application.class);
                                        i.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        // TODO Something
                                        Intent i1 = new Intent(getContext(), Attend_Regularization.class);
                                        i1.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i1);
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        // TODO Something
                                        Intent i2 = new Intent(getContext(), Out_Duty_Application.class);
                                        i2.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i2);
                                        dialog.dismiss();
                                        break;
                                    case 3:
                                        // TODO Something
                                        Intent i3 = new Intent(getContext(), CompoffActivity.class);
                                        i3.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i3);
                                        dialog.dismiss();
                                        break;
                                    case 4:
                                        // TODO Something
                                        Intent i4 = new Intent(getContext(), WorkFromHomeActivity.class);
                                        i4.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i4);
                                        dialog.dismiss();
                                        break;


                                }

                            }
                        });

*/
                    }
                    dialog.show();




                    applyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rg.getCheckedRadioButtonId() != -1) {

                               /* rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                                {
                                    @Override
                                    public void onCheckedChanged(final RadioGroup group, int checkedId)
                                    {*/
                                final int checkid = rg.getCheckedRadioButtonId();


                                switch (checkid) {
                                    case 0:
                                        Intent i = new Intent(getContext(), Leave_Application.class);
                                        i.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i);
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        // TODO Something
                                        Intent i1 = new Intent(getContext(), Attend_Regularization.class);
                                        i1.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i1);
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        // TODO Something
                                        Intent i2 = new Intent(getContext(), Out_Duty_Application.class);
                                        i2.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i2);
                                        dialog.dismiss();
                                        break;
                                    case 3:
                                        // TODO Something
                                        Intent i3 = new Intent(getContext(), CompoffActivity.class);
                                        i3.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i3);
                                        dialog.dismiss();
                                        break;
                                    case 4:
                                        // TODO Something
                                        Intent i4 = new Intent(getContext(), WorkFromHomeActivity.class);
                                        i4.putExtra("Date", DateFormat1(date.toString()));
                                        startActivity(i4);
                                        dialog.dismiss();
                                        break;


                                }
/*
                                }

                        });*/
                            } else {
                                EmpowerApplication.alertdialog("Please select Application type", getActivity());
                            }
                        }
                    });
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else {
                    EmpowerApplication.alertdialog("No Application available",getContext());
                }
            }
        });



    }

    // references to our items
	public String[] days;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {


            if (isVisibleToUser) {
                currentDate = Calendar.getInstance();
                // Load your data here or do network operations here


            }
        }
    }

}
