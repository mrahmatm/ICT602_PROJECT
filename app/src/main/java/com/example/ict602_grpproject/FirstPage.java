package com.example.ict602_grpproject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    Button loginUser, loginAdmin, signUp;

    Button testbutton;

    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        loginUser = (Button) findViewById(R.id.FirstPage_button);
        loginUser = (Button) findViewById(R.id.FirstPage_button2);
        signUp = (Button) findViewById(R.id.FirstPage_button3);
        aboutButton=(Button)findViewById(R.id.btnAbout);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(FirstPage.this, SignUpPage.class);
                startActivity(signUp);
            }
        });

        //deletable
        testbutton = (Button) findViewById(R.id.btnTest);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstPage.this, MapsActivity.class);
                startActivity(i);
            }
        });
        //end of deletable

            //nanti yg deletable ni aku nak letak about button, so for now dia compact sikit la sbb nak test
    }
    public void About ( View view){
        Intent intent = new Intent( FirstPage.this, AboutPage.class);
        startActivity(intent);
    }
}