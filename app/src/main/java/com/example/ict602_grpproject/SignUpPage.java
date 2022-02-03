package com.example.ict602_grpproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {

    EditText username, password;
    Button submit, back;
    RequestQueue signup;
    final String URL = "http://www.ict602.ml/createUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        username = (EditText) findViewById(R.id.SignUp_username);
        password = (EditText) findViewById(R.id.SignUp_password);
        submit = (Button) findViewById(R.id.SignUp_submit);
        back = (Button) findViewById(R.id.SignUp_back);

        signup = Volley.newRequestQueue(getApplicationContext());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void makeRequest() {
        StringRequest send = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                finish();
            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<>();

                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };
        signup.add(send);
    }

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}