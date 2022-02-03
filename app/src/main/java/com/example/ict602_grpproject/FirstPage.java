package com.example.ict602_grpproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    Button loginUser, loginAdmin, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        loginUser = (Button) findViewById(R.id.FirstPage_button);
        loginUser = (Button) findViewById(R.id.FirstPage_button2);
        signUp = (Button) findViewById(R.id.FirstPage_button3);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(FirstPage.this, SignUpPage.class);
                startActivity(signUp);
            }
        });


    }
}