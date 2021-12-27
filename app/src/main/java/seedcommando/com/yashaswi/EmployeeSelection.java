package seedcommando.com.yashaswi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSelection extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] employee = { "Vishnu-271092", "Vibha-1515151",
            "Rakesh-122334", "Ramesh-3443534",
            "DSA with java", "OS" };


    EmployeeAdapter adapter;

    List<Employee> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_selection);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comp off overtime approval");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.yashaswi_logo);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_fruits);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] fruits = getResources().getStringArray(R.array.item_fruits);
        String[] price = getResources().getStringArray(R.array.item_price);


        for (int i=0;i<fruits.length;i++){

            Employee fruits1 = new Employee(fruits[i],price[i],false);
            list.add(fruits1);
        }


        adapter = new EmployeeAdapter(EmployeeSelection.this,list);
        recyclerView.setAdapter(adapter);








        Spinner spino = findViewById(R.id.coursesspinner);
        spino.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, employee);

                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spino.setAdapter(ad);
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),employee[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }





}
