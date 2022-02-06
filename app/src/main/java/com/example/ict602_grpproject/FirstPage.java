package com.example.ict602_grpproject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    Button login, signUp, aboutButton;

    LocalDB dataHelper;
    SQLiteDatabase localDB;
    Cursor cursor;
    String loggedUserID, loggedUsername, loggedUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        //login = (Button) findViewById(R.id.FirstPage_button);
        //signUp = (Button) findViewById(R.id.FirstPage_button3);
        //aboutButton=(Button)findViewById(R.id.btnAbout);

        dataHelper = new LocalDB(this);

        localDB = dataHelper.getReadableDatabase();
        cursor = localDB.rawQuery("select * from login", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            loggedUserID = cursor.getString(1);
            loggedUsername = cursor.getString(2);
            loggedUserType = cursor.getString(3);

            Intent map = new Intent(FirstPage.this, MainActivity.class);

            map.putExtra("userID", loggedUserID);
            map.putExtra("username", loggedUsername);
            map.putExtra("userType", loggedUserType);
            //finish();
            startActivity(map);

            if (loggedUserType.equals("1")) {
                Toast.makeText(getApplicationContext(), "Welcome, Admin " + loggedUsername, Toast.LENGTH_LONG).show();
            }
            else if (loggedUserType.equals("2")) {
                Toast.makeText(getApplicationContext(), "Welcome, " + loggedUsername, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        }
        else {
            //none/multiple user data may exist
            Intent login = new Intent(FirstPage.this, LoginPage.class);
            startActivity(login);
        }
        finish();



        /*signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(FirstPage.this, SignUpPage.class);
                startActivity(signUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDB = dataHelper.getReadableDatabase();
                cursor = localDB.rawQuery("select * from login", null);

                if (cursor.getCount() == 1) {
                    cursor.moveToFirst();
                    loggedUserID = cursor.getString(1);
                    loggedUsername = cursor.getString(2);
                    loggedUserType = cursor.getString(3);

                    Intent map = new Intent(FirstPage.this, MainActivity.class);

                    map.putExtra("userID", loggedUserID);
                    //map.putExtra("username", loggedUsername);
                    map.putExtra("userType", loggedUserType);
                    //finish();
                    startActivity(map);

                    if (loggedUserType.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Welcome, Admin " + loggedUsername, Toast.LENGTH_LONG).show();
                    }
                    else if (loggedUserType.equals("2")) {
                        Toast.makeText(getApplicationContext(), "Welcome, " + loggedUsername, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    //none/multiple user data may exist
                    Intent login = new Intent(FirstPage.this, LoginPage.class);
                    startActivity(login);
                }
            }
        });*/

    }
}