package com.example.windows10.driver;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,GoogleMap.OnInfoWindowClickListener  {

    private LatLng destinationLocation;
    private Marker destinationMarker;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker myLocation;
    private boolean moveToCurrentLocation=false;
    private int defaultZoom=17;
    public static Driver d;
    private DatabaseReference mDatabase;
    private String Uid;
    private String path="users/driver/";
    public static Marker m;
    public static Passenger p;
    static private AlertDialog dialog;
    static private AlertDialog.Builder builder;
    static public DatabaseReference Database;
    static public ValueEventListener v;
    static public Polyline polyline;
private boolean foo=true;

private void abc(){

//    if(!isFinishing())
    dialog.show();
}

    private void def() {
        builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Passenger sent you a request")
                .setTitle("Passenger request").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mDatabase = FirebaseDatabase.getInstance().getReference(path+Uid+"/r_status");
                mDatabase.setValue(1);

                mDatabase = FirebaseDatabase.getInstance().getReference("users/passenger/"+d.passengerKey+"/r_status");
                mDatabase.setValue(1);

                Database = FirebaseDatabase.getInstance().getReference("users/passenger/"+d.passengerKey);
                v=Database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.



                        Passenger passenger = dataSnapshot.getValue(Passenger.class);



                        if(passenger!=null) {
                            if(passenger.status==0){

                                m.remove();
                                m=null;
                                p=null;
                                Database.removeEventListener(v);

                                foo=true;
                                polyline.remove();

                            }

                            else {
                                if (m != null) {
                                    m.remove();
                                }

                                m = mMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                        .position(new LatLng(passenger.x, passenger.y))
                                        .title("Passenger")
                                        .snippet("This is passenger location"));
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(passenger.x, passenger.y), defaultZoom));
                                p=passenger;


                                String serverKey = "AIzaSyAgBqU-Rmq-_qL-VFFO8arr9u_I-bUHyIE";
                                final LatLng origin = new LatLng(d.x, d.y);

                                final LatLng destination = new LatLng(p.x, p.y);

if(foo) {
    GoogleDirection.withServerKey(serverKey)
            .from(origin)
            .to(destination)
            .transportMode(TransportMode.WALKING).unit(Unit.METRIC).alternativeRoute(true)
            .execute(new DirectionCallback() {
                @Override
                public void onDirectionSuccess(Direction direction, String rawBody) {
                    // Do something here
                    if (direction.isOK()) {


                        ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                        polyline=mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED));


                    }
                }

                @Override
                public void onDirectionFailure(Throwable t) {
                    // Do something here

                    Toast.makeText(MapsActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
    foo=false;
}

                            }

                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value

                    }
                });


                Toast.makeText(MapsActivity.this,"Request Accepted",Toast.LENGTH_SHORT).show();





            }
        }).setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mDatabase = FirebaseDatabase.getInstance().getReference(path+Uid+"/passengerKey");
                mDatabase.setValue("");





                mDatabase = FirebaseDatabase.getInstance().getReference(path+Uid+"/r_status");
                mDatabase.setValue(-1);

                mDatabase = FirebaseDatabase.getInstance().getReference("users/passenger/"+d.passengerKey+"/driverKey");
                mDatabase.setValue("");

                mDatabase = FirebaseDatabase.getInstance().getReference("users/passenger/"+d.passengerKey+"/r_status");
                mDatabase.setValue(-1);

                p=null;
                Toast.makeText(MapsActivity.this,"Request rejected",Toast.LENGTH_SHORT).show();
            }
        });
        dialog = builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        Uid=i.getStringExtra("Uid");
        def();






        mDatabase = FirebaseDatabase.getInstance().getReference(path+Uid+"/status");
        mDatabase.setValue(1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }
        ConnectGoogleClientApi();



        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void RequestHandler() {
        mDatabase = FirebaseDatabase.getInstance().getReference(path+Uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(d!=null){
                    if(d.r_status==-1 && dataSnapshot.getValue(Driver.class).r_status==0){
                        //passenger recive request



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                abc();
                            }
                        });
                    }
                    else if(d.r_status==0 && dataSnapshot.getValue(Driver.class).r_status==-1){

                        if(dialog.isShowing())
                        dialog.dismiss();

                    }
                    else if(d.r_status==1 && dataSnapshot.getValue(Driver.class).r_status==-1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(Database!=null || v!=null)
                                Database.removeEventListener(v);
                                if(m!=null)
                                    m.remove();
                                p=null;

                                if(polyline!=null)
                                polyline.remove();
                            }
                        });



                    }



                }


                d=dataSnapshot.getValue(Driver.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setDestinationLocationListner(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(destinationLocation!=null){

                    destinationMarker.remove();
                    polyline.remove();


                }
                destinationMarker=mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .anchor(0.5f, 0.5f) // Anchors the marker on the bottom left
                        .position(latLng)
                        .title("Destination Location")
                        .snippet("This is destination location"));
                destinationLocation=latLng;
                String serverKey = "AIzaSyAgBqU-Rmq-_qL-VFFO8arr9u_I-bUHyIE";
                final LatLng origin = new LatLng(d.x,d.y);

                final LatLng destination = new LatLng(latLng.latitude,latLng.longitude);

                GoogleDirection.withServerKey(serverKey)
                        .from(origin)
                        .to(destination)
                        .transportMode(TransportMode.WALKING).unit(Unit.METRIC).alternativeRoute(true)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                // Do something here
                                if (direction.isOK()) {


                                    ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                    polyline=mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED));


                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                // Do something here

                                Toast.makeText(MapsActivity.this, "fail", Toast.LENGTH_SHORT).show();
                            }
                        });



                Toast.makeText(MapsActivity.this,"Destination location set",Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void SetOnInfoWindowListener() {
        mMap.setOnInfoWindowClickListener(this);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.equals(myLocation)){
            Intent markerIntent=new Intent(this, MyDetail.class);
            startActivity(markerIntent);
        }

        else if(marker.equals(m)){

            Intent markerIntent=new Intent(this, PassengerOptions.class);
            startActivity(markerIntent);

        }

//        else{
//            p=(Passenger) marker.getTag();
//            Intent markerIntent=new Intent(this, com.example.windows10.driver.Marker.class);
//            startActivity(markerIntent);
//        }


    }



    @Override
    protected void onDestroy() {



        super.onDestroy();
//        Toast.makeText(this,"ondistroy",Toast.LENGTH_SHORT).show();


        removePassengerRequest();
        mDatabase = FirebaseDatabase.getInstance().getReference(path+Uid+"/status");
        mDatabase.setValue(0);

    }

    public static void removePassengerRequest() {
        if(p!=null){

            String pathh = "users/driver/" + d.key + "/passengerKey";
            StaticFunc.writeStringInFireBase(pathh, "");
            pathh = "users/driver/" + d.key + "/r_status";
            StaticFunc.writeLongInFireBase(pathh, (long) -1);


            pathh = "users/passenger/" + p.key + "/driverKey";
            StaticFunc.writeStringInFireBase(pathh, "");

            pathh = "users/passenger/" + p.key + "/r_status";
            StaticFunc.writeLongInFireBase(pathh, (long) -1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
//        Toast.makeText(this,"onstopcalled",Toast.LENGTH_SHORT).show();

    }



    private void ConnectGoogleClientApi() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        EnableCurrentLocationIcon();
        RequestHandler();
        SetOnInfoWindowListener();
        setDestinationLocationListner();
        if(mGoogleApiClient!=null && mLastLocation==null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        123);
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        if(mLastLocation!=null)
            movToCurrentLocation();
        else{
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }



    }
    private void EnableCurrentLocationIcon() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }
        mMap.setMyLocationEnabled(true);

    }
    private void movToCurrentLocation(){
        if(mLastLocation!=null) {

            LatLng l1 = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if(myLocation!=null){
                myLocation.remove();
            }
            myLocation=mMap.addMarker(new MarkerOptions().position(l1).title("Marker in your locatioon").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            if(moveToCurrentLocation==false) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l1, defaultZoom));
                moveToCurrentLocation=true;
            }
        }

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    private void CheckGpsAvailibility() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this,100);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
    }



    private void startLocationUpdates(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {


        createLocationRequest();
//        CheckGpsAvailibility();
        startLocationUpdates();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mDatabase=FirebaseDatabase.getInstance().getReference(path+Uid+"/x");
        mDatabase.setValue(location.getLatitude());
        mDatabase=FirebaseDatabase.getInstance().getReference(path+Uid+"/y");
        mDatabase.setValue(location.getLongitude());
        if(location!=null){
            mLastLocation=location;


        }

        movToCurrentLocation();



    }


}
