package com.example.ict602_grpproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddMarker extends AppCompatActivity {

    Button btnSubmit;
    EditText name, type, desc;
    RequestQueue queue;

    final String URL = "http://www.ict602.ml/insertReport.php";

    RadioGroup radGrp;
    RadioButton radBtn1, radBtn2, radBtn3;
    String checkedHazard = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        radGrp = (RadioGroup) findViewById(R.id.grpRadio);
        radBtn1 = (RadioButton) findViewById(R.id.radID1);
        radBtn2 = (RadioButton) findViewById(R.id.radID2);
        radBtn3 = (RadioButton) findViewById(R.id.radID3);

        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radID1:
                        checkedHazard = "1";
                        break;
                    case R.id.radID2:
                        checkedHazard = "2";
                        break;
                    case R.id.radID3:
                        checkedHazard = "3";
                        break;
                }
            }
        });

        queue = Volley.newRequestQueue(getApplicationContext());

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //data passing
        Bundle extras = getIntent().getExtras();
        //if so, set all the edittext's and textview's based on values retrieved
        Location currentLocation = extras.getParcelable("currentLocation");
        String userID = extras.getString("userID");
        //remark: nanti pass user type sekali
        String userType = "2";

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //content of the edittext's

                //String hazardType = type.getText().toString();
                //String hazardDesc = desc.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                Date currentTime = Calendar.getInstance().getTime();

                Time dummyTime = new Time(currentTime.getTime());

               // Marker newMarker = new Marker(currentLocation, hazardName, hazardType, hazardDesc, userID, dummyTime);

                //hold jap, buat read dulu
                makeRequest(currentLocation, checkedHazard, userID, currentDateandTime);
            }
        });
    }

    //basically POST method
    public void makeRequest(Location currentLocation, String hazardID, String userID, String currentTime){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Connection OK!", Toast.LENGTH_SHORT).show();
            }
        }, errorListener){
            @Override
            protected Map<String, String> getParams (){
                //POST key and values
                Map<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(currentLocation.getLatitude()));
                params.put("lon", String.valueOf(currentLocation.getLongitude()));
                params.put("hazardID", hazardID);
                params.put("userID", userID);
                params.put("dateTime", currentTime);
                return params;
            }
        };

        queue.add(stringRequest);
        Toast.makeText(getApplicationContext(),
                "Hazard Reported!, Coor: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude()
                , Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),
                "\nHazard Reported!, hazard: " + hazardID
                , Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),
                "\nHazard Reported!, datetime: " + currentTime
                , Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),
                "\nHazard Reported!, userid: " + userID
                , Toast.LENGTH_LONG).show();

    }

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}