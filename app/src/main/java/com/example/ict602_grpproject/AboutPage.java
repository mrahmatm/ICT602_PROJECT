package com.example.ict602_grpproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        textView=findViewById(R.id.textViewLink);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}