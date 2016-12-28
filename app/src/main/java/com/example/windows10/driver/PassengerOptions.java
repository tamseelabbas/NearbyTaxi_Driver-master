package com.example.windows10.driver;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PassengerOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_options);
    }

    public void onClickPassengerDetail(View view) {

        Intent i=new Intent(this,PassengerDetail.class);
        startActivity(i);
    }

    public void onClickCancelRequest(View view) {

        MapsActivity.removePassengerRequest();
        if(MapsActivity.Database!=null || MapsActivity.v!=null)
            MapsActivity.Database.removeEventListener(MapsActivity.v);
        if(MapsActivity.m!=null)
            MapsActivity.m.remove();
        MapsActivity.p=null;

        if(MapsActivity.polyline!=null)
            MapsActivity.polyline.remove();

        finish();
    }

    public void onClickCallPassenger(View view) {

        String url = "tel:" + MapsActivity.p.contactNumber;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling


            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE},
                    444);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

}
