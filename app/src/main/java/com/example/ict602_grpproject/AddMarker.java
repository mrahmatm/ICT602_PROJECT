package com.example.ict602_grpproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMarker extends AppCompatActivity {

    Button btnSubmit;
    EditText name, type, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        name = (EditText) findViewById(R.id.txtName);
        type = (EditText) findViewById(R.id.txtHazardType);
        desc = (EditText) findViewById(R.id.txtDesc);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //content of the edittext's
                String hazardName = name.getText().toString();
                String hazardType = type.getText().toString();
                String hazardDesc = desc.getText().toString();
            }
        });
    }
}