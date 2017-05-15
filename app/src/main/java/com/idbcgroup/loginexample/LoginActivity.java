package com.idbcgroup.loginexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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


public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private EditText pwd;
    private Button submit;
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

        requestpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Password Recovery Intent",Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = phone.getText().toString();
                String password = pwd.getText().toString();
                load.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
                SignIn login = new SignIn();
                login.execute(phone_number, password);
                //load.setVisibility(View.GONE);
            }
        });
    }

    private class SignIn extends AsyncTask<String, Object, Integer> {

        private HttpURLConnection urlConnection = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(String... strings) {

            Integer result = 0;
            try {

                URL url = new URL("http://agruppastage.herokuapp.com/api-token-auth/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);

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
                JSONResponseController jcontroller = new JSONResponseController();
                APIResponse api_response = jcontroller.getJsonResponse(urlConnection);

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
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Integer result){
            if (result == -1){
                System.out.println("Empty Fields");
                Toast.makeText(getBaseContext(),"Fields must be filled", Toast.LENGTH_SHORT).show();
            } else if (result==0){
                System.out.println("User not Found");
                Toast.makeText(getBaseContext(),"User Not Found, Please SignUp", Toast.LENGTH_SHORT).show();
            } else if (result == 1){
                System.out.println("ID: " + id + "\nToken: " + token + "\nRole: " + role);
                Toast.makeText(getBaseContext(),"Welcome "+role, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
            load.setVisibility(View.GONE);
        }
    }
}