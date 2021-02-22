package com.example.getrides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastlocation;
    LocationRequest locationRequest;
//    Button button;
    Button button2;

    Marker mCurrLocationMarker;

    private SupportMapFragment mapFragment;

    private LatLng pickuplocation;

    private  boolean requestStatus= false;

    private Marker m;

   public String userid;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    TextView nameDri;
    TextView phoneDri;
    TextView emailDri;
    TextView carnumDri;
    TextView distanceDri;
    ImageView profilePicDri;
    ImageView callDri;

    private String driverfoundId;

    RelativeLayout relativeLayout2;


    String DriverName;
    String DriverPhone;
    String DriverEmail;
    String DriverCarNum;
    String DriverImg;

    String destination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_map);

//        button = findViewById(R.id.logout2);
        button2 = findViewById(R.id.reqRide);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);


         nameDri = findViewById(R.id.driverName);
         phoneDri= findViewById(R.id.driverNumber);
         emailDri= findViewById(R.id.driverEmailAddress);
         carnumDri= findViewById(R.id.driverCarnum);
         distanceDri= findViewById(R.id.driverDistance);
         profilePicDri= findViewById(R.id.profilePictureDRi);
         callDri= findViewById(R.id.callDri);
         relativeLayout2=findViewById(R.id.relativepasenger);
//        Places.initialize(getApplicationContext(),"AIzaSyDTkZSxUWMoV4d2oUErA1ZgF-p7avWU3q4");
//        PlacesClient placesClient =Places.createClient(this);
//
//
//        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NotNull Place place) {
//                // TODO: Get info about the selected place.
//               destination= place.getName();
//            }
//
//
//            @Override
//            public void onError(@NotNull Status status) {
//                // TODO: Handle the error.
//                Log.i("TAG", "An error occurred: " + status);
//            }
//        });

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            mapFragment.getMapAsync(this);
//        }//
//            ActivityCompat.requestPermissions(DriverMap.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Location_Request_Code);
//        }else {
//
//        drawerLayout= findViewById(R.id.drawer);
//        navigationView= findViewById(R.id.nav);
//        toolbar= findViewById(R.id.toolbar);



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(PassengerMap.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(requestStatus){
                    requestStatus= false;
                    geoQuery.removeAllListeners();
                    driverlocation.removeEventListener(driverlocationValueEventLisner);
                    relativeLayout2.setVisibility(View.GONE);


                    if (driverFound!=null){
                        customerFoundId = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverfoundId).child("customerRideId");
                        customerFoundId.removeValue();
                        driverfoundId = null;


                    }
                    driverFound = false;
                    radius = 1;

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference reference = database.getReference("PassengerAvailable");
                    GeoFire geoFire = new GeoFire(reference);
                    geoFire.removeLocation(userid);

                    if (marker != null){
                        marker.remove();
                    }
                    button2.setText("Request Ride");



                }else {
                    requestStatus= true;

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                     userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference reference = database.getReference("PassengerAvailable");

                    GeoFire geoFire = new GeoFire(reference);
                    geoFire.setLocation(userid,new GeoLocation(lastlocation.getLatitude(),lastlocation.getLongitude()));

                    pickuplocation= new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());

                   mMap.addMarker(new MarkerOptions().position(pickuplocation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.user))).setTitle("pickUp Location");
                    button2.setText("Requesting...");

                    getClosestDriver();
                }



            }


            private Boolean driverFound = false;

            private int radius = 1;

            private   GeoQuery geoQuery;
            private DatabaseReference customerFoundId;
            private void getClosestDriver() {

                DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("AvailableDrivers");

                GeoFire geoFire = new GeoFire(driverLocation);
                geoQuery = geoFire.queryAtLocation(new GeoLocation(pickuplocation.latitude,pickuplocation.longitude),radius);
                geoQuery.removeAllListeners();
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {

                        if (!driverFound)
                        {

                        driverFound=true;
                        driverfoundId=key;

                             customerFoundId = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverfoundId);

                            String customerId = FirebaseAuth.getInstance().getUid();

                            HashMap map = new HashMap();
                            map.put("customerRideId",customerId);

                            customerFoundId.updateChildren(map);

                            button2.setText("Finding Your Ride...");

                            getDriverlocation();

                        }
                    }




                    @Override
                    public void onKeyExited(String key) {

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {

                        if (!driverFound){

                            radius++;
                            getClosestDriver();
                        }

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });

            }


            private Marker marker;
            private DatabaseReference driverlocation;
            private ValueEventListener driverlocationValueEventLisner;
            private void getDriverlocation() {

                getdriverInfo();


                driverlocation = FirebaseDatabase.getInstance().getReference().child("AvailableDrivers").child(driverfoundId).child("l");

                driverlocationValueEventLisner = driverlocation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.exists()){



                            List<Object> map = (List<Object>) snapshot.getValue();
                            button2.setText("Driver Found");
                             double latLocation = 0;
                             double lngLocation = 0;

                             if (map.get(0)!=null){
                                 latLocation = Double.parseDouble(map.get(0).toString());
                             }
                            if (map.get(1)!=null){
                                lngLocation = Double.parseDouble(map.get(1).toString());
                            }

                            LatLng driverlatlng =new LatLng(latLocation,lngLocation);

                            if (marker != null){
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(driverlatlng).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.driver)));

                            Location loc = new Location("");
                            loc.setLatitude(pickuplocation.latitude);
                            loc.setLongitude(pickuplocation.longitude);

                            Location loc2 = new Location("");
                            loc2.setLatitude(driverlatlng.latitude);
                            loc2.setLongitude(driverlatlng.longitude);

                            float distance = loc.distanceTo(loc2);

                            if (distance<100){
                                button2.setText("Driver Arrived");
                            }else {

                               float f = Math.round(distance/100);

                                String distanceKm = (String.valueOf(f));





                                distanceDri.setText("Distance: " + distanceKm + " KM");
                                button2.setText("Cancel Ride");
                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

    }

    private void getdriverInfo() {
        relativeLayout2.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverfoundId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Map<String,Object> map = (Map<String, Object>) snapshot.getValue();

                    if (map.get("Dname")!=null){
                        DriverName=map.get("Dname").toString();
                        nameDri.setText(DriverName);
                    }
                    if (map.get("Dphone")!=null){
                        DriverPhone=map.get("Dphone").toString();
                        phoneDri.setText(DriverPhone);
                    }
                    if (map.get("Demail")!=null){
                        DriverEmail=map.get("Demail").toString();
                        emailDri.setText(DriverEmail);
                    }
                    if (map.get("DcarNumber")!=null){
                        DriverCarNum=map.get("DcarNumber").toString();
                        carnumDri.setText("Car Num: "+DriverCarNum);
                    }
                    if (map.get("image")!=null){
                        DriverImg=map.get("image").toString();

//                        Uri uri= (Uri) map.get("image");
                        Glide.with(getApplicationContext()).load(DriverImg).into(profilePicDri);
                    }
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
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.user));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));






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
                                ActivityCompat.requestPermissions(com.example.getrides.PassengerMap.this,
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
                    Toast.makeText(com.example.getrides.PassengerMap.this, "permission denied", Toast.LENGTH_SHORT).show();
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PassengerAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.examplemenu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.item11:
                Intent intent1 = new Intent(PassengerMap.this,PassengerProfile.class);
                startActivity(intent1);


                return true;

            case R.id.item22:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item33:

                Intent intent3 = new Intent(PassengerMap.this,PassengerLogin.class);
                startActivity(intent3);
                finish();

                return true;

            default:  return super.onOptionsItemSelected(item);
        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.onCreate(null);
//    }
}