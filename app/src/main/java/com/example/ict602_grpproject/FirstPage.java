package com.example.ict602_grpproject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    Button login, signUp;

    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        login = (Button) findViewById(R.id.FirstPage_button);
        signUp = (Button) findViewById(R.id.FirstPage_button3);
        aboutButton=(Button)findViewById(R.id.btnAbout);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(FirstPage.this, SignUpPage.class);
                startActivity(signUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(FirstPage.this, LoginPage.class);
                startActivity(login);
            }
        });

    }
    public void About ( View view){
        Intent intent = new Intent( FirstPage.this, AboutPage.class);
        startActivity(intent);
    }
}