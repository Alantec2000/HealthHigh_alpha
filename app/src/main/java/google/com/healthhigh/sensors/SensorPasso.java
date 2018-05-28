package google.com.healthhigh.sensors;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;

import google.com.healthhigh.utils.Toaster;

/**
 * Created by Alan on 21/07/2017.
 * Classe de gerenciamento de estado do pedômetro.
 */

public class SensorPasso {
    private Sensor stepSensor;
    private SensorUI UI;
    private SensorPassoListener listener;
    private SensorManager sensorManager;
    private Context context;

    public SensorPasso(Context c) {
        this.context = c;
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

    public void iniciarPedometro(){
        if(stepSensor != null){
            listener = new SensorPassoListener();
            sensorManager.registerListener(listener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void unsetEventListener(){
        sensorManager.unregisterListener(listener);
    }

    public int getPassos(){
        return listener != null ? listener.getNumero_passos() : 0;
    }

    public void setHandler(Handler h){
        listener.setHandler(h);
    }

    public void setStepSensor() {
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        } else {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        PackageManager pm = context.getPackageManager();
        if (stepSensor == null) {
            Toaster.toastLongMessage(context,"Seu celular não possui um sensor de aceleração.");
        }
    }
}