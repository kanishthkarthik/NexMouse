package com.blivic.nexmouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Test extends ActionBarActivity{

    private SensorManager mSensorManager;
    private Sensor mSensor;
    Socket socket;
    PrintWriter out;
    float x,y,z,xp,yp,zp;
    int c;
    String prevmotionstate, motionstate;

    //Low pass to remove upto 0.1 value noise
    float alpha;
    boolean flag;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        view = (TextView) findViewById(R.id.tv);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        alpha = 2f;
        x=xp=y=yp=z=zp=0f;
        flag = false;
        motionstate =prevmotionstate= "";

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                //double total = Math.sqrt(x * x + y * y + z * z);
                if((xp-x)*(xp-x)>alpha || (yp-y)*(yp-y)>alpha || (zp-z)*(zp-z)>alpha)
                {
                    //motionstate = getmotionstate();
                    //if(motionstate!=prevmotionstate) {
                    sendval();
                    xp = x;
                    yp = y;
                    zp = z;
                    //}
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

        }, mSensor, SensorManager.SENSOR_DELAY_UI);

        Button button_send = (Button)findViewById(R.id.send);
        button_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flag = true;
            }
        });

        Button button_lc = (Button)findViewById(R.id.button_lc);
        button_lc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                c = 1;
            }
        });

        Button button_rc = (Button)findViewById(R.id.button_rc);
        button_rc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                c = 2;
            }
        });
    }
    private void sendval(){

        if (flag) {//As left is to be made -ve in our convention

            try {
                socket = new Socket("10.14.56.136", 5267);
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                //As left is to be made -ve in our convention
                out.println(""+(-x)+","+(-y)+","+(c));
                c=0;

                out.close();
                socket.close();
            }catch (UnknownHostException e) {
                view.setText("Don't know about host: hostname");
            } catch (IOException e){
                view.setText("Couldn't get I/O for the connection to: hostname");
            }
            //out.println(sensorsX + "," + sensorsY + "," + sensorsZ);
            //view.setText("Sent stuff I guess");
        }
    }
}