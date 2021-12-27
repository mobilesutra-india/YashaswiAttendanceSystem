package seedcommando.com.yashaswi.manageractivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.yashaswi.R;
import seedcommando.com.yashaswi.Utilities;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;
import seedcommando.com.yashaswi.pojos.ManagerPoJo.ClaenderPoJo;
import seedcommando.com.yashaswi.rest.ApiClient;
import seedcommando.com.yashaswi.rest.ApiInterface;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout
{
	// for logging
	private static final String LOGTAG = "Calendar View";

	// how many days to show, defaults to six weeks, 42 days
	private static final int DAYS_COUNT = 42;

	// default date format
	private static final String DATE_FORMAT = "MMM yyyy";

	// default date format
	private static final String DATE_FORMAT_calender = "dd-MMM-yyyy";

	// date format
	private String dateFormat;

	// current displayed month
	private Calendar currentDate = CalenderFragment.currentDate;

	//event handling
	private EventHandler eventHandler = null;

	// internal components
	private LinearLayout header;
	private ImageView btnPrev;
	private ImageView btnNext;
	private TextView txtDate;
	public static GridView grid;

	ProgressDialog pd;
	private ApiInterface apiService;
	static ArrayList<String> daystatus;
	 public static ArrayList<Date> cells;

	// month-season association (northern hemisphere, sorry australia :)
	int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

	public CalendarView(Context context)
	{
		super(context);
		currentDate = Calendar.getInstance();
	}

	public CalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initControl(context, attrs);
		currentDate = Calendar.getInstance();
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initControl(context, attrs);
		currentDate = Calendar.getInstance();
	}



	/**
	 * Load control xml layout
	 */
	private void initControl(Context context, AttributeSet attrs)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.control_calendar, this);


		currentDate = Calendar.getInstance();
		apiService =
				ApiClient.getClient().create(ApiInterface.class);
		loadDateFormat(attrs);
		assignUiElements();
		assignClickHandlers();


		//updateCalendar();
	}



	private void loadDateFormat(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

		try
		{
			// try to load provided date format, and fallback to default otherwise
			dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
			if (dateFormat == null)
				dateFormat = DATE_FORMAT;
		}
		finally
		{
			ta.recycle();
		}
	}
	private void assignUiElements()
	{
		// layout is inflated, assign local variables to components
		header = findViewById(R.id.calendar_header);
		btnPrev = findViewById(R.id.calendar_prev_button);
		btnNext = findViewById(R.id.calendar_next_button);
		txtDate = findViewById(R.id.calendar_date_display);
		grid = findViewById(R.id.calendar_grid);
		grid.setBackgroundColor(Color.parseColor("#d3d3d3"));
		grid.setVerticalSpacing(2);
		grid.setHorizontalSpacing(2);
	}

	private void assignClickHandlers()
	{
		// add one month and refresh UI
		btnNext.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				currentDate.add(Calendar.MONTH, 1);
				updateCalendar();
			}
		});

		// subtract one month and refresh UI
		btnPrev.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				currentDate.add(Calendar.MONTH, -1);
				updateCalendar();
			}
		});


		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				eventHandler.onDayClick((Date) parent.getItemAtPosition(position));
			}
		});

		txtDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(getContext(), date, currentDate
						.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
						currentDate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			// TODO Auto-generated method stub
			currentDate.set(Calendar.YEAR, year);
			currentDate.set(Calendar.MONTH, monthOfYear);
			currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			//updateLabel();
			SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_calender);
			Log.e("data123456789",sdf1.format(currentDate.getTime()));
			updateCalendar();
		}

	};
	private void updateLabel() {
		//String myFormat = "MMMyy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		txtDate.setText(sdf.format(currentDate.getTime()));
		updateCalendar();
	}


	/*private void setDateTimeField() {
		//Calendar dateSelected = Calendar.getInstance();
		DatePickerDialog datePickerDialog;
		Calendar newCalendar = currentDate;
		datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				//currentDate.set(year, monthOfYear, dayOfMonth, 0, 0);
				currentDate.set(year, monthOfYear, dayOfMonth, 0, 0);
				updateCalendar();
				//dateEditText.setText(dateFormatter.format(dateSelected.getTime()));
			}

		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();

		//dateEditText.setText(dateFormatter.format(dateSelected.getTime()));
	}
*/

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar()
	{
		updateCalendar(null);
	}

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar(HashSet<Date> events)
	{
		SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_calender);
		//if(HomeFragment.position==0) {
			if (Utilities.isNetworkAvailable(getContext())) {

				getCalenderData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")), sdf1.format(currentDate.getTime()), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
			} else {
				Toast.makeText(getContext(), "No Internet Connection...", Toast.LENGTH_LONG).show();
			}
		//}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		txtDate.setText(sdf.format(currentDate.getTime()));
		cells = new ArrayList<>();
		Calendar calendar = (Calendar)currentDate.clone();
		// determine the cell for current month's beginning
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		// move calendar backwards to the beginning of the week
		calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

		// fill cells
		while (cells.size() < DAYS_COUNT)
		{
			cells.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}


	}


	public void getCalenderData(String empid, final String date, String token) {

		pd = new ProgressDialog(getContext());
		pd.setMessage("loading");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		final Map<String,String> calenderdata=new HashMap<String,String>();
		calenderdata.put("employeeId",empid);
		calenderdata.put("date",date);
		calenderdata.put("token",token);
		Log.e("DateString",date);
		final ArrayList<String> calenderdate=new ArrayList<>();
		 daystatus=new ArrayList<>();

		retrofit2.Call<ClaenderPoJo> call = apiService.getCalenderData(calenderdata);
		call.enqueue(new Callback<ClaenderPoJo>() {
			@Override
			public void onResponse(retrofit2.Call<ClaenderPoJo> call, Response<ClaenderPoJo> response) {

				pd.dismiss();

				if(response.isSuccessful()) {
					//Log.d("User ID: ", response.body().getMessage());

					if (response.body().getStatus().equals("1")) {
						if(pd.isShowing()){
							pd.dismiss();
						}
						Log.e("responsedata", response.body().getStatus());
						int count = response.body().getData().size();
						for (int i = 0; i < count; i++) {
							Log.e("arraydata1", String.valueOf(response.body().getData().get(i).getDayStatusCode()));
							Log.e("arraydata2", String.valueOf(response.body().getData().get(i).getShiftDate()));
							calenderdate.add(response.body().getData().get(i).getShiftDate());
							daystatus.add(response.body().getData().get(i).getDayStatusCode());


						}

						grid.setAdapter(new CalendarAdapter(getContext(), currentDate, cells, daystatus));



						Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
						//startActivity(new Intent(RegistrationActivity.this,LicenseActivity.class));

					} else {
						EmpowerApplication.alertdialog(response.body().getMessage(), getContext());

					}
				}else {
					switch (response.code()) {
						case 404:
							//Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
							EmpowerApplication.alertdialog("File or directory not found", getContext());
							break;
						case 500:
							EmpowerApplication.alertdialog("server broken", getContext());

							//Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
							break;
						default:
							EmpowerApplication.alertdialog("unknown error", getContext());

							//Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
							break;
					}
				}



			}

			@Override
			public void onFailure(retrofit2.Call<ClaenderPoJo> call, Throwable t) {
				// Log error here since request failed
				Log.e("TAG", t.toString());
				pd.dismiss();

			}
		});
	}




	/**
	 * Assign event handler to be passed needed events
	 */
	public void setEventHandler(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}

	/**
	 * This interface defines what events to be reported to
	 * the outside world
	 */
	public interface EventHandler
	{
		void onDayClick(Date date);
	}




/*

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Do something that differs the Activity's menu here
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_notification) {
			// stub = (ViewStub)view.findViewById(R.id.layout_stub);

			return false;
		}
		if (id == R.id.action_sync) {
			// stub = (ViewStub)view.findViewById(R.id.layout_stub);


			if(Utilities.isNetworkAvailable(this.getContext())) {
				getEmployeeConfig(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));

				// getPendingAprovalData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
			}else {
				Toast.makeText(this.getContext(),"No Internet Connection...",Toast.LENGTH_LONG).show();
			}


			return true;
		}
		return false;
	}

*/



}

