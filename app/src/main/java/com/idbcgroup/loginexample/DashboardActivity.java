package com.idbcgroup.loginexample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DashboardActivity extends AppCompatActivity {

    private ImageButton back;
    private ImageButton edit;
    private GraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (ImageButton) findViewById(R.id.back);
        edit = (ImageButton) findViewById(R.id.edit);

        graph = (GraphView) findViewById(R.id.graph);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        series.setTitle("Random Curve 1");
        series.setColor(getResources().getColor(R.color.colorAccent));

        series.setSpacing(50);
/*
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
*/
        graph.addSeries(series);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, EditProfile.class);
                startActivity(i);
            }
        });

    }
}
