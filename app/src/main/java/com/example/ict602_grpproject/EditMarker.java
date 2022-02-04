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

    RequestQueue queueU, queueD;
    final String URLUpdate = "http://www.ict602.ml/updateReport.php";
    final String URLDelete = "http://www.ict602.ml/deleteReport.php";

    Marker[] markerList;
    Message[] messageList;
    Gson gson;

    private String checkRadio(View v){
        int current = radGrp.getCheckedRadioButtonId();
        String output = "0";
        switch(current){
            case R.id.radID1: output = "1"; break;
            case R.id.radID2: output = "2"; break;
            case R.id.radID3: output = "3"; break;
        }
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_marker);

        radGrp = (RadioGroup) findViewById(R.id.grpRadio);
        radBtn1 = (RadioButton) findViewById(R.id.radID1);
        radBtn2 = (RadioButton) findViewById(R.id.radID2);
        radBtn3 = (RadioButton) findViewById(R.id.radID3);

        //Toast.makeText(getApplicationContext(), "current radio: " + getTheCheckedHazard(), Toast.LENGTH_LONG).show();

        btnSubmit = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        gson = new GsonBuilder().create();
        queueU = Volley.newRequestQueue(getApplicationContext());
        queueD = Volley.newRequestQueue(getApplicationContext());
        //data passing
        Bundle extras = getIntent().getExtras();
        boolean isEditable = extras.getBoolean("isEditable");
        String targetReport = extras.getString("reportID");
        String defaultHazard = extras.getString("hazardID");

        switch(defaultHazard){
            case "1" : radBtn1.performClick(); break;
            case "2" : radBtn2.performClick(); break;
            case "3" : radBtn3.performClick(); break;
        }

        //Toast.makeText(getApplicationContext(), "reportID: " + targetReport +
         //       " hazardID: " + defaultHazard + " edit: " + isEditable, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "you're in onc create!", Toast.LENGTH_SHORT).show();
        //checkTheHazard(defaultHazard);

        //Toast.makeText(getApplicationContext(), "sent default hazard: " + defaultHazard, Toast.LENGTH_LONG).show();

        //if(isEditable){
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String selected = checkRadio(v);
                    sendUpdateRequest(targetReport, selected);
                    finish();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendDeleteRequest(targetReport);
                }
            });
       // }else{
        //   Toast.makeText(getApplicationContext(), "Eh kau xleh edit anat", Toast.LENGTH_SHORT).show();
       // }

    }

    public void sendUpdateRequest(String reportID, String input) {
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
                params.put("hazardID", input);

                return params;
            }
        };

        queueU.add(stringRequest);
        Toast.makeText(getApplicationContext(), "Hazard Updated!", Toast.LENGTH_SHORT).show();
    }

    public void sendDeleteRequest(String reportID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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

        queueD.add(stringRequest);
        Toast.makeText(getApplicationContext(), "Hazard Deleted!", Toast.LENGTH_SHORT).show();
    }

        public Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };


}