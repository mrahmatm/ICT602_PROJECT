package com.example.ict602_grpproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    EditText username, password;
    Button login, back;
    RequestQueue signin;
    ProgressBar progress;
    String URL = "http://www.ict602.ml/loginUser.php", statusCode, statusDesc, userID, loggedUsername, userType;

    LocalDB dataHelper;
    SQLiteDatabase localDB;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.Login_username);
        password = (EditText) findViewById(R.id.Login_password);
        login = (Button) findViewById(R.id.Login_button);
        back = (Button) findViewById(R.id.Login_button2);
        progress = (ProgressBar) findViewById(R.id.Login_progress);

        signin = Volley.newRequestQueue(getApplicationContext());

        dataHelper = new LocalDB(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progress.setVisibility(View.VISIBLE);
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
//        JsonObjectRequest send = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                progress.setVisibility(View.GONE);
//
//                //retrieve http status code
//                try {
//                    statusCode = response.getString("statusCode");
//                    statusDesc = response.getString("statusDesc");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (statusCode.equals("200")) {
//                    //if status code 200, then fetch remaining data
//                    try {
//                        userID = response.getString("userID");
//                        userType = response.getString("userType");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    //precaution to not create multiple userid in local db
//                    localDB = dataHelper.getWritableDatabase();
//                    localDB.execSQL("delete from login;");
//                    localDB.execSQL("insert into login(id, userid) values(null, '"+ userID +"');");
//
//                    if (userType.equals("1")) {
//                        //Admin
//                        Toast.makeText(getApplicationContext(), "Welcome, Admin " + username, Toast.LENGTH_LONG).show();
//                    }
//                    else if (userType.equals("2")) {
//                        //Peasant
//                        Toast.makeText(getApplicationContext(), "Welcome, " + username, Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "who you", Toast.LENGTH_LONG).show();
//                    }
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), statusDesc, Toast.LENGTH_LONG).show();
//                }
//            }
//        }, errorListener) {
//            @Override
//            protected Map<String, String> getParams () {
//                Map<String, String> params = new HashMap<>();
//
//                params.put("username", username.getText().toString());
//                params.put("password", password.getText().toString());
//
//                return params;
//            }
//        };

        StringRequest send = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progress.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //retrieve http status code
                    statusCode = jsonObject.getString("statusCode");
                    statusDesc = jsonObject.getString("statusDesc");

                    if (statusCode.equals("200")) {
                        //if status code 200, then fetch remaining data
                        userID = jsonObject.getString("id");
                        loggedUsername = jsonObject.getString("username");
                        userType = jsonObject.getString("userType");

                        //precaution to not create multiple userid in local db
                        localDB = dataHelper.getWritableDatabase();
                        localDB.execSQL("delete from login;");
                        localDB.execSQL("insert into login(id, userid, username, usertype) values(null, '"+ userID +"', '"+ loggedUsername +"', '"+ userType +"');");

                        //count local db data
//                        localDB = dataHelper.getReadableDatabase();
//                        cursor = localDB.rawQuery("select * from login", null);
//                        cursor.moveToFirst();
//                        String countCheck = String.valueOf(cursor.getCount());

                        if (userType.equals("1") || userType.equals("2")) {
                            //check if userID exist
                            //Intent map = new Intent(LoginPage.this, MapsActivity.class);
                            Intent map = new Intent(LoginPage.this, MainActivity.class);

                            map.putExtra("userID", userID);
                            map.putExtra("username", loggedUsername);
                            map.putExtra("userType", userType);
                            //finish();
                            startActivity(map);

                            if (userType.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Welcome, Admin " + loggedUsername, Toast.LENGTH_LONG).show();
                            }
                            else if (userType.equals("2")) {
                                Toast.makeText(getApplicationContext(), "Welcome, " + loggedUsername, Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "who you", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "who you. sign up la aiyo", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), statusDesc, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        signin.add(send);
    }

    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}