package com.example.getrides;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;


public class DriverMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
     GoogleApiClient googleApiClient;
    Location lastlocation;
    LocationRequest locationRequest;
    Button button;

    Marker mCurrLocationMarker;

    private SupportMapFragment mapFragment;

    private String coustomerid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

        button = findViewById(R.id.logout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(DriverMap.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Location_Request_Code);
//        }else {
//            mapFragment.getMapAsync(this);
//        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                

                Intent intent = new Intent(DriverMap.this,MainActivity.class);
                startActivity(intent);
                finish();

            }

        });
        getassignedcustomer();
    }

    private void getassignedcustomer() {

        String driverId= FirebaseAuth.getInstance().getUid();
        DatabaseReference customerFoundId = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverId).child("customerRideId");

        customerFoundId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){


                       coustomerid = snapshot.getValue().toString();

                       getassignedcustomerlocation();

                }else {

                    coustomerid = "";

                    if (marker!= null){

                        marker.remove();

                    }

                    if (customerRequestedRideVlaueventlisner!=null){

                        customerRequestedRide = FirebaseDatabase.getInstance().getReference().child("PassengerAvailable").child(coustomerid).child("l");
                        customerRequestedRide.removeEventListener(customerRequestedRideVlaueventlisner);
                        Toast.makeText(DriverMap.this, "Passenger Canceled The Ride ", Toast.LENGTH_LONG).show();
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private Marker marker;
    private DatabaseReference customerRequestedRide;
    private ValueEventListener customerRequestedRideVlaueventlisner;
    private void getassignedcustomerlocation() {

       customerRequestedRide = FirebaseDatabase.getInstance().getReference().child("PassengerAvailable").child(coustomerid).child("l");

        customerRequestedRideVlaueventlisner = customerRequestedRide.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && !coustomerid.equals("")){

                    List<Object> map = (List<Object>) snapshot.getValue();

                    double latLocation = 0;
                    double lngLocation = 0;

                    if (map.get(0)!=null){
                        latLocation = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1)!=null){
                        lngLocation = Double.parseDouble(map.get(1).toString());
                    }

                    LatLng Customerlatlng =new LatLng(latLocation,lngLocation);

                    marker = mMap.addMarker(new MarkerOptions().position(Customerlatlng).title("Pickup Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.user)));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildgoogleapi();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildgoogleapi();
            mMap.setMyLocationEnabled(true);
        }

//        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(DriverMap.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},Location_Request_Code);
//        }
//
//        buildgoogleapi();
//        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildgoogleapi() {

        googleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        lastlocation= location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

         LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.driver));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
         mMap.animateCamera(CameraUpdateFactory.zoomTo(17));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference refAvailable = database.getReference("AvailableDrivers");
        DatabaseReference refWorking = database.getReference("driverWorking");
        GeoFire geoFire = new GeoFire(refAvailable);
        GeoFire geoFire2 = new GeoFire(refWorking);


        switch (coustomerid){
            case "":
                geoFire2.removeLocation(userid);
                geoFire.setLocation(userid,new GeoLocation(location.getLatitude(),location.getLongitude()));
                break;

            default:
                geoFire.removeLocation(userid);
                geoFire2.setLocation(userid,new GeoLocation(location.getLatitude(),location.getLongitude()));
                break;

        }


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(com.example.getrides.DriverMap.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    final int Location_Request_Code=1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            buildgoogleapi();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(com.example.getrides.DriverMap.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

//        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AvailableDrivers");
//
//        GeoFire geoFire = new GeoFire(reference);
//        geoFire.removeLocation(userid);

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AvailableDrivers");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
        FirebaseAuth.getInstance().signOut();
    }
}