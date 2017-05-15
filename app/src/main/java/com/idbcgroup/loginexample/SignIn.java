package com.idbcgroup.loginexample;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class SignIn extends AsyncTask<String, Void, Void> {


    private HttpURLConnection urlConnection = null;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {

            String username = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(strings[0],"UTF-8");
            String password = URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(strings[1],"UTF-8");
            URL url = new URL("http://agruppastage.herokuapp.com/api-token-auth/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
/*
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("password", strings[1])
                    .appendQueryParameter("username",strings[0]);
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            Log.d("flush","LLEGUE ANTES DEL FLUsH");
            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();
*/
            OutputStreamWriter toUrl = new OutputStreamWriter(urlConnection.getOutputStream());
            Log.d("flush","LLEGUE ANTES DEL FLUsH");
            toUrl.write(password+"&"+username);
            toUrl.flush();
            toUrl.close();
            JSONResponseController jcontroller = new JSONResponseController();
            APIResponse response = jcontroller.getJsonResponse(urlConnection);
            Log.d("response","leggue despues del api response");

            if (response != null) {
                Log.d("NO VACIO","entre");
                System.out.println("status: " + response.getStatus());
                System.out.println("body: " + response.getBody().toString());
            }else{
                Log.d("vvacio","RETORNE NULLL");
            }
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}