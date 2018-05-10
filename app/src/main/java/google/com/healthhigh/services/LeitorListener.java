package google.com.healthhigh.services;

import android.content.Context;

import google.com.healthhigh.domain.Noticia;

public interface LeitorListener {
    long getTempoLeitura();
    void setNewCronometro(Context c, Noticia n);
    void startCronometro();
    void stopCronometro();
}
