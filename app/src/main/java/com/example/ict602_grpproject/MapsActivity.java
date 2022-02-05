package com.example.ict602_grpproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    TextView Ptitle, Pusername, Pdatetime; Button PbtnClose;

    //initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    FloatingActionButton btnTest;

    Location currentLocation;

    RequestQueue queue;
    final String URL = "http://www.ict602.ml/getReports.php";
    Gson gson;
    Marker[] markerList;
    MarkerOptions marker;
    Vector<MarkerOptions> markerOptions;
    private GoogleMap mMap;
    private GoogleMap cMap;

    SQLiteDatabase localDB;
    LocalDB dataHelper;

    HashMap<LatLng, String> map = new HashMap<LatLng, String>();

    String currentUserGlobal,userTypeGlobal;

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
        //for now, dummy
        String userID = extras.getString("userID");
        currentUserGlobal = userID;
        Toast.makeText(getApplicationContext(), "Your id: " + currentUserGlobal, Toast.LENGTH_LONG).show();
        userTypeGlobal = extras.getString("userType");
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

        dataHelper = new LocalDB(this);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //actions bila user log out

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Log Out");
                builder.setMessage("Would you like to log out?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                localDB = dataHelper.getWritableDatabase();
                                localDB.execSQL("delete from login;");
                                finish();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing bitch
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
                            cMap = googleMap;
                            //Initialize Lat Lng
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            //Create marker options
                            MarkerOptions options = new MarkerOptions()
                                    .position(latLng)
                                    .title("You are Here")
                                    .snippet("Your location")
                                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_userlocation));

                            //zoom map scale 15
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                            String pass = null;
                            map.put(new LatLng(location.getLatitude(), location.getLongitude()), pass);
                            mMap.addMarker(options);
                            //Toast.makeText(getApplicationContext(), "self marker: " + mMap.get(Marker.get), Toast.LENGTH_LONG).show();
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

            markerList = gson.fromJson(response, Marker[].class);

            if(markerList.length < 1){
                Toast.makeText(getApplicationContext(), "No Hazards Recorded!", Toast.LENGTH_LONG).show();
                return;
            }

            BitmapDrawable bitmapDrawable;

            for(Marker info: markerList){
                Double lat = Double.valueOf(info.getLatitude());
                Double lng = Double.valueOf(info.getLongitude());
                String title = info.getHazard();
                String snippet = "Reported " + info.getTime() + " by " + info.getReportedBy();
                String pass = info.getReportID() + "#" + info.getHazardID() + "#" + info.getUserID()
                        + "#" + info.getReportedBy() + "#" + info.getTime();

                //Toast.makeText(getApplicationContext(), "Snippet: " + snippet, Toast.LENGTH_LONG).show();

                MarkerOptions marker= new MarkerOptions()
                        .position(new LatLng(lat,lng))
                        .title(title)
                        .snippet(snippet);
                //.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_hazardicon));

                if(marker.getTitle().equals("Road Obstruction")){
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.h1_roadobstruction);
                }else if(marker.getTitle().equals("Slippery Road")){
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.h2_slipperyroad);
                }else if(marker.getTitle().equals("Dangerous Pothole")){
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.h3_pothole);
                }else{
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.h4_trafficaccident);
                }
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), 150, 150, false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                map.put(new LatLng(lat, lng), pass);
                mMap.addMarker(marker);
                //Toast.makeText(getApplicationContext(), "marker: " + map.get(marker.getPosition()), Toast.LENGTH_LONG).show();

            } // end of for

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull com.google.android.gms.maps.model.Marker marker) {

                    //only intent if the marker is from DB (doesn't intent with currentLocation markers)
                    if(!(map.get(marker.getPosition()) == null)){

                        final boolean[] isEditable = {false};
                        Intent i = new Intent(MapsActivity.this, EditMarker.class);
                        String receivedString = map.get(marker.getPosition());
                        String[] outputToken = receivedString.split("#");

                        String reportID = outputToken[0];
                        String hazardID = outputToken[1];
                        String userID = outputToken[2];

                        //Toast.makeText(getApplicationContext(), "reportID: " + reportID +
                        //       " hazardID: " + hazardID + " userID: " + userID, Toast.LENGTH_LONG).show();

                        Toast.makeText(getApplicationContext(), "Your autho: " + userTypeGlobal, Toast.LENGTH_SHORT).show();

                        if(currentUserGlobal.equals(userID)  || userTypeGlobal.equals("Admin") || userTypeGlobal.equals("1")){
                            isEditable[0] = true;
                            Toast.makeText(getApplicationContext(), "Perh boleh edit sia", Toast.LENGTH_LONG).show();
                            i.putExtra("currentLocation", currentLocation);
                            i.putExtra("userID", currentUserGlobal);
                            i.putExtra("userType", userTypeGlobal);
                            i.putExtra("reportID",reportID);
                            i.putExtra("hazardID", hazardID);
                            i.putExtra("isEditable", isEditable[0]);
                            //finish();
                            startActivity(i);
                        }else{
                            String popupHazard = outputToken[1];
                            String popupUser = outputToken[3];
                            String popupTime = outputToken[4];

                            showPopup(popupHazard, popupUser, popupTime);
                            return true;
                        }


                        //Toast.makeText(getApplicationContext(), "Clicked: on custom marker!", Toast.LENGTH_SHORT).show();
                        return false;
                    }else{
                        Toast.makeText(getApplicationContext(), "This is you!", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                }
            });

        }//end of onresponse

    }; //end of sendrequest

    public Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectoriID){
        Drawable vectorDrawable  = ContextCompat.getDrawable(context, vectoriID);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showPopup(String hazard, String user, String time){
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.map_popup);

        Ptitle      = (TextView)dialog.findViewById(R.id.txtTitle11);
        Pusername   = (TextView)dialog.findViewById(R.id.txtUsername);
        Pdatetime   = (TextView)dialog.findViewById(R.id.txtDateTime);
        PbtnClose   = (Button)dialog.findViewById(R.id.btnOkay);

        ImageView icon = (ImageView)dialog.findViewById(R.id.imgIcon1);

        switch (hazard){
            case "1" :
                Ptitle.setText("Road Obstruction");
                icon.setImageResource(R.drawable.h1_roadobstruction);
                break;
            case "2" :
                Ptitle.setText("Slippery Road");
                icon.setImageResource(R.drawable.h2_slipperyroad);
                break;
            case "3" :
                Ptitle.setText("Dangerous Pothol");
                icon.setImageResource(R.drawable.h3_pothole);
                break;
            case "4" :
                Ptitle.setText("Accident");
                icon.setImageResource(R.drawable.h4_trafficaccident);
                break;
        }

        Pusername.setText("Reported By: " + user);
        Pdatetime.setText("Timestamp: \n" + time);

        PbtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}