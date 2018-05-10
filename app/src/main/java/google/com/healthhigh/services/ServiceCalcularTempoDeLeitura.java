package google.com.healthhigh.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Chronometer;

import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.utils.DataHelper;

public class ServiceCalcularTempoDeLeitura extends Service implements LeitorListener, Chronometer.OnChronometerTickListener {
    ConexaoLeitor cl = new ConexaoLeitor();

    public static final String ACTION = "CONTAR_TEMPO_LEITURA_NOTICIA";
    public static final String I_TEMPO_INICIAL = "TEMPO_INICIAL";
    Chronometer contador;

    long tempo_inicial = 0;
    long tempo_leitura;

    Noticia noticia = new Noticia();

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        tempo_leitura = SystemClock.elapsedRealtime() - chronometer.getBase();
        Log.i("tempo_leitura", DataHelper.parseUT(tempo_leitura, "mm:ss"));
    }

    public class ConexaoLeitor extends Binder {
        public LeitorListener getCronometroLeitura(){
            return (ServiceCalcularTempoDeLeitura.this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        long tempo_inicial = intent.getLongExtra(I_TEMPO_INICIAL,0);
        return cl;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public long getTempoLeitura() {
        return tempo_leitura;
    }

    @Override
    public void setNewCronometro(Context c, Noticia n) {
        noticia = n;
        if(noticia.getTempoLeitura() > 0){
            contador.setBase(SystemClock.elapsedRealtime() - noticia.getTempoLeitura());
        }
        contador = new Chronometer(c);
        contador.setOnChronometerTickListener(this);
    }

    @Override
    public void startCronometro() {
        contador.start();
    }

    @Override
    public void stopCronometro() {
        tempo_leitura = contador.getBase();
        contador.stop();
    }
}
