package com.example.ict602_grpproject;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class AddMarker extends AppCompatActivity {

    Button btnSubmit;
    EditText name, type, desc;

    RequestQueue queue;
    final String URL = "www.ict602.ml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        name = (EditText) findViewById(R.id.txtName);
        type = (EditText) findViewById(R.id.txtHazardType);
        desc = (EditText) findViewById(R.id.txtDesc);


        //data passing
        Bundle extras = getIntent().getExtras();

        //if so, set all the edittext's and textview's based on values retrieved
        Location currentLocation = extras.getParcelable("currentLocation");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //content of the edittext's
                String hazardName = name.getText().toString();
                String hazardType = type.getText().toString();
                String hazardDesc = desc.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();

                int dummyID = 00001;
                Time dummyTime = new Time(currentTime.getTime());

                Marker newMarker = new Marker(currentLocation, hazardName, hazardType, hazardDesc, dummyID, dummyTime);

                //hold jap, buat read dulu
                //makeRequest();
            }
        });
    }
/*
    public void makeRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        });
    } */

}