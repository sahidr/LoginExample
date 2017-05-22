package com.idbcgroup.loginexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private EditText pwd;
    private Button submit;
    private Button register;
    private ProgressBar load;
    private TextView requestpwd;
    private String id = "";
    private String token = "";
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = (EditText) findViewById(R.id.phone);
        pwd = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        load = (ProgressBar) findViewById(R.id.load);
        requestpwd = (TextView) findViewById(R.id.rpwd);
        register =(Button) findViewById(R.id.signup);

        requestpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(),"Password Recovery Intent",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, PasswordRestoreActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String phone_number = phone.getText().toString();
                String password = pwd.getText().toString();

                String regex = "^[+]?[0-9]{10,13}$";
                // ^0[412]\\d{9}$
                if (phone_number.matches(regex)){
                    Toast.makeText(LoginActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
                    SignIn login = new SignIn();
                    login.execute(phone_number, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }*/
                Toast.makeText(LoginActivity.this, "WELCOME!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private class SignIn extends AsyncTask<String, Object, Integer> {

        private HttpURLConnection urlConnection = null;

        @Override
        protected void onPreExecute() {
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... strings) {

            Integer result = -1;
            try {

                URL url = new URL("http://agruppastage.herokuapp.com/api-token-auth/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(5000);

                String credentials= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(strings[0],"UTF-8")
                        +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(strings[1],"UTF-8");

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(credentials);
                out.flush();
/*
                urlConnection.setConnectTimeout(7000);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("password", strings[1])
                        .appendQueryParameter("username",strings[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream toUrl = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(toUrl, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                toUrl.close();
                urlConnection.connect();
*/
                APIResponse api_response = JSONResponseController.getJsonResponse(urlConnection);

                if (api_response != null) {
                    Log.d("API_RESPONSE","status: " + api_response.getStatus());
                    Log.d("API_RESPONSE","body: " + api_response.getBody().toString());

                    if (api_response.getStatus()==HttpURLConnection.HTTP_OK){

                        id = api_response.getBody().getString("id");
                        token = api_response.getBody().getString("token");
                        role = api_response.getBody().getString("role");

                        SharedPreferences.Editor editor = getSharedPreferences("User_Auth", 0).edit();
                        editor.putString("id", id);
                        editor.putString("token", token);
                        editor.putString("role", role);
                        editor.apply();
                        Log.d("API_RESPONSE","Welcome");
                        result = 1;

                    } else if (api_response.getStatus()==HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.d("API_RESPONSE","USER NOT FOUND");
                        result = 0;
                    }else{
                        Log.d("API_RESPONSE","PLEASE TRY AGAIN, MAYBE FIELD ARE EMPTY");
                        result = -1;
                        }
                } else {
                    Log.d("JSON EMPTY", "Null");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Integer result){
            if (result == -1){
                Toast.makeText(getBaseContext(),"Please Try Again", Toast.LENGTH_SHORT).show();
            } else if (result==0){
                Toast.makeText(getBaseContext(),"User Not Found, Please Sign Up", Toast.LENGTH_SHORT).show();
            } else if (result == 1){
                System.out.println("ID: " + id + "\nToken: " + token + "\nRole: " + role);
                Toast.makeText(getBaseContext(),"Welcome "+role+"!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            load.setVisibility(View.GONE);
        }
    }
}