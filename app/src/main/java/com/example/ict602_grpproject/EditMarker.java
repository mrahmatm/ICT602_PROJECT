package com.example.ict602_grpproject;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class EditMarker extends AppCompatActivity {

    Button btnSubmit;
    EditText name, type, desc;

    RequestQueue queue;
    final String URL = "http://www.ict602.ml/getReports.php";
    //final String URL1 = "http://www.ict602.ml/getSingleReport.php";

    Marker[] markerList;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        name = (EditText) findViewById(R.id.txtName);
        desc = (EditText) findViewById(R.id.txtDesc);

        gson = new GsonBuilder().create();

        //remarks: kena receive userID + hazardID

        //data passing
        Bundle extras = getIntent().getExtras();

        Date currentTime = Calendar.getInstance().getTime();

        String userID = extras.getString("userID");

        Time dummyTime = new Time(currentTime.getTime());

        //Marker newMarker = new Marker(currentLocation, hazardName, hazardType, hazardDesc, dummyID, dummyTime);

        //hold jap, buat read dulu
        sendRequest();


    }
/*
    public void makeRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        });
    } */

    public void sendRequest(){
        queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, onSuccess, onError);
        queue.add(stringRequest);

    }

    public Response. Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //if success

            markerList = gson.fromJson(response, Marker[].class);

            if(markerList.length < 1){
                Toast.makeText(getApplicationContext(), "No Hazards Recorded!", Toast.LENGTH_LONG).show();
                return;
            }

            name.setText(markerList[0].getHazard());

            //desc.setText(markerList[0].get);

            Toast.makeText(getApplicationContext(), "Hazard Loaded!", Toast.LENGTH_LONG).show();
        }
    };

    public Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //if fail
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}