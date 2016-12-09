package com.example.windows10.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MyDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);



        TextView name=(TextView)findViewById(R.id.name2);
        TextView age=(TextView)findViewById(R.id.age2);
        TextView contactNumber=(TextView)findViewById(R.id.contactNumber2);
        TextView cnic=(TextView)findViewById(R.id.cnic2);



        if(MapsActivity.d!=null) {
            name.setText(MapsActivity.d.name);
            age.setText(Long.toString(MapsActivity.d.age));
            contactNumber.setText(MapsActivity.d.contactNumber);

            cnic.setText(MapsActivity.d.cnic);
        }
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this,"ondistroymydetail",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
