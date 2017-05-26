package com.idbcgroup.loginexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GreenPointRegisterActivity extends AppCompatActivity {

    private Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_point_register);

        cancel = (Button) findViewById(R.id.cancel);


    }

    public void cancelClick(View view){

        finish();

    }


}
