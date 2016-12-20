package com.example.windows10.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PassengerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_detail);


        TextView name=(TextView)findViewById(R.id.name2);
        TextView age=(TextView)findViewById(R.id.age2);
        TextView contactNumber=(TextView)findViewById(R.id.contactNumber2);
        TextView cnic=(TextView)findViewById(R.id.cnic2);



        if(MapsActivity.p!=null) {
            name.setText(MapsActivity.p.name);
            age.setText(Long.toString(MapsActivity.p.age));
            contactNumber.setText(MapsActivity.p.contactNumber);

            cnic.setText(MapsActivity.p.cnic);
        }
    }
}
