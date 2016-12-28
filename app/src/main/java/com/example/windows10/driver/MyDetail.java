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
            name.setText(name.getText()+MapsActivity.d.name);
            age.setText(age.getText()+Long.toString(MapsActivity.d.age));
            contactNumber.setText(contactNumber.getText()+MapsActivity.d.contactNumber);

            cnic.setText(cnic.getText()+MapsActivity.d.cnic);
        }
    }

    @Override
    protected void onDestroy() {
//        Toast.makeText(this,"ondistroymydetail",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
