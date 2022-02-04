package com.example.ict602_grpproject;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditMarker extends AppCompatActivity {

    Button btnSubmit, btnDelete;

    RadioGroup radGrp;
    RadioButton radBtn1, radBtn2, radBtn3;

    TextView preview;

    String checkedHazard;

    RequestQueue queue;
    final String URLUpdate = "http://www.ict602.ml/getReports.php";
    final String URLDelete = "http://www.ict602.ml/deleteReport.php";

    Marker[] markerList;
    Message[] messageList;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_marker);

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

        btnSubmit = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        gson = new GsonBuilder().create();

        //data passing
        Bundle extras = getIntent().getExtras();
        boolean isEditable = extras.getBoolean("isEditable");
        String targetReport = extras.getString("reportID");
        String defaultHazard = extras.getString("hazardID");

        //preview.findViewById(R.id.txtPreviewID);
        /*

        i.putExtra("userID", currentUserGlobal);
                    i.putExtra("reportID",reportID);
                    i.putExtra("hazardID", hazardID);
                    i.putExtra("isEditable", isEditable[0]);

         */

        //preview.setText("Hazard ID: " + defaultHazard.toString());

        Toast.makeText(getApplicationContext(), "sent default hazard: " + defaultHazard, Toast.LENGTH_LONG).show();

        switch (defaultHazard){
            case "1": radBtn1.performClick();
            case "2": radBtn2.performClick();
            case "3": radBtn3.performClick();
        }

        if(isEditable){
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendUpdateRequest(targetReport);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendDeleteRequest(targetReport);
                }
            });
        }else{
           Toast.makeText(getApplicationContext(), "Eh kau xleh edit anat", Toast.LENGTH_SHORT).show();
        }



        //Marker newMarker = new Marker(currentLocation, hazardName, hazardType, hazardDesc, dummyID, dummyTime);

        //hold jap, buat read dulu



    }

    public void sendUpdateRequest(String reportID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Connection OK!", Toast.LENGTH_SHORT).show();
            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                //POST key and values
                Map<String, String> params = new HashMap<>();
                params.put("reportID", reportID);

                String checkedHazard;

                switch (radGrp.getCheckedRadioButtonId()){
                    case R.id.radID1: checkedHazard = "1";
                    case R.id.radID2: checkedHazard = "2";
                    case R.id.radID3: checkedHazard = "3";
                        break;
                    default:
                        checkedHazard= "0";
                }

                params.put("hazardID", checkedHazard);

                return params;
            }
        };

        queue.add(stringRequest);

    }

    public void sendDeleteRequest(String reportID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //messageList = gson.fromJson(response, Message[].class);

               // for(Message msg: messageList){
                 //   String current = msg.getMsg();
                 //   Toast.makeText(getApplicationContext(), current, Toast.LENGTH_LONG).show();
                //}
            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                //POST key and values
                Map<String, String> params = new HashMap<>();
                params.put("reportID", reportID);

                return params;
            }
        };

        queue.add(stringRequest);
        //Toast.makeText(getApplicationContext(), "Target: " + reportID + "New: " + checkedHazard, Toast.LENGTH_LONG).show();
    }

        public Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };


}