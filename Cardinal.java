package com.holdings.siloaman.talktoiea;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardinalDirectionActivity extends AppCompatActivity implements SensorEventListener {

    float[] mGravity;
    float[] mGeomagnetic;
    float azimut;

    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardinal_direction);

        // SET UP SENSORS
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // DIRECTION DISPLAY
        final TextView DirectionTV = (TextView)findViewById(R.id.directionTextView);
        Button directionButton = (Button)findViewById(R.id.directionButton);

        directionButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){

                float degree = (float)(Math.toDegrees(azimut)+360)%360;


                //      Directions Cardinally
                //      North = 0, South = 180, East = 90, West = 270
               
                String currDegrees = Float.toString(degree);
                String currDirection = "unknown";

                if(degree >= 357.5 || degree <= 2.5)
                    currDirection = "North";
                if(degree >= 177.5 && degree <= 182.5)
                    currDirection = "South";
                if(degree >= 87.5 && degree <= 92.5)
                    currDirection = "East";
                if(degree >= 267.5 && degree <= 272.5)
                    currDirection = "West";

                if(degree > 2.5 && degree < 87.5)
                    currDirection = "North-East";
                if(degree > 92.5 && degree < 177.5)
                    currDirection = "South-East";
                if(degree > 182.5 && degree < 267.5)
                    currDirection = "South-West";
                if(degree > 272.5 && degree < 357.5)
                    currDirection = "North-West";

                // DISPLAY THE DIRECTION ON TEXTVIEW
                DirectionTV.setText(currDegrees + " degrees " + currDirection);
            }
        });


    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                azimut = orientation[0];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }    
    
}
