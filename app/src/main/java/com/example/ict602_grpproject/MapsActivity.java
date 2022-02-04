package com.example.ict602_grpproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class MapsActivity extends FragmentActivity {

    //initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    Location currentLocation;

    RequestQueue queue;
    final String URL = "http://www.ict602.ml/getReports.php";
    Gson gson;
    Marker[] markerList;
    MarkerOptions marker;
    Vector<MarkerOptions> markerOptions;
    private GoogleMap mMap;

    String currentUserGlobal;

    FloatingActionButton btnOpen, btnAdd, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        gson = new GsonBuilder().create();

        //start of map
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        //end of map

        //data passing
        Bundle extras = getIntent().getExtras();

        //if so, set all the edittext's and textview's based on values retrieved
        //Location currentLocation = extras.getParcelable("currentLocation");
        //if log in succressful, pass user id through intent ni
        //String userID = extras.getString("userID");

        //for now, dummy
        String userID = "6";
        currentUserGlobal = userID;
        //String finalUserID = userID;

        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnLogOut = (FloatingActionButton) findViewById(R.id.btnLogOut);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, AddMarker.class);

                //pass the current location to the add marker class through intent shit
                i.putExtra("currentLocation", currentLocation);
                i.putExtra("userID", userID);
                startActivity(i);

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //actions bila user log out
            }
        });

    }

    private void getCurrentLocation() {
        //Initialize task location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //When success
                if (location != null) {
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            mMap = googleMap;

                            //Initialize Lat Lng
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            //Create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am here");
                            //zoom map scale 15
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            googleMap.addMarker(options);
                            /*
                            for(MarkerOptions mark: markerOptions){
                                mMap.addMarker(mark);
                            } */

                            currentLocation = location;
                            sendRequest();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //Get current location when permission granted
                getCurrentLocation();
            }
        }
    }

    public void sendRequest(){
        queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, onSuccess, onError);
        queue.add(stringRequest);

    }

    public Response. Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            HashMap<LatLng, String> map = new HashMap<LatLng, String>();
            markerList = gson.fromJson(response, Marker[].class);

            if(markerList.length < 1){
                Toast.makeText(getApplicationContext(), "No Hazards Recorded!", Toast.LENGTH_LONG).show();
                return;
            }

            for(Marker info: markerList){
                Double lat= Double.valueOf(info.getLatitude());
                Double lng = Double.valueOf(info.getLongitude());
                String title = info.getHazard();
                String snippet = info.getReportedBy();
                String pass = info.getReportID() + "#" + info.getHazardID() + "#" + info.getUserID();

                //Toast.makeText(getApplicationContext(), "Added Marker!" + lat.toString() + ", " + lng.toString(), Toast.LENGTH_LONG).show();

                MarkerOptions marker= new MarkerOptions().position(new LatLng(lat,lng))
                        .title(title)
                        .snippet(snippet);

                map.put(new LatLng(lat, lng), pass);
                mMap.addMarker(marker);


                //on marker click

            } // end of for

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull com.google.android.gms.maps.model.Marker marker) {
                    final boolean[] isEditable = {false};
                    Intent i = new Intent(MapsActivity.this, EditMarker.class);

                    String receivedString = map.get(marker.getPosition());
                    String[] outputToken = receivedString.split("#");

                    String reportID = outputToken[0];
                    String hazardID = outputToken[1];
                    String userID = outputToken[2];



                    Toast.makeText(getApplicationContext(), "reportID: " + reportID +
                            " hazardID: " + hazardID + " userID: " + userID, Toast.LENGTH_LONG).show();

                    if(currentUserGlobal.equals(userID)){
                        isEditable[0] = true;
                        Toast.makeText(getApplicationContext(), "Perh boleh edit sia", Toast.LENGTH_LONG).show();
                    }

                    i.putExtra("userID", currentUserGlobal);
                    i.putExtra("reportID",reportID);
                    i.putExtra("hazardID", hazardID);
                    i.putExtra("isEditable", isEditable[0]);
                    startActivity(i);
                    //Toast.makeText(getApplicationContext(), "Clicked: " + map.get(marker.getPosition()), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });

        }//end of onresponse

    }; //end of sendrequest

    public Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}