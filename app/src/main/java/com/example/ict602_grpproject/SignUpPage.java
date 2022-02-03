package com.example.ict602_grpproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.*;

public class SignUpPage extends AppCompatActivity {

    EditText username, password;
    Button submit, back;
    RequestQueue signup;
    final String URL = "http://ict602.ml/createUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        username = (EditText) findViewById(R.id.SignUp_username);
        password = (EditText) findViewById(R.id.SignUp_password);
        submit = (Button) findViewById(R.id.SignUp_submit);
        back = (Button) findViewById(R.id.SignUp_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}