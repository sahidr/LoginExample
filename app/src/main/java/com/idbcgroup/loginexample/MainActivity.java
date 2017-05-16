package com.idbcgroup.loginexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView username;
    private Button logout;
    private String id = "";
    private String token = "";
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = getSharedPreferences("User_Auth", 0);
        id = prefs.getString("id",null);
        token = prefs.getString("token", null);
        role = prefs.getString("role",null);
        username = (TextView) findViewById(R.id.user);
        logout= (Button) findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor preferences = getSharedPreferences("User_Auth", 0).edit().clear();
                preferences.apply();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        if (token==null){

            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        } else {

            username.setText("ID: " + id + "\nToken: " + token + "\nRole: " + role);
            if (Integer.parseInt(id) == 1 ){

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, new Tendero());
                ft.commit();

            } else if (Integer.parseInt(id) == 2 ) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, new Transportador());
                ft.commit();

            }
        }


    }
}