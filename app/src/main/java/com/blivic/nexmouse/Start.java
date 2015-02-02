package com.blivic.nexmouse;

/**
 * Created by Kanishth Karthik on 9/9/2014.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Start extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the initial page
        setContentView(R.layout.activity_start);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //call home screen after 700 milliseconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run()
            {
                Intent intent = new Intent(Start.this, Test.class);
                startActivity(intent);
                finish();
            }
        }, 700);
    }

}