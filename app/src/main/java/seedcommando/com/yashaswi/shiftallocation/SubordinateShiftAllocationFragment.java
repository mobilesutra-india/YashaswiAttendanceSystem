package seedcommando.com.yashaswi.shiftallocation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.CommanResponsePojo;
import seedcommando.com.yashaswi.pojos.shiftallocation.shifts.Data;
import seedcommando.com.yashaswi.pojos.shiftallocation.shifts.ShiftsData;
import seedcommando.com.yashaswi.pojos.shiftallocation.subshift.SubOrdinateShift;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by commando1 on 9/18/2017.
 */

public class SubordinateShiftAllocationFragment  extends Fragment {

    ArrayList<ShiftPOJO> arrayList;
    ListView listView,list_views;
    ArrayList<ShiftDaysPOJO> arrayList_days;
    static   ArrayList<String>  arrayList_shiftName;
    private ApiInterface apiService;
    ProgressDialog pd;
    ArrayList<String> dates,dates12;
    TextView textView1,textView2,textView3,textView4,textView5,textView6;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM");

    public SubordinateShiftAllocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subordinate_shift_allocation_fragment, container, false);

        textView1=rootView.findViewById(R.id.textView_shift1);
        textView2=rootView.findViewById(R.id.textView_shift2);
        textView3=rootView.findViewById(R.id.textView_shift3);
        textView4=rootView.findViewById(R.id.textView_shift4);
        textView5=rootView.findViewById(R.id.textView_shift5);
        textView6=rootView.findViewById(R.id.textView_shift6);
        listView = rootView.findViewById(R.id.list_shift_allocation);
        list_views = rootView.findViewById(R.id.list_shift_days);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        dates=new ArrayList<>();
        dates12=new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            String day = sdf.format(calendar.getTime());
            String day12 = sdf1.format(calendar.getTime());
            dates.add(day);
            dates12.add(day12);
            //Log.i(TAG, day);
        }

        textView1.setText(dates12.get(0));
        textView2.setText(dates12.get(1));
        textView3.setText(dates12.get(2));
        textView4.setText(dates12.get(3));
        textView5.setText(dates12.get(4));
        textView6.setText(dates12.get(5));

        if (Utilities.isNetworkAvailable(getActivity())) {

            if(ShiftAllocationActivity.position==1) {
                getShifts();
                subOrdinateData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
            }

        } else {
                Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();

            }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TextView tv1 = (TextView) view.findViewById(R.id.star);
                    TextView name =
                            view.findViewById(R.id.textView_name_shift);
                    final TextView one_TextView =
                            view.findViewById(R.id.button1);
                    final TextView two_TextView =
                            view.findViewById(R.id.button2);
                    final TextView three_TextView =
                            view.findViewById(R.id.button3);
                    final TextView four_TextView =
                            view.findViewById(R.id.button4);
                    final TextView five_TextView =
                            view.findViewById(R.id.button5);
                    final TextView six_TextView =
                            view.findViewById(R.id.button6);

                    one_TextView.setOnClickListener(new View.OnClickListener() {
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
                    });

                }
            });

        return rootView;
    }
    //shiftAllocate
    public void subOrdinateData(String employeeId) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading....");
        pd.show();
        Map<String,String> subOrdinateData=new HashMap<String,String>();
        subOrdinateData.put("employeeId",employeeId);
        subOrdinateData.put("fromDate",dates.get(0));
        subOrdinateData.put("toDate",dates.get(5));
        Log.e("dates",dates.get(0)+""+dates.get(5));

        arrayList_days = new ArrayList<ShiftDaysPOJO>();
        arrayList = new ArrayList<ShiftPOJO>();

        retrofit2.Call<SubOrdinateShift> call = apiService.getSubOrdinateShiftData(subOrdinateData);
        call.enqueue(new Callback<SubOrdinateShift>() {
            @Override
            public void onResponse(retrofit2.Call<SubOrdinateShift> call, Response<SubOrdinateShift> response) {
                pd.dismiss();
                //id.clear();
                //date1.clear();
                arrayList_days.clear();
                arrayList.clear();

                if (response.isSuccessful()) {
                    HashSet<String> itemID = new HashSet<String>();
                    itemID.clear();
                    LinkedHashSet<String> itemDate = new LinkedHashSet<String>();
                    itemDate.clear();
                    LinkedHashSet<String> shiftType = new LinkedHashSet<String>();
                    itemDate.clear();
                    // reason12.add("Select");

                    if (response.body().getStatus().equals("1")) {
                        for(int i=0;i<response.body().getData().size();i++) {
                            itemID.add(response.body().getData().get(i).getEmployeeID());
                            itemDate.add(response.body().getData().get(i).getShiftDate());
                            // Log.e("Dates",response.body().getData().get(i).getShiftDate());
                            if(!response.body().getData().get(i).getShift().isEmpty()) {
                                shiftType.add(response.body().getData().get(i).getShift());
                            }
                        }
                        ArrayList<String> id=new ArrayList<>(itemID);
                        ArrayList<String> date1=new ArrayList<String>(itemDate);
                        ArrayList<String>shiftCount=new ArrayList<String>(shiftType);
                        for(int i=0;i<id.size();i++) {
                            ShiftPOJO shiftPOJO=new ShiftPOJO();
                            shiftPOJO.setId(id.get(i));
                            for (int j = 0; j < date1.size(); j++) {
                                for(int k=0;k<response.body().getData().size();k++) {
                                    if (id.get(i).equals(response.body().getData().get(k).getEmployeeID()) && date1.get(j).equals(response.body().getData().get(k).getShiftDate())){
                                        shiftPOJO.setName(response.body().getData().get(k).getEmployeeName());
                                        shiftPOJO.setEmpcode(response.body().getData().get(k).getEmployeeCode());

                                        if(date1.get(0).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftPOJO.setDayone(response.body().getData().get(k).getShift());
                                            shiftPOJO.setDate1(response.body().getData().get(j).getShiftDate());
                                        }
                                        if(date1.get(1).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftPOJO.setDaytwo(response.body().getData().get(k).getShift());
                                            shiftPOJO.setDate2(response.body().getData().get(j).getShiftDate());
                                        }
                                        if(date1.get(2).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftPOJO.setDaythree(response.body().getData().get(k).getShift());
                                            shiftPOJO.setDate3(response.body().getData().get(j).getShiftDate());
                                        }
                                        if(date1.get(3).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftPOJO.setDayfour(response.body().getData().get(k).getShift());
                                            shiftPOJO.setDate4(response.body().getData().get(j).getShiftDate());
                                        }
                                        if(date1.get(4).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftPOJO.setDayfive(response.body().getData().get(k).getShift());
                                            shiftPOJO.setDate5(response.body().getData().get(j).getShiftDate());
                                        }
                                        if(date1.get(5).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftPOJO.setDaysix(response.body().getData().get(k).getShift());
                                            shiftPOJO.setDate6(response.body().getData().get(j).getShiftDate());
                                        }
                                    }

                                }
                            }
                            arrayList.add(shiftPOJO);
                        }


                        for (String l:shiftCount) {
                            Log.e("shifts",l);

                        }

                        for(int i=0;i<shiftCount.size();i++) {
                            int a=0,b=0,c=0,d=0,e=0,f=0;
                            ShiftDaysPOJO shiftdaysPOJO = new ShiftDaysPOJO();
                            //shiftdaysPOJO.setId(id.get(i));
                            for (int j = 0; j < date1.size(); j++) {
                                for(int k=0;k<response.body().getData().size();k++) {
                                    if (shiftCount.get(i).equals(response.body().getData().get(k).getShift()) && date1.get(j).equals(response.body().getData().get(k).getShiftDate())){
                                        shiftdaysPOJO.setShifttype(response.body().getData().get(k).getShift());
                                        Log.e("shifts",response.body().getData().get(k).getShift());
                                        if(date1.get(0).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftdaysPOJO.setShiftone(++a);
                                            //Log.e("shifts1",String.valueOf(a++));
                                        }
                                        if(date1.get(1).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftdaysPOJO.setShifttwo(++b);
                                            //Log.e("shifts2",String.valueOf(b++));
                                        }
                                        if(date1.get(2).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftdaysPOJO.setShiftthree(++c);
                                            //Log.e("shifts3",String.valueOf(c++));
                                        }
                                        if(date1.get(3).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftdaysPOJO.setShiftfour(++d);
                                            //Log.e("shifts4",String.valueOf(d++));
                                        }
                                        if(date1.get(4).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftdaysPOJO.setShiftfive(++e);
                                            //Log.e("shifts5",String.valueOf(e++));
                                        }
                                        if(date1.get(5).equals(response.body().getData().get(k).getShiftDate())){
                                            shiftdaysPOJO.setShiftsix(++f);
                                            //Log.e("shifts6",String.valueOf(f++));
                                        }
                                    }

                                }
                            }
                            arrayList_days.add(shiftdaysPOJO);
                        }
                        //Log.e("sssss",shiftPOJO.getDayone());
                        ShiftAdapter adapter = new ShiftAdapter(getActivity(), arrayList);
                        listView.setAdapter(adapter);

                        ShiftDaysAdapter adapter1 = new ShiftDaysAdapter(getActivity(), arrayList_days);
                        list_views.setAdapter(adapter1);

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), getActivity());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getActivity());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<SubOrdinateShift> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public void getShifts() {
        Map<String,String> sData=new HashMap<String,String>();
        sData.put("employeeId",EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        arrayList_shiftName = new ArrayList<String>();
        //arrayList = new ArrayList<ShiftPOJO>();
        retrofit2.Call<ShiftsData> call = apiService.getShiftData(sData);
        call.enqueue(new Callback<ShiftsData>() {
            @Override
            public void onResponse(retrofit2.Call<ShiftsData> call, Response<ShiftsData> response) {

                arrayList_days.clear();
                arrayList.clear();

                if (response.isSuccessful()) {
                    if(response.body().getStatus().equals("1")){
                        if(!response.body().getData().isEmpty()){
                            for (Data d: response.body().getData()) {
                                arrayList_shiftName.add(d.getShiftName());

                            }

                            }

                        //ShiftDaysAdapter adapter1 = new ShiftDaysAdapter(getActivity(), arrayList_days);
                        //list_views.setAdapter(adapter1);

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), getActivity());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getActivity());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<ShiftsData> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    public void dialogForSpineer(final String empcode, final String date, final String name){

        final Dialog dialog = new Dialog(getActivity());
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,arrayList_shiftName);
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
                if (Utilities.isNetworkAvailable(getActivity())) {
                    getShifts();

                    AllocateShift(empcode,date,spinner.getSelectedItem().toString());
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();

                }


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

    public void AllocateShift( String empcode,String shidtdate,String shiftname) {
        Map<String,String> sData=new HashMap<String,String>();
        sData.put("allocatedToEmployeeCode",empcode);
        sData.put("shiftDate",shidtdate);
        sData.put("shiftName",shiftname);
        sData.put("createdByEmployeeID",EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        sData.put("token",EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));

        retrofit2.Call<CommanResponsePojo> call = apiService.shiftAllocate(sData);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equals("1")){
                        if (Utilities.isNetworkAvailable(getActivity())) {
                           // getShifts();
                            subOrdinateData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                        } else {
                            Toast.makeText(getActivity(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), getActivity());
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", getActivity());
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", getActivity());

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                // Load your data here or do network operations here

                if (ShiftAllocationActivity.position == 1) {
                    if (Utilities.isNetworkAvailable(getContext())) {
                        getShifts();
                        subOrdinateData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                        // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                }
            }


        }
    }
}
