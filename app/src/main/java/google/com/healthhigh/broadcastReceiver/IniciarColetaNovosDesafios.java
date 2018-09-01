package google.com.healthhigh.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import google.com.healthhigh.services.ObterDesafioService;

public class IniciarColetaNovosDesafios extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast receiver","iniciando coleta de novos desafios");
        Intent i = new Intent(context, ObterDesafioService.class);
//        intent.putExtra(ObterDesafioService.NOVOS_DESAFIOS, true);
        context.startService(i);
    }
}
