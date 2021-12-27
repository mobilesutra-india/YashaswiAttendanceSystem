package seedcommando.com.yashaswi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by commando4 on 4/14/2018.
 */

public class AboutUs extends AppCompatActivity {

    String AboutUs="emSphere is a leading provider of Time and Attendance Management Solution which helps companies automate all " +
            "aspect of their Workforce Management processes. emSphere, has installations at Fortune 500 companies as well as at midsize companies," +
            " helping all with a tangible boost to organizations productivity.";
    String AboutUs1="Copyright © eMsphere Technologies, 2018. All rights reserved.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.about_us);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // toolbar.setLogo(R.drawable.yashaswi_logo);

        TextView aboutus= findViewById(R.id.aboutus);
        TextView aboutus1= findViewById(R.id.aboutus1);

        aboutus.setText(AboutUs);
        aboutus1.setText(AboutUs1);


    }
}
