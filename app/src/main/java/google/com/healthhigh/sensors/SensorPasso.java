package google.com.healthhigh.sensors;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.utils.Toaster;

/**
 * Created by Alan on 21/07/2017.
 */

public class SensorPasso {
    private Sensor stepSensor;
    private SensorUI UI;
    private SensorPassoListener settedListener;
    private SensorManager sensorManager;
    private Context c;

    public SensorPasso(Context c) {
        this.c = c;
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        setStepSensor();
    }

    public Sensor getStepSensor(){
        return this.stepSensor;
    }

    public void setUI(){
        if(UI == null){
            UI = new SensorUI();
        }
    }
    public SensorUI getUI(){
        return UI;
    }

    public void setEventListener(Desafio d){
        if(stepSensor != null){
            settedListener = new SensorPassoListener(c, d);
            sensorManager.registerListener(settedListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void setEventListener(SensorUI hud, Desafio d){
        if(stepSensor != null){
            settedListener = new SensorPassoListener(c, d);
            settedListener.setHUD(hud);
            sensorManager.registerListener(settedListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void unsetEventListener(){
        sensorManager.unregisterListener(settedListener);
    }

    public Sensor setStepSensor() {
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        } else {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        PackageManager pm = c.getPackageManager();
        if (stepSensor == null) {
            Toaster.toastLongMessage(c,"Seu celular não possui um sensor de aceleração.");
        }
        return stepSensor;
    }
}