package google.com.healthhigh.sensors;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Alan on 21/07/2017.
 * Classe que tem como responsabilidade obter os dados do sensor de aceleração e contar os passos dados
 * de acordo com o algoritmo de contagem.
 */

public class SensorPassoListener implements SensorEventListener {
    private String TAG = "sensor_aceleração_passo";
    private int valorAnterior = 0;
    private int numero_passos = 0;
    private int totalult = 3;

    private Handler handler_conta_passos;

    //    A sensibilidade poderá ser alterada pelo próprio usuário mais pra frente
    private double sensibilidade = 0.75;
    private double[] ultimos_valores = new double[totalult];
    private int ultimo_valor_index = 0;
    private SensorUI hud;
    private double avg = 0;

    private Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            if(!t.isInterrupted()){
                synchronized(this){
                    ultimos_valores[ultimo_valor_index] = avg;
                    ultimo_valor_index++;
                    if(ultimo_valor_index >= totalult){
                        ultimo_valor_index = 0;
                    }
                    double avg_ult = getMediaUltimosValores();
                    if ((avg_ult < sensibilidade) && avg > sensibilidade) {
                        numero_passos++;
                        Log.i("Passos", String.valueOf(numero_passos));
                        try {
                            handler_conta_passos.sendEmptyMessage(0);
                        } catch (Exception e) {
                            Log.e("Error EventoPasso", e.getMessage());
                        }
                    }
                }
            }
        }
    });

    SensorPassoListener() {
        reiniciaUltimosValores();
    }

    public void iniciarSensor(){
        t.start();
    }

    public void PararSensor(){
        t.interrupt();
    }

    private void reiniciaUltimosValores(){
        for(int i = 0; i < totalult ; i++){
            ultimos_valores[i] = 0;
        }
    }

    @SuppressLint("HandlerLeak")
    private void voidHandler(){
        handler_conta_passos = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i("",String.valueOf(numero_passos));
            }
        };
    }

    public void setHandler(Handler h){
        handler_conta_passos = h;
    }

    public void setHUD(SensorUI hud){
        this.hud = hud;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        /*
        * Sensores do tipo TYPE_ACCELEROMETER retornam 3 valores no atributo values:
        * [0] - Valor da aceleração no eixo X;
        * [1] - Valor da aceleração no eixo Y;
        * [2] - Valor da aceleração no eixo Z;
        * Todos os valores estão na unidade m/s^2 e já incluem a gravidade(o que quer que isso signifique)
        * */
        avg = (event.values[0] + event.values[1] + event.values[2]) / 3;
        t.run();
    }

    public int getNumero_passos() {
        return numero_passos;
    }

    private double getMediaUltimosValores() {
        double result = 0;
        for(int i = 0; i < totalult; i++){
            result += ultimos_valores[i];
        }
        if(result != 0){
            result/=totalult;
        }
        return result;
    }
}
