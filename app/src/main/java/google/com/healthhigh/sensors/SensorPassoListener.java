package google.com.healthhigh.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.utils.Toaster;

/**
 * Created by Alan on 21/07/2017.
 */

public class SensorPassoListener implements SensorEventListener {
    private Desafio desafio = null;
    private DesafioDAO desafio_dao = null;
    private String TAG = "sensor_aceleração_passo";
    private int valorAnterior = 0;
    private int nPassos = 0;
    private int totalult = 3;
    //    A sensibilidade poderá ser alterada pelo próprio usuário mais pra frente
    private double sensibilidade = 0.75;
    private double[] ultimosValores = new double[totalult];
    private int ultimoValorIndex = 0;
    private SensorUI hud;
    private Context context;
    private double avg = 0;
    Handler handle_n_passos = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hud.getnPassos().setText(String.valueOf(nPassos));
        }
    };
    Handler handle_conclusao = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toaster.toastShortMessage(context, "Desafio concluído");
            desafio.setStatus(Desafio.CONCLUIDO);
            desafio_dao.updateStatus(desafio, desafio.getStatus());
            ((Activity) context).finish();
        }
    };
    private Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            synchronized(this){
                ultimosValores[ultimoValorIndex] = avg;
                ultimoValorIndex++;
                if(ultimoValorIndex >= totalult){
                    ultimoValorIndex = 0;
                }
                double avg_ult = getMediaUltimosValores();
                if ((avg_ult < sensibilidade) && avg > sensibilidade) {
                    nPassos++;
                    if (hud != null) {
                        try {
                            handle_n_passos.sendEmptyMessage(0);
                        } catch (Exception e) {
                            Log.e("Error EventoPasso", e.getMessage());
                        }
                    } else {
                        Log.i("Media", String.valueOf(avg));
                        Log.i("Passos", String.valueOf(nPassos));
                    }
                }
                if(desafio != null &&
                   desafio.getStatus() != Desafio.CONCLUIDO &&
                   nPassos >= desafio.getQuantidade()){
                    handle_conclusao.sendEmptyMessage(0);
                }
            }
        }
    });

    public SensorPassoListener(Context c, Desafio d) {
        desafio = d;
        context = c;
        desafio_dao = new DesafioDAO(context);
        t.start();
        reiniciaUltimosValores();
    }

    private void reiniciaUltimosValores(){
        for(int i = 0; i < totalult ; i++){
            ultimosValores[i] = 0;
        }
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

    public double getMediaUltimosValores() {
        double result = 0;
        for(int i = 0; i < totalult; i++){
            result += ultimosValores[i];
        }
        if(result != 0){
            result/=totalult;
        }
        return result;
    }
}
